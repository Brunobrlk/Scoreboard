package com.bgbrlk.scoreboardbrlk.ui.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgbrlk.scoreboardbrlk.R
import com.bgbrlk.scoreboardbrlk.domain.models.Setting
import com.bgbrlk.scoreboardbrlk.domain.repository.AppSettings
import com.bgbrlk.scoreboardbrlk.helpers.Constants.HALF_MORE
import com.bgbrlk.scoreboardbrlk.helpers.Constants.POINTS_TO_WIN
import com.bgbrlk.scoreboardbrlk.helpers.Constants.RELEASE_ACCESS_TAPS
import com.bgbrlk.scoreboardbrlk.helpers.DebugUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel
    @Inject
    constructor(
        private val appSettings: AppSettings,
    ) : ViewModel() {
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

        private val pointsToWin = MutableLiveData(POINTS_TO_WIN)
        private val pointsOnTap = MutableLiveData(1)
        private var releaseAccess = 0

        val isLongGame: Boolean
            get() =
                (_counterTeam1.value ?: 0) + (_counterTeam2.value ?: 0) >=
                    (pointsToWin.value ?: POINTS_TO_WIN) * HALF_MORE

        init {
            viewModelScope.launch {
                pointsToWin.value = appSettings.getPointsToWin()
                pointsOnTap.value = appSettings.getPointsOnTap()

                _settingList =
                    arrayListOf(
                        Setting(R.string.points_to_win, R.drawable.ic_crown, pointsToWin.value ?: POINTS_TO_WIN),
                        Setting(R.string.points_on_tap, R.drawable.ic_plus, pointsOnTap.value ?: 1),
                    )

                _showAdvertisement.value = appSettings.showAds()
            }
        }

        fun addPointTeam1() {
            _counterTeam1.value = _counterTeam1.value?.plus(pointsOnTap.value!!)
            if ((_counterTeam1.value ?: 0) >= pointsToWin.value!!) {
                finishGame()
            }
        }

        fun addPointTeam2() {
            _counterTeam2.value = _counterTeam2.value?.plus(pointsOnTap.value!!)
            if ((_counterTeam2.value ?: 0) >= pointsToWin.value!!) {
                finishGame()
            }
        }

        fun showAds() = _showAdvertisement.value ?: false

        private fun finishGame() {
            triggerFinalScoreDialog()
        }

        private fun triggerFinalScoreDialog() {
            _finishingGame.value = true
        }

        fun gameFinished() {
            _finishingGame.value = false
        }

        fun decrementTeam1() {
            val decrementedValue = _counterTeam1.value?.minus(pointsOnTap.value!!) ?: 0
            _counterTeam1.value = if (decrementedValue > 0) decrementedValue else 0
        }

        fun decrementTeam2() {
            val decrementedValue = _counterTeam2.value?.minus(pointsOnTap.value!!) ?: 0
            _counterTeam2.value = if (decrementedValue > 0) decrementedValue else 0
        }

        private fun releaseAccess() {
            _showAdvertisement.value = false
            viewModelScope.launch {
                appSettings.setShowAds(false)
            }
        }

        fun restartCounters() {
            _counterTeam1.value = 0
            _counterTeam2.value = 0
        }

        fun flipCounters() {
            if (_counterTeam1.value == 0 && _counterTeam2.value == 0 && showAds()) {
                releaseAccess++
                DebugUtils.reportDebug("Access: $releaseAccess")
                if (releaseAccess == RELEASE_ACCESS_TAPS) {
                    releaseAccess()
                }
            }
            val tempCounterTeam2 = _counterTeam2.value
            _counterTeam2.value = _counterTeam1.value
            _counterTeam1.value = tempCounterTeam2
        }

        fun saveSettings(list: List<Setting>) {
            if (list.isNotEmpty() && list.size == 2) {
                val newPointsToWin = list[0].value
                val newPointsOnTap = list[1].value
                updateSettingsState(newPointsToWin, newPointsOnTap, list)
                updateSettingsDatabase(newPointsToWin, newPointsOnTap)
            }
        }

        private fun updateSettingsState(
            newPointsToWin: Int,
            newPointsOnTap: Int,
            list: List<Setting>,
        ) {
            _settingList = ArrayList(list)
            pointsToWin.value = newPointsToWin
            pointsOnTap.value = newPointsOnTap
        }

        private fun updateSettingsDatabase(
            newPointsToWin: Int,
            newPointsOnTap: Int,
        ) {
            viewModelScope.launch {
                appSettings.setPointsToWin(newPointsToWin)
                appSettings.setPointsOnTap(newPointsOnTap)
            }
        }
    }
