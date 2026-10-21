package com.rajatmishra.smartlauncher.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.rajatmishra.smartlauncher.model.AppInfo
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Smart Launcher Signature "Flower" Layout.
 * Positions apps radially like flower petals around a central hub.
 */
@Composable
fun FlowerLayout(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    radius: Dp = 110.dp,
    iconSize: Dp = 58.dp
) {
    Box(
        modifier = modifier.size(radius * 2 + iconSize),
        contentAlignment = Alignment.Center
    ) {
        val total = apps.size
        if (total == 0) return@Box

        apps.forEachIndexed { index, app ->
            // Distribute around 360 degrees evenly starting from top (-PI/2)
            val angle = (-PI / 2) + (2 * PI / total) * index
            val xOffset = (radius.value * cos(angle)).roundToInt()
            val yOffset = (radius.value * sin(angle)).roundToInt()

            Box(
                modifier = Modifier
                    .offset { IntOffset(xOffset * 3, yOffset * 3) } // DP to approximate density offset
                    .size(iconSize)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
                    .clickable { onAppClick(app) },
                contentAlignment = Alignment.Center
            ) {
                AppIcon(
                    drawable = app.icon,
                    contentDescription = app.label,
                    modifier = Modifier.size(iconSize - 12.dp)
                )
            }
        }
    }
}

@Composable
fun AppIcon(
    drawable: Drawable?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    if (drawable == null) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(Color.Gray)
        )
        return
    }

    val bitmap = remember(drawable) {
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 96
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 96
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bmp
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = contentDescription,
        modifier = modifier
    )
}
