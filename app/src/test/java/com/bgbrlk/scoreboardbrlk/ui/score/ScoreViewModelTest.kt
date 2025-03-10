package com.bgbrlk.scoreboardbrlk.ui.score

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bgbrlk.scoreboardbrlk.repository.DummyAppDatastoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScoreViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun addPointsTeam1_incrementsCounterTo10_whenCalled10Times() =
        runTest {
            val viewModel = ScoreViewModel(DummyAppDatastoreRepository)
            repeat(10) {
                viewModel.addPointTeam1()
            }
            assertEquals(10, viewModel.counterTeam1.value)
        }

    @Test
    fun addPointsTeam2_incrementsCounterTo10_whenCalled10Times() =
        runTest {
            val viewModel = ScoreViewModel(DummyAppDatastoreRepository)
            repeat(10) {
                viewModel.addPointTeam2()
            }
            assertEquals(10, viewModel.counterTeam2.value)
        }
}
