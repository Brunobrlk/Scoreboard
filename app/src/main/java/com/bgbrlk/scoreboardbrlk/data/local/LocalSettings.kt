package com.bgbrlk.scoreboardbrlk.data.local

interface LocalSettings {
    suspend fun getPointsToWin(): Int
    suspend fun setPointsToWin(points: Int)
    suspend fun getPointsOnTap(): Int
    suspend fun setPointsOnTap(points: Int)
    suspend fun showAds(): Boolean
    suspend fun setShowAds(showAds: Boolean)
}