package black.old.spacedrepetitionowl.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import black.old.spacedrepetitionowl.models.*

@Database(
    entities    = [Subject::class, Reminder::class],
    version     = 1)
abstract class SroDatabase : RoomDatabase() {
    abstract val subjectDao: SubjectDao
    abstract val reminderDao: ReminderDao

    // Create the SroDatabase as a singleton to prevent having multiple instances of
    // the database opened at the same time.
    companion object {
        private var INSTANCE: SroDatabase? = null

        fun getDatabase(context: Context) : SroDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(SroDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SroDatabase::class.java,
                    "sro_db_test_1")
                    // Wipes and rebuilds instead of migrating
                    // if no Migration object
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}