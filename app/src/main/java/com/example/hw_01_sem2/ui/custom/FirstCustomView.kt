package com.example.hw_01_sem2.ui.custom


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.math.min

@Composable
fun FirstCustomView(
    values: List<Int>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    require(values.size in 2..7)
    require(values.size == colors.size)
    require(colors.toSet().size == colors.size)
    require(values.all { it in 1..100 })

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .pointerInput(values) {
                    detectTapGestures { offset ->
                        val cx = size.width / 2
                        val cy = size.height / 2
                        val dx = offset.x - cx
                        val dy = offset.y - cy
                        val distance = sqrt(dx * dx + dy * dy)

                        var angle = atan2(dy.toDouble(), dx.toDouble()) * (180 / PI)
                        if (angle < 0) angle += 360

                        var relativeAngle = angle - 90
                        if (relativeAngle < 0) relativeAngle += 360

                        val maxRadius = min(size.width, size.height).toFloat() / 2f
                        val strokeWidth = maxRadius / (values.size * 1.5f)
                        val gap = strokeWidth * 0.3f

                        var clickedIndex: Int? = null

                        for (i in values.indices) {
                            val radius = maxRadius - (i * (strokeWidth + gap)) - strokeWidth / 2
                            val innerBound = radius - (strokeWidth / 2)
                            val outerBound = radius + (strokeWidth / 2)

                            if (distance in innerBound..outerBound) {
                                val sweep = (values[i] / 100f) * 360f
                                if (relativeAngle <= sweep) {
                                    clickedIndex = i
                                    break
                                }
                            }
                        }
                        selectedIndex = clickedIndex
                    }
                }
        ) {
            val maxRadius = min(size.width, size.height) / 2f
            val strokeWidth = maxRadius / (values.size * 1.5f)
            val gap = strokeWidth * 0.3f

            values.forEachIndexed { index, value ->
                val radius = maxRadius - (index * (strokeWidth + gap)) - strokeWidth / 2
                val sweepAngle = (value / 100f) * 360f

                val isSelected = selectedIndex == index
                val alpha = if (selectedIndex == null || isSelected) 1.0f else 0.3f
                val ringColor = colors[index].copy(alpha = alpha)

                drawArc(
                    color = ringColor,
                    startAngle = 90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            if (selectedIndex != null) {
                Text(
                    text = "${values[selectedIndex!!]}%",
                    color = colors[selectedIndex!!],
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Нажми\nна сектор",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}