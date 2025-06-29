package com.luciaaldana.eccomerceapp.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Date.toReadableFormat(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toRelativeTime(): String {
    val now = Date()
    val diffInMillis = now.time - this.time

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    return when {
        minutes < 1 -> "justo ahora"
        minutes == 1L -> "hace 1 minuto"
        minutes < 60 -> "hace $minutes minutos"
        hours == 1L -> "hace 1 hora"
        hours < 24 -> "hace $hours horas"
        days == 1L -> "ayer"
        days < 7 -> "hace $days días"
        else -> this.toReadableFormat()
    }
}