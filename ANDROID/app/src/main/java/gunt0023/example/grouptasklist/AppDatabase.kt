package gunt0023.example.grouptasklist

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


    @Database(entities = arrayOf(Task::class),
        version = TodoListDBContract.DATABASE_VERSION)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun taskDao(): TaskDAO
    }

