package com.jetbrains.kmpapp.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jetbrains.kmpapp.data.local.AppDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val dbFile = context.getDatabasePath("kmp_app_db")
    return Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
}
