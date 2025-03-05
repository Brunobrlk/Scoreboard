package com.bgbrlk.scoreboardbrlk.domain.repository

interface AppSettings {
    suspend fun getPointsToWin(): Int

    suspend fun setPointsToWin(points: Int)

    suspend fun getPointsOnTap(): Int

    suspend fun setPointsOnTap(points: Int)

    suspend fun showAds(): Boolean

    suspend fun setShowAds(showAds: Boolean)
}
