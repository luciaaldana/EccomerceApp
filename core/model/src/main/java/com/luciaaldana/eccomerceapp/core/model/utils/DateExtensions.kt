package com.luciaaldana.eccomerceapp.core.model.utils

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
        minutes < 1 -> "Justo ahora"
        minutes == 1L -> "Hace 1 minuto"
        minutes < 60 -> "Hace $minutes minutos"
        hours == 1L -> "Hace 1 hora"
        hours < 24 -> "Hace $hours horas"
        days == 1L -> "Ayer"
        days < 7 -> "Hace $days días"
        else -> this.toReadableFormat()
    }
}