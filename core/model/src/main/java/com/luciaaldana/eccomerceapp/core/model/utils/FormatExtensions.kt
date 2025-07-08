package com.luciaaldana.eccomerceapp.core.model.utils

import java.text.DecimalFormat

fun Double.toPriceFormat(): String {
    val formatter = DecimalFormat("#,##0.00")
    return "$ ${formatter.format(this)}"
}