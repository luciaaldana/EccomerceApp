package com.luciaaldana.eccomerceapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luciaaldana.eccomerceapp.core.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val nationality: String,
    val userImageUrl: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val version: Int?
)

fun UserEntity.toUser(): User {
    return User(
        _id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password,
        nationality = nationality,
        userImageUrl = userImageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        __v = version
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = _id ?: "",
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password,
        nationality = nationality,
        userImageUrl = userImageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        version = __v
    )
}