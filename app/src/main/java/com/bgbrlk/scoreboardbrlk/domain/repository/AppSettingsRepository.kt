package com.bgbrlk.scoreboardbrlk.domain.repository

import com.bgbrlk.scoreboardbrlk.data.local.LocalSettings
import com.bgbrlk.scoreboardbrlk.data.remote.AdminSettings

class AppSettingsRepository(
    private val localSettings: LocalSettings,
    private val adminSettings: AdminSettings,
) : AppSettings {
    override suspend fun getPointsToWin(): Int = localSettings.getPointsToWin()

    override suspend fun getPointsOnTap(): Int = localSettings.getPointsOnTap()

    override suspend fun setPointsToWin(points: Int) {
        localSettings.setPointsToWin(points)
    }

    override suspend fun setPointsOnTap(points: Int) {
        localSettings.setPointsOnTap(points)
    }

    override suspend fun showAds(): Boolean = adminSettings.showAds() && localSettings.showAds()

    override suspend fun setShowAds(showAds: Boolean) {
        localSettings.setShowAds(showAds)
    }
}
