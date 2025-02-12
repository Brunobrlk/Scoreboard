package com.bgbrlk.scoreboardbrlk.repository

object DummyAppDatastoreRepository : AppRules {
    override suspend fun getPointsToWin() = 15

    override suspend fun getPointsOnTap() = 1

    override suspend fun setPointsToWin(points: Int) {
    }

    override suspend fun setPointsOnTap(points: Int) {
    }

    override suspend fun showAds() = false

    override suspend fun setShowAds(showAds: Boolean) {
    }
}