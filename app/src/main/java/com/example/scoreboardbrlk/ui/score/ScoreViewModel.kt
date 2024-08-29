package com.example.scoreboardbrlk.ui.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoreboardbrlk.R
import com.example.scoreboardbrlk.domain.Setting
import kotlinx.coroutines.launch

class ScoreViewModel(private val appDatastoreRepository: AppDatastoreRepository) : ViewModel() {
    private var _settingList = ArrayList<Setting>()
    val settingList: ArrayList<Setting>
        get() = _settingList

    private val _counterTeam1 = MutableLiveData(0)
    val counterTeam1: LiveData<Int>
        get() = _counterTeam1
    private var _releaseAccess = 0

    private val _counterTeam2 = MutableLiveData(0)
    val counterTeam2: LiveData<Int>
        get() = _counterTeam2

    private val _accessReleased = MutableLiveData(false)
    val accessReleased: LiveData<Boolean>
        get() = _accessReleased

    init {
        viewModelScope.launch {
            val pointsToWin = appDatastoreRepository.getString("pointsToWin")?.toInt()
            val pointsOnTap = appDatastoreRepository.getString("pointsOnTap")?.toInt()
            _settingList = arrayListOf(
                Setting("pointsToWin", R.drawable.icons8_settings, pointsToWin ?: 15),
                Setting("pointsOnTap", R.drawable.icons8_settings, pointsOnTap ?: 1))
        }
    }

    fun addPointTeam1(){
        _counterTeam1.value = _counterTeam1.value?.inc()
    }

    fun addPointTeam2(){
        _counterTeam2.value = _counterTeam2.value?.inc()
    }

    fun decrementTeam1(){
        if(_counterTeam1.value!=0){
            _counterTeam1.value = _counterTeam1.value?.dec()
        }
    }

    fun decrementTeam2(){
        if(_counterTeam2.value!=0) {
            _counterTeam2.value = _counterTeam2.value?.dec()
        }
    }

    fun releaseAccess(){
        _accessReleased.value = true
    }

    fun restartCounters() {
        _counterTeam1.value = 0
        _counterTeam2.value = 0
    }

    fun flipCounters(){
        if(_counterTeam1.value==0 && _counterTeam2.value==0){
            _releaseAccess.inc()
            if(_releaseAccess==10){
                releaseAccess()
            }
        }
        val tempCounterTeam2 = _counterTeam2.value
        _counterTeam2.value = _counterTeam1.value
        _counterTeam1.value = tempCounterTeam2
    }

    fun saveSettings(list: List<Setting>){
        viewModelScope.launch {
            appDatastoreRepository.putString("pointsToWin", list[0].counter.toString())
            appDatastoreRepository.putString("pointsOnTap", list[1].counter.toString())
        }
    }
}