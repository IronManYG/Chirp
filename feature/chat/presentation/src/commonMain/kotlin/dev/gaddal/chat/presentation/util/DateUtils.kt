package dev.gaddal.chat.presentation.util

import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.today
import chirp.feature.chat.presentation.generated.resources.yesterday
import dev.gaddal.core.presentation.util.UiText
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * A utility object responsible for handling date and time formatting for display in the UI.
 * Provides methods to customize the representation of date and time.
 */
object DateUtils {

    /**
     * Formats the given [Instant] into a user-friendly time representation based on the current system's timezone.
     * If the provided time corresponds to today, it returns a localized "Today" string. If it corresponds to yesterday,
     * it returns a localized "Yesterday" string. For all other dates, it formats the [Instant] into a "MM/DD/YYYY HH:MM AM/PM" format.
     *
     * @param instant The [Instant] representing the time to format.
     * @param clock An optional [Clock] instance used to determine the current date. Defaults to the system clock.
     * @return A [UiText] instance containing the localized string for "Today" or "Yesterday", or a dynamic string
     * representation of the formatted date and time for other dates.
     */
    fun formatMessageTime(instant: Instant, clock: Clock = Clock.System): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val messageDateTime = instant.toLocalDateTime(timeZone)
        val todayDate = clock.now().toLocalDateTime(timeZone).date
        val yesterdayDate = todayDate.minus(1, DateTimeUnit.DAY)

        val formattedDateTime = messageDateTime.format(
            LocalDateTime.Format {
                day()
                char('/')
                monthNumber()
                char('/')
                year()
                char(' ')
                amPmHour()
                char(':')
                minute()
                amPmMarker("am", "pm")
            }
        )

        return when (messageDateTime.date) {
            todayDate -> UiText.Resource(Res.string.today)
            yesterdayDate -> UiText.Resource(Res.string.yesterday)
            else -> UiText.DynamicString(formattedDateTime)
        }
    }
}