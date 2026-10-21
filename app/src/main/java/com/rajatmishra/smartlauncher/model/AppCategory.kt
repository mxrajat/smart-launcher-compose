package com.rajatmishra.smartlauncher.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppCategory(
    val title: String,
    val icon: ImageVector
) {
    FAVORITES("Favorites", Icons.Default.Star),
    COMMUNICATION("Communication", Icons.Default.Chat),
    INTERNET("Internet", Icons.Default.Language),
    MEDIA("Media", Icons.Default.PlayCircle),
    GAMES("Games", Icons.Default.SportsEsports),
    UTILITIES("Tools", Icons.Default.Build),
    SETTINGS("Settings", Icons.Default.Settings),
    ALL("All Apps", Icons.Default.Apps);

    companion object {
        val drawerCategories = listOf(
            COMMUNICATION,
            INTERNET,
            MEDIA,
            GAMES,
            UTILITIES,
            SETTINGS,
            ALL
        )
    }
}
