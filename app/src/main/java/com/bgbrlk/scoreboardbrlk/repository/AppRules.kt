package com.bgbrlk.scoreboardbrlk.repository

interface AppRules {
    suspend fun getPointsToWin(): Int
    suspend fun getPointsOnTap(): Int
    suspend fun setPointsToWin(points: Int)
    suspend fun setPointsOnTap(points: Int)
    suspend fun showAds(): Boolean
    suspend fun setShowAds(showAds: Boolean)
}