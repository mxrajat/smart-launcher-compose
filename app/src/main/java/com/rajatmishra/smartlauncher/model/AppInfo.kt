package com.rajatmishra.smartlauncher.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val label: String,
    val packageName: String,
    val activityName: String,
    val icon: Drawable?,
    val category: AppCategory = AppCategory.ALL,
    val isFavorite: Boolean = false
)
