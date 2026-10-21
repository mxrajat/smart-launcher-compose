package com.rajatmishra.smartlauncher.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rajatmishra.smartlauncher.model.AppCategory

@Composable
fun CategoryRail(
    selectedCategory: AppCategory,
    onCategorySelected: (AppCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(68.dp)
            .fillMaxHeight()
            .background(Color.Black.copy(alpha = 0.45f))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        AppCategory.drawerCategories.forEach { category ->
            val isSelected = category == selectedCategory
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) Color.White.copy(alpha = 0.35f) else Color.Transparent,
                label = "cat_bg"
            )
            val iconTint by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                label = "cat_tint"
            )

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(backgroundColor)
                    .clickable { onCategorySelected(category) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
