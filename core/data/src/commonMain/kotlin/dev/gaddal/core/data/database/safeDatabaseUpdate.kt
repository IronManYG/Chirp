package dev.gaddal.core.data.database

import androidx.sqlite.SQLiteException
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result

/**
 * Executes a database update operation safely, catching any SQLite exceptions
 * and returning a wrapped result indicating success or failure.
 *
 * @param update A suspend function that performs the database update operation.
 * @return A [Result] wrapping the outcome of the update operation. A successful update
 *         returns [Result.Success] with the result of the operation, while a failure
 *         due to a SQLite exception returns [Result.Failure] with a [DataError.Local.DISK_FULL] error.
 */
suspend inline fun <T> safeDatabaseUpdate(update: suspend () -> T): Result<T, DataError.Local> {
    return try {
        Result.Success(update())
    } catch (_: SQLiteException) {
        Result.Failure(DataError.Local.DISK_FULL)
    }
}