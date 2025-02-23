package com.bgbrlk.scoreboardbrlk.repository

import com.bgbrlk.scoreboardbrlk.domain.repository.AppSettings

object DummyAppDatastoreRepository : AppSettings {
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