package com.bgbrlk.scoreboardbrlk.ui.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgbrlk.scoreboardbrlk.R
import com.bgbrlk.scoreboardbrlk.domain.Setting
import com.bgbrlk.scoreboardbrlk.helpers.DatastoreKeys
import com.bgbrlk.scoreboardbrlk.helpers.DebugUtils
import com.bgbrlk.scoreboardbrlk.repository.AppDatastoreInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private val _pointsToWin = MutableLiveData(15)

    private val _pointsOnTap = MutableLiveData(1)
    private var _releaseAccess = 0
    var showAdvertisement = true
    val isLongGame: Boolean
        get() = (_counterTeam1.value?:0) + (_counterTeam2.value?:0) >= (_pointsToWin.value?:10)*1.5

    init {
        viewModelScope.launch {
            _pointsToWin.value = appDatastoreRepository.getInteger(DatastoreKeys.POINTS_TO_WIN) ?: 15
            _pointsOnTap.value = appDatastoreRepository.getInteger(DatastoreKeys.POINTS_ON_TAP) ?: 1
            showAdvertisement = appDatastoreRepository.getBoolean(DatastoreKeys.SHOW_ADVERTISEMENT) ?: true

            _settingList = arrayListOf(
                Setting("Points to Win", R.drawable.ic_crown, _pointsToWin.value!!),
                Setting("Points on Tap", R.drawable.ic_plus, _pointsOnTap.value!!),
            )
        }
    }

    fun addPointTeam1() {
        DebugUtils.reportDebug("Adding point to team 1. IsLongGame: $isLongGame")
        _counterTeam1.value = _counterTeam1.value?.plus(_pointsOnTap.value!!)
        if ((_counterTeam1.value ?: 0) >= _pointsToWin.value!!) {
            finishGame()
        }
    }

    fun addPointTeam2() {
        DebugUtils.reportDebug("Adding point to team 2. IsLongGame: $isLongGame")
        _counterTeam2.value = _counterTeam2.value?.plus(_pointsOnTap.value!!)
        if ((_counterTeam2.value ?: 0) >= _pointsToWin.value!!) {
            finishGame()
        }
    }

    private fun finishGame() {
        triggerFinalScoreDialog()
    }

    private fun triggerFinalScoreDialog() {
        _finishingGame.value = true
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
        showAdvertisement = false
        DebugUtils.reportDebug("Access Released")
        viewModelScope.launch {
            appDatastoreRepository.putBoolean(DatastoreKeys.SHOW_ADVERTISEMENT, showAdvertisement)
        }
    }

    fun restartCounters() {
        _counterTeam1.value = 0
        _counterTeam2.value = 0
    }

    fun flipCounters() {
        if (_counterTeam1.value == 0 && _counterTeam2.value == 0 && showAdvertisement) {
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