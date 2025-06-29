package com.luciaaldana.eccomerceapp.core.utils

import java.text.DecimalFormat
import java.util.Locale

fun Double.toPriceFormat(): String {
    val formatter = DecimalFormat("#,##0.00")
    return "$ ${formatter.format(this)}"
}