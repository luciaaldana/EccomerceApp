package com.luciaaldana.eccomerceapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.luciaaldana.eccomerceapp.data.database.dao.UserDao
import com.luciaaldana.eccomerceapp.data.database.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    
    companion object {
        const val DATABASE_NAME = "ecommerce_app_database"
    }
}