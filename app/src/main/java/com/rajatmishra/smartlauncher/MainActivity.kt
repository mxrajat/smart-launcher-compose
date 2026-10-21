package com.rajatmishra.smartlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rajatmishra.smartlauncher.ui.screens.AppDrawerScreen
import com.rajatmishra.smartlauncher.ui.screens.HomeScreen
import com.rajatmishra.smartlauncher.ui.theme.SmartLauncherTheme
import com.rajatmishra.smartlauncher.viewmodel.LauncherViewModel

enum class LauncherScreen {
    HOME,
    DRAWER
}

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    LauncherApp(viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh installed apps list if any new app was installed/uninstalled
        viewModel.loadApps()
    }
}

@Composable
fun LauncherApp(viewModel: LauncherViewModel) {
    var currentScreen by remember { mutableStateOf(LauncherScreen.HOME) }

    val filteredApps by viewModel.filteredApps.collectAsState()
    val flowerApps by viewModel.homeFlowerApps.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Handle Android system back gesture: Drawer -> Home
    BackHandler(enabled = currentScreen == LauncherScreen.DRAWER) {
        currentScreen = LauncherScreen.HOME
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            if (targetState == LauncherScreen.DRAWER) {
                (slideInVertically { it } + fadeIn()).togetherWith(
                    slideOutVertically { -it / 3 } + fadeOut()
                )
            } else {
                (slideInVertically { -it / 3 } + fadeIn()).togetherWith(
                    slideOutVertically { it } + fadeOut()
                )
            }
        },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            LauncherScreen.HOME -> {
                HomeScreen(
                    flowerApps = flowerApps,
                    onAppClick = { app -> viewModel.launchApp(app) },
                    onOpenDrawer = { currentScreen = LauncherScreen.DRAWER }
                )
            }
            LauncherScreen.DRAWER -> {
                AppDrawerScreen(
                    apps = filteredApps,
                    selectedCategory = selectedCategory,
                    searchQuery = searchQuery,
                    onCategorySelected = { category -> viewModel.selectCategory(category) },
                    onSearchQueryChange = { query -> viewModel.onSearchQueryChange(query) },
                    onAppClick = { app -> viewModel.launchApp(app) },
                    onAppLongClick = { app -> viewModel.toggleFavorite(app) }
                )
            }
        }
    }
}
