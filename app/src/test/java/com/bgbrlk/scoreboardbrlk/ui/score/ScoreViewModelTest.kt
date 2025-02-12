package com.bgbrlk.scoreboardbrlk.ui.score

import com.bgbrlk.scoreboardbrlk.repository.DummyAppDatastoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScoreViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun addPointsTeam1_incrementsCounterTo10_whenCalled10Times() = runTest {
        val viewModel = ScoreViewModel(DummyAppDatastoreRepository)
        repeat(10){
            viewModel.addPointTeam1()
        }
        assertEquals(10, viewModel.counterTeam1.value)
    }

    @Test
    fun addPointsTeam2_incrementsCounterTo10_whenCalled10Times() = runTest {
        val viewModel = ScoreViewModel(DummyAppDatastoreRepository)
        repeat(10){
            viewModel.addPointTeam2()
        }
        assertEquals(10, viewModel.counterTeam2.value)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
}