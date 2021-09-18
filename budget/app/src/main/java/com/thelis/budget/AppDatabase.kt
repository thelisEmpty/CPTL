package com.thelis.budget

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Transction::class),version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao():transactionDao

}