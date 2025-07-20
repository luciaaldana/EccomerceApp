package com.luciaaldana.eccomerceapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.luciaaldana.eccomerceapp.data.database.dao.UserDao
import com.luciaaldana.eccomerceapp.data.database.dao.ProductDao
import com.luciaaldana.eccomerceapp.data.database.entity.UserEntity
import com.luciaaldana.eccomerceapp.data.database.entity.ProductEntity

@Database(
    entities = [UserEntity::class, ProductEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    
    companion object {
        const val DATABASE_NAME = "ecommerce_app_database"
    }
}