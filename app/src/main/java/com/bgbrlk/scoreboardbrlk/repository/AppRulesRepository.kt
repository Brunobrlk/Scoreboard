package com.bgbrlk.scoreboardbrlk.repository

import com.bgbrlk.scoreboardbrlk.helpers.DatastoreKeys

class AppRulesRepository(private val appDatastore: AppDatastoreInterface, private val remoteConfig: AppRemoteConfigInterface) : AppRules {
    override suspend fun getPointsToWin(): Int {
        return appDatastore.getInteger(DatastoreKeys.POINTS_TO_WIN) ?: 15
    }

    override suspend fun getPointsOnTap(): Int {
        return appDatastore.getInteger(DatastoreKeys.POINTS_ON_TAP) ?: 1
    }

    override suspend fun setPointsToWin(points: Int) {
        appDatastore.putInteger(DatastoreKeys.POINTS_TO_WIN, points)
    }

    override suspend fun setPointsOnTap(points: Int) {
        appDatastore.putInteger(DatastoreKeys.POINTS_ON_TAP, points)
    }

    override suspend fun showAds(): Boolean {
        return remoteConfig.showAds() && appDatastore.getBoolean(DatastoreKeys.SHOW_ADVERTISEMENT) ?: true
    }

    override suspend fun setShowAds(showAds: Boolean) {
        appDatastore.putBoolean(DatastoreKeys.SHOW_ADVERTISEMENT, showAds)
    }
}