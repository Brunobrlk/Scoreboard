package com.bgbrlk.scoreboardbrlk.ui.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgbrlk.scoreboardbrlk.R
import com.bgbrlk.scoreboardbrlk.domain.Setting
import com.bgbrlk.scoreboardbrlk.helpers.DatastoreKeys
import com.bgbrlk.scoreboardbrlk.helpers.DebugUtils
import com.bgbrlk.scoreboardbrlk.helpers.RemoteConfigKeys
import com.bgbrlk.scoreboardbrlk.repository.AppDatastoreInterface
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(private val appDatastoreRepository: AppDatastoreInterface) : ViewModel() {
    private var _finishingGame = MutableLiveData<Boolean>()
    val finishingGame: LiveData<Boolean>
        get() = _finishingGame

    private var _settingList = ArrayList<Setting>()
    val settingList: ArrayList<Setting>
        get() = _settingList

    private val _counterTeam1 = MutableLiveData(0)
    val counterTeam1: LiveData<Int>
        get() = _counterTeam1

    private val _counterTeam2 = MutableLiveData(0)
    val counterTeam2: LiveData<Int>
        get() = _counterTeam2

    private val _showAdvertisement = MutableLiveData(false)
    val showAdvertisement: LiveData<Boolean>
        get() = _showAdvertisement

    private val _pointsToWin = MutableLiveData(15)
    private val _pointsOnTap = MutableLiveData(1)
    private var _releaseAccess = 0

    val isLongGame: Boolean
        get() = (_counterTeam1.value?:0) + (_counterTeam2.value?:0) >= (_pointsToWin.value?:10)*1.5

    init {
        viewModelScope.launch {
            _pointsToWin.value = appDatastoreRepository.getInteger(DatastoreKeys.POINTS_TO_WIN) ?: 15
            _pointsOnTap.value = appDatastoreRepository.getInteger(DatastoreKeys.POINTS_ON_TAP) ?: 1

            _settingList = arrayListOf(
                Setting(R.string.points_to_win, R.drawable.ic_crown, _pointsToWin.value!!),
                Setting(R.string.points_on_tap, R.drawable.ic_plus, _pointsOnTap.value!!),
            )

            val remoteConfShowAdvertisement = isAdsEnabled()
            DebugUtils.reportDebug("Remote Config: $remoteConfShowAdvertisement")
            _showAdvertisement.value = remoteConfShowAdvertisement && appDatastoreRepository.getBoolean(DatastoreKeys.SHOW_ADVERTISEMENT) ?: true
        }
    }

    private suspend fun isAdsEnabled(): Boolean {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = RemoteConfigKeys.FETCH_INTERVAL_PROD
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

    fun addPointTeam1() {
        _counterTeam1.value = _counterTeam1.value?.plus(_pointsOnTap.value!!)
        if ((_counterTeam1.value ?: 0) >= _pointsToWin.value!!) {
            finishGame()
        }
    }

    fun addPointTeam2() {
        _counterTeam2.value = _counterTeam2.value?.plus(_pointsOnTap.value!!)
        if ((_counterTeam2.value ?: 0) >= _pointsToWin.value!!) {
            finishGame()
        }
    }

    fun showAds() = _showAdvertisement.value?:false

    private fun finishGame() {
        triggerFinalScoreDialog()
    }

    private fun triggerFinalScoreDialog() {
        _finishingGame.value = true
    }

    fun gameFinished(){
        _finishingGame.value = false
    }

    fun decrementTeam1() {
        val decrementedValue = _counterTeam1.value?.minus(_pointsOnTap.value!!) ?: 0
        _counterTeam1.value = if (decrementedValue > 0) decrementedValue else 0
    }

    fun decrementTeam2() {
        val decrementedValue = _counterTeam2.value?.minus(_pointsOnTap.value!!) ?: 0
        _counterTeam2.value = if (decrementedValue > 0) decrementedValue else 0
    }

    private fun releaseAccess() {
        _showAdvertisement.value = false
        DebugUtils.reportDebug("Access Released")
        viewModelScope.launch {
            appDatastoreRepository.putBoolean(DatastoreKeys.SHOW_ADVERTISEMENT, showAds())
        }
    }

    fun restartCounters() {
        _counterTeam1.value = 0
        _counterTeam2.value = 0
    }

    fun flipCounters() {
        if (_counterTeam1.value == 0 && _counterTeam2.value == 0 && showAds()) {
            _releaseAccess++
            DebugUtils.reportDebug("Access: $_releaseAccess")
            if (_releaseAccess == 10) {
                releaseAccess()
            }
        }
        val tempCounterTeam2 = _counterTeam2.value
        _counterTeam2.value = _counterTeam1.value
        _counterTeam1.value = tempCounterTeam2
    }

    fun saveSettings(list: List<Setting>) {
        updateSettingValues(list)
        viewModelScope.launch {
            appDatastoreRepository.putInteger(DatastoreKeys.POINTS_TO_WIN, _pointsToWin.value!!)
            appDatastoreRepository.putInteger(DatastoreKeys.POINTS_ON_TAP, _pointsOnTap.value!!)
        }
    }

    private fun updateSettingValues(list: List<Setting>) {
        _settingList = ArrayList(list)
        _pointsToWin.value = list[0].value
        _pointsOnTap.value = list[1].value
    }
}