package com.bgbrlk.scoreboardbrlk.data.remote

import com.bgbrlk.scoreboardbrlk.helpers.DebugUtils
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.tasks.await

class FirebaseAdminSettings: AdminSettings {
    override suspend fun showAds(): Boolean {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = FirebaseKeys.FETCH_INTERVAL_PROD
        }
        val remoteConfig = Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
        }

        try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            DebugUtils.reportDebug("Unexpected Remote Config Error: ${e.localizedMessage}")
        }

        return remoteConfig.getBoolean("showAdvertisement")
    }
}