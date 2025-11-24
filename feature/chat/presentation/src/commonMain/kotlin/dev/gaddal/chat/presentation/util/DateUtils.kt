package dev.gaddal.chat.presentation.util

import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.date_time_format_am
import chirp.feature.chat.presentation.generated.resources.date_time_format_pm
import chirp.feature.chat.presentation.generated.resources.today_format_am
import chirp.feature.chat.presentation.generated.resources.today_format_pm
import chirp.feature.chat.presentation.generated.resources.yesterday_format_am
import chirp.feature.chat.presentation.generated.resources.yesterday_format_pm
import dev.gaddal.core.presentation.util.UiText
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * A utility object responsible for handling date and time formatting for display in the UI.
 * Provides methods to customize the representation of date and time.
 */
object DateUtils {
    /**
     * Formats the given [Instant] into a user-friendly representation based on the current system's timezone.
     * - If the provided time corresponds to today, returns a localized "Today" string resource.
     * - If it corresponds to yesterday, returns a localized "Yesterday" string resource.
     * - Otherwise, returns a date-time string resource with discrete arguments (day, month, year, hour, minute)
     *   so locales can format them appropriately.
     *
     * Note: The actual formatting for the non-today/yesterday case should be handled in the string resource.
     *
     * @param instant The [Instant] representing the time to format.
     * @param clock An optional [Clock] instance used to determine the current date. Defaults to the system clock.
     * @return A [UiText] representing "Today", "Yesterday", or a localized date-time string with arguments.
     */
    fun formatMessageTime(instant: Instant, clock: Clock = Clock.System): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val messageDateTime = instant.toLocalDateTime(timeZone)
        val todayDate = clock.now().toLocalDateTime(timeZone).date
        val yesterdayDate = todayDate.minus(1, DateTimeUnit.DAY)

        return when (messageDateTime.date) {
            todayDate -> messageDateTime.formatAsRelativeTime(
                Res.string.today_format_am,
                Res.string.today_format_pm
            )

            yesterdayDate -> messageDateTime.formatAsRelativeTime(
                Res.string.yesterday_format_am,
                Res.string.yesterday_format_pm
            )

            else -> messageDateTime.formatAsAbsoluteDateTime()
        }
    }

    /**
     * Returns the hour in 1-12 format (e.g., 0 and 12 both become 12).
     */
    private val LocalDateTime.hourOf12: Int
        get() = when (val h = this.hour % 12) {
            0 -> 12
            else -> h
        }

    /**
     * Returns true if the time is Post Meridiem (PM).
     */
    private val LocalDateTime.isPm: Boolean
        get() = this.hour >= 12

    /**
     * Formats a [LocalDateTime] instance as a relative time string (AM/PM) using specified string resources.
     *
     * This function determines if the time is in the AM or PM period and returns
     * a [UiText.Resource] using the provided string resource IDs for formatting.
     *
     * @param amResource The [StringResource] representing the format to use for the AM period.
     * @param pmResource The [StringResource] representing the format to use for the PM period.
     * @return A [UiText] representing the formatted time string with localized arguments.
     */
    private fun LocalDateTime.formatAsRelativeTime(
        amResource: StringResource,
        pmResource: StringResource,
    ): UiText {
        val formatId = if (isPm) pmResource else amResource
        return UiText.Resource(
            id = formatId,
            args = arrayOf(
                hourOf12,    // %1$d
                this.minute  // %2$d
            )
        )
    }

    /**
     * Converts the current [LocalDateTime] instance to a 12-hour format with AM/PM designation.
     *
     * This method formats the date and time into a localized string resource, where:
     * - Hours are converted from a 24-hour format to a 12-hour format.
     * - The AM/PM designation is determined based on whether the hour is before or after 12 PM.
     * - The [UiText.Resource] encapsulates arguments for the day, month, year, hour, and minute,
     *   allowing proper localization and formatting using string resources.
     *
     * @return A [UiText.Resource] containing the formatted 12-hour time representation with appropriate
     * string arguments for localization.
     */
    private fun LocalDateTime.formatAsAbsoluteDateTime(): UiText {
        val formatId = if (isPm) Res.string.date_time_format_pm else Res.string.date_time_format_am

        return UiText.Resource(
            id = formatId,
            args = arrayOf(
                this.date.day,           // %1$d
                this.date.month.number,  // %2$d
                this.date.year,          // %3$d
                this.hourOf12,           // %4$d (12-hour)
                this.minute              // %5$d
            )
        )
    }
}