package com.example.scoreboardbrlk.ui.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScoreViewModelFactory(private val appDatastoreRepository: AppDatastoreRepository): ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ScoreViewModel::class.java)){
            return modelClass.getConstructor(AppDatastoreRepository::class.java).newInstance(appDatastoreRepository)
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}