package com.rajatmishra.smartlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import com.rajatmishra.smartlauncher.model.AppCategory
import com.rajatmishra.smartlauncher.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val launcherIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                launcherIntent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
            )
        } else {
            packageManager.queryIntentActivities(launcherIntent, 0)
        }

        val apps = resolveInfos.mapNotNull { resolveInfo ->
            val pkgName = resolveInfo.activityInfo.packageName
            val actName = resolveInfo.activityInfo.name
            // Skip the launcher itself
            if (pkgName == context.packageName) return@mapNotNull null

            val label = resolveInfo.loadLabel(packageManager).toString()
            val icon = resolveInfo.loadIcon(packageManager)
            val category = resolveCategory(resolveInfo.activityInfo.applicationInfo, pkgName)

            AppInfo(
                label = label,
                packageName = pkgName,
                activityName = actName,
                icon = icon,
                category = category
            )
        }

        apps.sortedBy { it.label.lowercase() }
    }

    private fun resolveCategory(appInfo: ApplicationInfo, packageName: String): AppCategory {
        val lowerPkg = packageName.lowercase()

        // 1. Android standard application category check (API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            when (appInfo.category) {
                ApplicationInfo.CATEGORY_GAME -> return AppCategory.GAMES
                ApplicationInfo.CATEGORY_AUDIO,
                ApplicationInfo.CATEGORY_VIDEO,
                ApplicationInfo.CATEGORY_IMAGE -> return AppCategory.MEDIA
                ApplicationInfo.CATEGORY_SOCIAL -> return AppCategory.COMMUNICATION
                ApplicationInfo.CATEGORY_NEWS -> return AppCategory.INTERNET
                ApplicationInfo.CATEGORY_PRODUCTIVITY -> return AppCategory.UTILITIES
            }
        }

        // 2. Intelligent heuristic category matching based on package naming
        return when {
            lowerPkg.contains("whatsapp") || lowerPkg.contains("telegram") ||
            lowerPkg.contains("messenger") || lowerPkg.contains("signal") ||
            lowerPkg.contains("dialer") || lowerPkg.contains("contacts") ||
            lowerPkg.contains("sms") || lowerPkg.contains("mms") ||
            lowerPkg.contains("messages") || lowerPkg.contains("phone") -> AppCategory.COMMUNICATION

            lowerPkg.contains("chrome") || lowerPkg.contains("firefox") ||
            lowerPkg.contains("browser") || lowerPkg.contains("opera") ||
            lowerPkg.contains("twitter") || lowerPkg.contains("instagram") ||
            lowerPkg.contains("reddit") || lowerPkg.contains("facebook") ||
            lowerPkg.contains("linkedin") -> AppCategory.INTERNET

            lowerPkg.contains("spotify") || lowerPkg.contains("youtube") ||
            lowerPkg.contains("music") || lowerPkg.contains("player") ||
            lowerPkg.contains("gallery") || lowerPkg.contains("photos") ||
            lowerPkg.contains("camera") || lowerPkg.contains("netflix") ||
            lowerPkg.contains("vlc") -> AppCategory.MEDIA

            lowerPkg.contains("game") || lowerPkg.contains("pubg") ||
            lowerPkg.contains("roblox") || lowerPkg.contains("minecraft") ||
            lowerPkg.contains("supercell") || lowerPkg.contains("unity") -> AppCategory.GAMES

            lowerPkg.contains("settings") -> AppCategory.SETTINGS

            lowerPkg.contains("calculator") || lowerPkg.contains("clock") ||
            lowerPkg.contains("calendar") || lowerPkg.contains("notes") ||
            lowerPkg.contains("file") || lowerPkg.contains("manager") ||
            lowerPkg.contains("drive") || lowerPkg.contains("tool") -> AppCategory.UTILITIES

            else -> AppCategory.ALL
        }
    }

    fun launchApp(appInfo: AppInfo) {
        val launchIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
        }
    }
}
