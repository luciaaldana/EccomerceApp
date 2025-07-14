package com.luciaaldana.eccomerceapp.data.auth.utils

import android.util.Base64

object PasswordEncoder {
    fun encodePassword(password: String): String {
        return Base64.encodeToString(password.toByteArray(), Base64.NO_WRAP).trim()
    }
}