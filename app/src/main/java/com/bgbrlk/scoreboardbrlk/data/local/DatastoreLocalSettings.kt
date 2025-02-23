package com.bgbrlk.scoreboardbrlk.data.local

import com.bgbrlk.scoreboardbrlk.data.DatastoreHelper
import javax.inject.Inject


class DatastoreLocalSettings @Inject constructor(
    private val datastoreHelper: DatastoreHelper
) : LocalSettings {

    override suspend fun getPointsToWin(): Int {
        return datastoreHelper.getInteger(DatastoreKeys.POINTS_TO_WIN) ?: 15
    }

    override suspend fun setPointsToWin(points: Int) {
        datastoreHelper.putInteger(DatastoreKeys.POINTS_TO_WIN, points)
    }

    override suspend fun getPointsOnTap(): Int {
        return datastoreHelper.getInteger(DatastoreKeys.POINTS_ON_TAP) ?: 1
    }

    override suspend fun setPointsOnTap(points: Int) {
        datastoreHelper.putInteger(DatastoreKeys.POINTS_ON_TAP, points)
    }

    override suspend fun showAds(): Boolean {
        return datastoreHelper.getBoolean(DatastoreKeys.SHOW_ADVERTISEMENT) ?: true
    }

    override suspend fun setShowAds(showAds: Boolean) {
        datastoreHelper.putBoolean(DatastoreKeys.SHOW_ADVERTISEMENT, showAds)
    }
}
