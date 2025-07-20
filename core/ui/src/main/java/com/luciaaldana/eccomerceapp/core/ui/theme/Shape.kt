package com.luciaaldana.eccomerceapp.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Material 3 Expressive Shapes
// More dynamic and expressive corner radius values
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

// Custom Expressive Shapes for special use cases
val ExpressiveShapes = object {
    val card = RoundedCornerShape(16.dp)
    val dialog = RoundedCornerShape(28.dp)
    val bottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    val fab = RoundedCornerShape(16.dp)
    val chip = RoundedCornerShape(8.dp)
    val button = RoundedCornerShape(20.dp)
    val textField = RoundedCornerShape(4.dp)
}