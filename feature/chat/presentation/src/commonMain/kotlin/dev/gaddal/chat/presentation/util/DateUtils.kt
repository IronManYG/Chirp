package dev.gaddal.chat.presentation.util

import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.date_time_format_am
import chirp.feature.chat.presentation.generated.resources.date_time_format_pm
import chirp.feature.chat.presentation.generated.resources.today
import chirp.feature.chat.presentation.generated.resources.yesterday
import dev.gaddal.core.presentation.util.UiText
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
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

        val formattedDateTime = messageDateTime.to12HourFormat()

        return when (messageDateTime.date) {
            todayDate -> UiText.Resource(Res.string.today)
            yesterdayDate -> UiText.Resource(Res.string.yesterday)
            else -> formattedDateTime
        }
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
    private fun LocalDateTime.to12HourFormat(): UiText {
        // Convert 24h -> 12h and choose AM/PM-specific string resource
        val isPm = this.hour >= 12
        val hour12 = when (val h = this.hour % 12) {
            0 -> 12
            else -> h
        }

        val formatId = if (isPm) Res.string.date_time_format_pm else Res.string.date_time_format_am

        return UiText.Resource(
            id = formatId, args = arrayOf(
                this.date.day,           // %1$02d
                this.date.month.number,  // %2$02d
                this.date.year,          // %3$04d
                hour12,                  // %4$02d (12-hour)
                this.minute              // %5$02d
            )
        )
    }
}