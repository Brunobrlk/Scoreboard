package com.bgbrlk.scoreboardbrlk.repository

import com.bgbrlk.scoreboardbrlk.domain.repository.AppSettings

object DummyAppDatastoreRepository : AppSettings {
    private var pointsToWin = 15
    private var pointsOnTap = 1
    private var showAds = false

    override suspend fun getPointsToWin() = pointsToWin

    override suspend fun getPointsOnTap() = pointsOnTap

    override suspend fun setPointsToWin(points: Int) {
        pointsToWin = points
    }

    override suspend fun setPointsOnTap(points: Int) {
        pointsOnTap = points
    }

    override suspend fun showAds() = showAds

    override suspend fun setShowAds(showAds: Boolean) {
        this.showAds = showAds
    }
}
