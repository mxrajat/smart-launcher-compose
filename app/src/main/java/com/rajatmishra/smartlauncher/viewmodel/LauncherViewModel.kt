package com.rajatmishra.smartlauncher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rajatmishra.smartlauncher.data.AppRepository
import com.rajatmishra.smartlauncher.model.AppCategory
import com.rajatmishra.smartlauncher.model.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application.applicationContext)

    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()

    private val _selectedCategory = MutableStateFlow(AppCategory.COMMUNICATION)
    val selectedCategory: StateFlow<AppCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favoritePackageNames = MutableStateFlow<Set<String>>(emptySet())

    val filteredApps: StateFlow<List<AppInfo>> = combine(
        _allApps,
        _selectedCategory,
        _searchQuery,
        _favoritePackageNames
    ) { apps, category, query, favorites ->
        var list = if (query.isNotBlank()) {
            // Global search overrides category filter
            apps.filter { it.label.contains(query, ignoreCase = true) }
        } else {
            when (category) {
                AppCategory.FAVORITES -> apps.filter { favorites.contains(it.packageName) }
                AppCategory.ALL -> apps
                else -> apps.filter { it.category == category }
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val homeFlowerApps: StateFlow<List<AppInfo>> = combine(
        _allApps,
        _favoritePackageNames
    ) { apps, favorites ->
        val explicitFavs = apps.filter { favorites.contains(it.packageName) }
        if (explicitFavs.isNotEmpty()) {
            explicitFavs.take(6)
        } else {
            // Default top apps (phone, browser, camera, settings, messaging, music)
            apps.take(6)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            val list = repository.getInstalledApps()
            _allApps.value = list
        }
    }

    fun selectCategory(category: AppCategory) {
        _selectedCategory.value = category
        _searchQuery.value = "" // clear search when switching categories
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun launchApp(app: AppInfo) {
        repository.launchApp(app)
    }

    fun toggleFavorite(app: AppInfo) {
        val current = _favoritePackageNames.value.toMutableSet()
        if (current.contains(app.packageName)) {
            current.remove(app.packageName)
        } else {
            current.add(app.packageName)
        }
        _favoritePackageNames.value = current
    }
}
