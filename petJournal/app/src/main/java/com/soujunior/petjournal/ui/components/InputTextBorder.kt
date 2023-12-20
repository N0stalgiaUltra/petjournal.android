package com.soujunior.petjournal.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.inputTextBorder(
    shape: Shape,
    isError: Boolean,
    strokeWidth: Dp = 1.dp,
    dashWidth: Dp = 8.dp,
    gapWidth: Dp = 10.dp,
    cap: StrokeCap = StrokeCap.Round
) = this.drawWithContent{
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = Path()
    val color = if(isError) Color(0xFFFF917A) else Color.Gray

    path.addOutline(outline)

    val stroke = if(isError){
        Stroke(
            cap = cap,
            width = (strokeWidth*2).toPx(),
        )
    } else {
        Stroke(
            cap = cap,
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx(), 0f)
            )
        )
    }

    drawPath(
        path = path,
        style = stroke,
        color = color
    )

    drawContent()
}