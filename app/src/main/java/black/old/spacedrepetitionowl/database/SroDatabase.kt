package black.old.spacedrepetitionowl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import black.old.spacedrepetitionowl.models.*

@Database(
    entities = [Subject::class, Reminder::class],
    version = 1)
abstract  class SroDatabase : RoomDatabase() {
}