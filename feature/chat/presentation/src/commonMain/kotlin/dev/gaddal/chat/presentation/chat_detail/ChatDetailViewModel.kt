@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package dev.gaddal.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.today
import dev.gaddal.chat.domain.chat.ChatConnectionClient
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.chat.domain.message.MessageRepository
import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.chat.domain.models.ConnectionState
import dev.gaddal.chat.domain.models.OutgoingNewMessage
import dev.gaddal.chat.presentation.mappers.toUi
import dev.gaddal.chat.presentation.mappers.toUiList
import dev.gaddal.chat.presentation.model.MessageUi
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.util.DataErrorException
import dev.gaddal.core.domain.util.Paginator
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import dev.gaddal.core.presentation.util.UiText
import dev.gaddal.core.presentation.util.toUiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatDetailViewModel(
    private val chatRepository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val messageRepository: MessageRepository,
    private val connectionClient: ChatConnectionClient
) : ViewModel() {
    private val eventChannel = Channel<ChatDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private var currentPaginator: Paginator<String?, ChatMessage>? = null

    private val _state = MutableStateFlow(ChatDetailState())

    private val _chatId = MutableStateFlow<String?>(null)

    private val chatInfoFlow = _chatId
        .onEach { chatId ->
            if (chatId != null) {
                setupPaginatorForChat(chatId)
            } else {
                currentPaginator = null
            }
        }
        .flatMapLatest { chatId ->
            if (chatId != null) {
                chatRepository.getChatInfoById(chatId)
            } else emptyFlow()
        }

    private val canSendMessage = snapshotFlow { _state.value.messageTextFieldState.text.toString() }
        .map { it.isBlank() }
        .combine(connectionClient.connectionState) { isMessageBlank, connectionState ->
            !isMessageBlank && connectionState == ConnectionState.CONNECTED
        }

    private val stateWithMessages = combine(
        _state,
        chatInfoFlow,
        sessionStorage.observeAuthInfo()
    ) { currentState, chatInfo, authInfo ->
        if (authInfo == null) {
            return@combine ChatDetailState()
        }

        currentState.copy(
            chatUi = chatInfo.chat.toUi(authInfo.user.id),
            messages = chatInfo.messages.toUiList(authInfo.user.id)
        )
    }

    val state = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null) {
                stateWithMessages
            } else {
                _state
            }
        }
        .onStart {
            if (!hasLoadedInitialData) {
                observeConnectionState()
                observeChatMessages()
                observeCanSendMessage()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatDetailState()
        )

    fun onAction(action: ChatDetailAction) {
        when (action) {
            is ChatDetailAction.OnSelectChat -> switchChat(action.chatId)
            ChatDetailAction.OnChatOptionsClick -> onChatOptionsClick()
            is ChatDetailAction.OnDeleteMessageClick -> deleteMessage(action.message)
            ChatDetailAction.OnDismissChatOptions -> onDismissChatOptions()
            ChatDetailAction.OnDismissMessageMenu -> onDismissMessageMenu()
            ChatDetailAction.OnLeaveChatClick -> onLeaveChatClick()
            is ChatDetailAction.OnMessageLongClick -> onMessageLongClick(action.message)
            is ChatDetailAction.OnRetryClick -> retryMessage(action.message)
            ChatDetailAction.OnScrollToTop -> onScrollToTop()
            ChatDetailAction.OnSendMessageClick -> sendMessage()
            ChatDetailAction.OnRetryPaginationClick -> retryPagination()
            ChatDetailAction.OnHideBanner -> hideBanner()
            is ChatDetailAction.OnTopVisibleIndexChanged -> updateBanner(action.topVisibleIndex)
            is ChatDetailAction.OnFirstVisibleIndexChanged -> updateNearBottom(action.index)
            else -> Unit
        }
    }

    /**
     * Sets up a paginator for the chat based on the provided chat ID. This paginator is used to
     * load messages incrementally, handle pagination states, and manage errors or success events
     * for the chat.
     *
     * @param chatId The unique identifier of the chat for which the paginator is being set up.
     */
    private fun setupPaginatorForChat(chatId: String) {
        currentPaginator = Paginator(
            initialKey = null,
            onLoadUpdated = { isLoading ->
                _state.update { it.copy(isPaginationLoading = isLoading) }
            },
            onRequest = { beforeTimestamp ->
                messageRepository.fetchMessages(chatId, beforeTimestamp)
            },
            getNextKey = { messages ->
                messages.minOfOrNull { it.createdAt }?.toString()
            },
            onError = { throwable ->
                if (throwable is DataErrorException) {
                    _state.update {
                        it.copy(
                            paginationError = throwable.error.toUiText()
                        )
                    }
                }
            },
            onSuccess = { messages, _ ->
                _state.update {
                    it.copy(
                        endReached = messages.isEmpty(),
                        paginationError = null
                    )
                }
            }
        )

        _state.update {
            it.copy(
                endReached = false,
                isPaginationLoading = false,
            )
        }
    }

    /**
     * Observes the connection state of the chat and updates the `ChatDetailState` accordingly.
     *
     * This method listens to changes in the connection state emitted by `connectionClient.connectionState`.
     * When the connection transitions to `ConnectionState.CONNECTED`, it attempts to fetch the messages
     * for the current chat by invoking `messageRepository.fetchMessages`. Additionally, it updates the `_state`
     * to reflect the latest connection state.
     *
     * Behavior:
     * - If the connection state is `CONNECTED`, the method fetches messages for the current chat ID.
     *   The operation only proceeds if `_chatId` contains a valid chat ID.
     * - Updates the `ChatDetailState.connectionState` property with the current connection state.
     *
     * Side effects:
     * - Initiates a message fetch from `messageRepository` upon connection establishment.
     * - Updates the `_state` with the new connection state, triggering any observers on the state.
     */
    private fun observeConnectionState() {
        connectionClient
            .connectionState
            .onEach { connectionState ->
                if (connectionState == ConnectionState.CONNECTED) {
                    currentPaginator?.loadNextItems()
                }

                _state.update {
                    it.copy(
                        connectionState = connectionState
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Stores the identifier of the most recent message that has been processed.
     * This variable helps in tracking and ensuring that messages are not handled repeatedly.
     * It is nullable to represent the absence of any previously handled message.
     */
    private var lastHandledNewestMessageId: String? = null

    /**
     * Observes chat messages and integrates them into the application's state and event system.
     *
     * This method performs the following operations:
     * - Tracks the current list of messages from the application's state and ensures updates only occur
     *   when the message list changes.
     * - Observes a flow of new messages for the current chat using the `messageRepository`.
     * - Combines new messages with authentication information to transform the messages into UI models,
     *   and updates the application state with these transformed messages.
     * - Monitors whether the user is near the bottom of the chat view to handle new message notifications appropriately.
     * - Uses lastHandledNewestMessageId to prevent duplicate processing of the same message, ensuring
     *   that new message notifications are only triggered once per unique message.
     * - Combines the current messages, new messages, and the "is near bottom" flag to determine if a new message
     *   notification event should be emitted. If the user is near the bottom of the chat and there are new messages,
     *   a `ChatDetailEvent.OnNewMessage` event is sent to the event channel.
     *
     * This method uses Kotlin Flows to handle real-time updates and ensure reactivity in the chat interface.
     * The operations are scoped to the `viewModelScope` to manage coroutine lifecycle.
     */
    private fun observeChatMessages() {
        val currentMessages = state
            .map { it.messages }
            .distinctUntilChanged()

        val newMessages = _chatId.flatMapLatest { chatId ->
            if (chatId != null) {
                messageRepository.getMessagesForChat(chatId)
            } else emptyFlow()
        }

        val isNearBottom = state.map { it.isNearBottom }.distinctUntilChanged()

        combine(
            currentMessages,
            newMessages,
            isNearBottom
        ) { currentMessages, newMessages, isNearBottom ->
            val newestIncomingId = newMessages.firstOrNull()?.message?.id
            val newestCurrentId = currentMessages.firstOrNull()?.id

            val isTrulyNew =
                newestIncomingId != null &&
                        newestIncomingId != newestCurrentId &&
                        newestIncomingId != lastHandledNewestMessageId

            if (isTrulyNew && isNearBottom) {
                eventChannel.send(ChatDetailEvent.OnNewMessage)
                lastHandledNewestMessageId = newestIncomingId
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Observes the state of whether the user can send messages and updates the application state accordingly.
     *
     * This method listens to updates emitted by the `canSendMessage` flow. Each emitted value determines
     * whether the user is currently allowed to send a message. When an update is received, it modifies
     * the `canSendMessage` property within the current `ChatDetailState` by creating a new state object
     * with the updated value.
     *
     * Behavior:
     * - Subscribes to the `canSendMessage` flow.
     * - On each emitted value, updates the `_state` with a copy that reflects the new `canSendMessage` value.
     *
     * Scope:
     * The flow collection runs within the `viewModelScope`, ensuring the coroutine's lifecycle is bound to the ViewModel.
     */
    private fun observeCanSendMessage() {
        canSendMessage.onEach { canSend ->
            _state.update {
                it.copy(
                    canSendMessage = canSend
                )
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Updates the current chat context by switching to the specified chat and fetching its data.
     *
     * @param chatId The unique identifier of the chat to switch to. If null, no chat is selected.
     */
    private fun switchChat(chatId: String?) {
        _chatId.update { chatId }
        viewModelScope.launch {
            chatId?.let {
                chatRepository.fetchChatById(chatId)
            }
        }
    }

    /**
     * Handles the opening of the chat options menu by updating the state to reflect that the menu is now open.
     * Updates the `isChatOptionsOpen` property of the `ChatDetailState` to `true`.
     * This allows the UI to react to the change and display the chat options menu.
     */
    private fun onChatOptionsClick() {
        _state.update {
            it.copy(
                isChatOptionsOpen = true
            )
        }
    }

    /**
     * Deletes a local user message from the repository and handles errors by emitting events.
     *
     * This method launches a coroutine in the `viewModelScope` to execute the deletion
     * asynchronously. If the operation fails, it sends an appropriate error event
     * to the event channel.
     *
     * @param message The local user message to be deleted. It contains the necessary details,
     *                including the unique identifier required for the deletion operation.
     */
    private fun deleteMessage(message: MessageUi.LocalUserMessage) {
        viewModelScope.launch {
            messageRepository
                .deleteMessage(message.id)
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    /**
     * Handles the dismissal of the chat options menu.
     *
     * This method updates the current state of the chat detail screen by setting
     * the `isChatOptionsOpen` property to `false`. It is used to close the chat
     * options menu when the user dismisses it or performs an action that requires
     * its closure.
     */
    private fun onDismissChatOptions() {
        _state.update {
            it.copy(
                isChatOptionsOpen = false
            )
        }
    }

    /**
     * Handles the dismissal of the message options menu.
     *
     * This method updates the current application state to reflect that no message
     * currently has its options menu open. It achieves this by setting the
     * `messageWithOpenMenu` property to `null` in the `ChatDetailState`,
     * effectively closing any open message menus.
     *
     * This is typically called when the user dismisses the message menu
     * without selecting any specific action.
     */
    private fun onDismissMessageMenu() {
        _state.update {
            it.copy(
                messageWithOpenMenu = null
            )
        }
    }

    /**
     * Handles the event when the user clicks to leave the chat.
     *
     * This method performs the following steps:
     * 1. Retrieves the current chat ID; if null, it exits early.
     * 2. Updates the state to close the chat options menu.
     * 3. Performs an asynchronous operation:
     *    - Calls `leaveChat` on the `chatRepository` to leave the chat.
     *    - If the operation is successful:
     *      - Clears the current input in the message text field.
     *      - Resets the state by removing chat-related data such as `chatUi`, `messages`, and `bannerState`.
     *    - If the operation fails:
     *      - Sends an error event encapsulating a user-friendly error message.
     */
    private fun onLeaveChatClick() {
        val chatId = _chatId.value ?: return

        _state.update {
            it.copy(
                isChatOptionsOpen = false
            )
        }

        viewModelScope.launch {
            chatRepository
                .leaveChat(chatId)
                .onSuccess {
                    _state.value.messageTextFieldState.clearText()

                    _chatId.update { null }
                    _state.update {
                        it.copy(
                            chatUi = null,
                            messages = emptyList(),
                            bannerState = BannerState()
                        )
                    }
                }
                .onFailure { error ->
                    eventChannel.send(
                        ChatDetailEvent.OnError(
                            error.toUiText()
                        )
                    )
                }
        }
    }

    /**
     * Handles the event when the user performs a long click on a local user message.
     *
     * This action updates the current state to register the specific message that
     * has its menu open. This is often used to trigger contextual actions like
     * replying to, deleting, or copying the content of the message.
     *
     * @param message The local user message that was long-clicked. Includes details
     *                such as content and metadata of the message.
     */
    private fun onMessageLongClick(message: MessageUi.LocalUserMessage) {
        _state.update {
            it.copy(
                messageWithOpenMenu = message
            )
        }
    }

    /**
     * Retries sending a previously failed message.
     *
     * This method attempts to resend a specific local user message. It initiates the retry
     * operation by calling the `retryMessage` function of the `messageRepository` with the
     * identifier (`id`) of the message to be retried. If the operation fails, an error event
     * is sent to the event channel for further handling in the UI.
     *
     * @param message The local user message to be retried. The message includes information such as its
     *                unique identifier and content.
     */
    private fun retryMessage(message: MessageUi.LocalUserMessage) {
        viewModelScope.launch {
            messageRepository
                .retryMessage(message.id)
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    /**
     * Handles the scroll-to-top action in the chat view.
     *
     * This method is triggered when the user scrolls to the top of the chat, indicating that
     * they want to load older messages. It internally delegates to the `loadNextItems` function
     * to fetch the next set of messages from the paginator.
     *
     * Behavior:
     * - Invokes the `loadNextItems` method to load more messages.
     * - Ensures seamless integration with the pagination mechanism to incrementally fetch data.
     *
     * Use case includes scenarios where modern chat applications load messages in chunks
     * based on scroll actions.
     */
    private fun onScrollToTop() = loadNextItems()

    /**
     * Sends a textual message in the current chat.
     *
     * This method retrieves the current chat ID and the text content from the state.
     * If the message content is blank or no valid chat ID is available, the function exits early.
     * Otherwise, it constructs a new `OutgoingNewMessage` object with the necessary details
     * (e.g., chat ID, message ID, and content) and calls the `sendMessage` method of the `messageRepository`
     * to send the message asynchronously.
     *
     * Upon a successful message transmission:
     * - The text field state is cleared.
     *
     * In case of an error:
     * - An `OnError` event with a UI-friendly error message is emitted to the event channel.
     *
     * This method operates within the `viewModelScope` to handle coroutine lifecycle management
     * for asynchronous operations.
     */
    private fun sendMessage() {
        val currentChatId = _chatId.value
        val content = state.value.messageTextFieldState.text.toString().trim()
        if (content.isBlank() || currentChatId == null) {
            return
        }

        viewModelScope.launch {
            val message = OutgoingNewMessage(
                chatId = currentChatId,
                messageId = Uuid.random().toString(),
                content = content
            )

            messageRepository
                .sendMessage(message)
                .onSuccess {
                    state.value.messageTextFieldState.clearText()
                }
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    /**
     * Attempts to retry the pagination process for loading the next set of chat items.
     *
     * This method is typically called when a previous pagination attempt fails or encounters
     * an error. It triggers the `loadNextItems` method, which initiates the loading of the next
     * batch of chat items through the defined paginator.
     *
     * Behavior:
     * - Invokes the `loadNextItems` function to resume or restart the pagination process.
     * - Ensures that the application attempts to fetch more messages for the chat.
     *
     * Purpose:
     * - Used to handle retries when the chat content fails to load due to connectivity issues
     *   or other interruptions during pagination.
     */
    private fun retryPagination() = loadNextItems()

    /**
     * Triggers the loading of the next set of items in the chat, typically for pagination purposes.
     *
     * This method launches a coroutine within the `viewModelScope`, invoking the `loadNextItems`
     * function of the current paginator (`currentPaginator`). If no paginator is set up, the method
     * will have no effect.
     *
     * Behavior:
     * - Operates within the `viewModelScope` for lifecycle-bound coroutine management.
     * - Delegates the responsibility of loading the next set of items to the paginator.
     */
    private fun loadNextItems() {
        viewModelScope.launch {
            currentPaginator?.loadNextItems()
        }
    }

    /**
     * Hides the banner by updating the banner's visibility state to false.
     * This method modifies the underlying state to ensure that the banner
     * is not displayed in the UI.
     */
    private fun hideBanner() {
        _state.update {
            it.copy(
                bannerState = it.bannerState.copy(
                    isVisible = false
                )
            )
        }
    }

    /**
     * Updates the banner state based on the visible message index.
     *
     * @param topVisibleIndex The index of the topmost visible message in the list,
     *                        used to calculate the banner date.
     */
    private fun updateBanner(topVisibleIndex: Int) {
        val visibleDate = calculateBannerDateFromIndex(
            messages = state.value.messages,
            index = topVisibleIndex
        )

        _state.update {
            it.copy(
                bannerState = BannerState(
                    formattedDate = visibleDate,
                    isVisible = visibleDate != null
                )
            )
        }
    }

    /**
     * Updates the state to indicate whether the user is near the bottom of the list.
     *
     * @param firstVisibleIndex The index of the first visible item in the list.
     * A value less than or equal to 3 is considered near the bottom.
     */
    private fun updateNearBottom(firstVisibleIndex: Int) {
        _state.update {
            it.copy(
                isNearBottom = firstVisibleIndex <= 3
            )
        }
    }

    /**
     * Calculates the banner date from a given index in a list of messages.
     *
     * It scans through the messages starting from the provided index to find the nearest
     * date separator. If a valid date is found, it returns the corresponding banner date.
     * Otherwise, it returns null.
     *
     * @param messages A list of MessageUi objects which may contain messages and date separators.
     * @param index The starting index in the list from which the search for a date separator begins.
     * @return A UiText representing the banner date if found, or null if no valid date separator exists.
     */
    private fun calculateBannerDateFromIndex(
        messages: List<MessageUi>,
        index: Int
    ): UiText? {
        if (messages.isEmpty() || index < 0 || index >= messages.size) {
            return null
        }

        val nearestDateSeparator = (index until messages.size)
            .asSequence()
            .mapNotNull { index ->
                val item = messages.getOrNull(index)
                if (item is MessageUi.DateSeparator) item.date else null
            }
            .firstOrNull()

        return when (nearestDateSeparator) {
            is UiText.Resource -> {
                if (nearestDateSeparator.id == Res.string.today) null else nearestDateSeparator
            }

            else -> nearestDateSeparator
        }
    }
}