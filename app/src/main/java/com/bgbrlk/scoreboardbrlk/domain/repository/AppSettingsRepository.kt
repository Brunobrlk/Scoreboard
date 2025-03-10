package com.bgbrlk.scoreboardbrlk.domain.repository

import com.bgbrlk.scoreboardbrlk.data.local.LocalSettings
import com.bgbrlk.scoreboardbrlk.data.remote.AdminSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppSettingsRepository(
    private val localSettings: LocalSettings,
    private val adminSettings: AdminSettings,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AppSettings {
    override suspend fun getPointsToWin(): Int =
        withContext(ioDispatcher) {
            localSettings.getPointsToWin()
        }

    override suspend fun getPointsOnTap(): Int =
        withContext(ioDispatcher) {
            localSettings.getPointsOnTap()
        }

    override suspend fun setPointsToWin(points: Int) =
        withContext(ioDispatcher) {
            localSettings.setPointsToWin(points)
        }

    override suspend fun setPointsOnTap(points: Int) =
        withContext(ioDispatcher) {
            localSettings.setPointsOnTap(points)
        }

    override suspend fun showAds(): Boolean =
        withContext(ioDispatcher) {
            adminSettings.showAds() && localSettings.showAds()
        }

    override suspend fun setShowAds(showAds: Boolean) =
        withContext(ioDispatcher) {
            localSettings.setShowAds(showAds)
        }
}
