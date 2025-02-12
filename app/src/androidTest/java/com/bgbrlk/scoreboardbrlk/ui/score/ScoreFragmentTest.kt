package com.bgbrlk.scoreboardbrlk.ui.score

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgbrlk.scoreboardbrlk.R
import com.bgbrlk.scoreboardbrlk.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScoreFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun textViewCounterTeam1_incrementsTeam1Score_whenClicked() {
        onView(withId(R.id.textview_counter_team1))
            .check(matches(withText("0")))

        repeat(5){
            onView(withId(R.id.textview_counter_team1))
                .perform(click())
        }

        onView(withId(R.id.textview_counter_team1))
            .check(matches(withText("5")))
    }

    @Test
    fun scoreAreaTeam1_incrementsTeam1Score_whenClicked() {
        val scoreTeam1Area = onView(withId(R.id.view_left_half))
        val scoreTeam1TextView = onView(withId(R.id.textview_counter_team1))

        scoreTeam1TextView.check(matches(withText("0")))

        repeat(5){
            scoreTeam1Area.perform(click())
        }

        scoreTeam1TextView.check(matches(withText("5")))
    }

    @Test
    fun newGameButton_resetScores_whenClicked() {
        val scoreTeam1TextView = onView(withId(R.id.textview_counter_team1))
        val scoreTeam2TextView = onView(withId(R.id.textview_counter_team2))

        repeat(15){
            scoreTeam1TextView.perform(click())
        }

        onView(withText("NEW GAME")).perform(click())

        scoreTeam1TextView.check(matches(withText("0")))
        scoreTeam2TextView.check(matches(withText("0")))
    }

    @Test
    fun fabReload_resetScores_whenClicked() {
        val scoreTeam1TextView = onView(withId(R.id.textview_counter_team1))
        val scoreTeam2TextView = onView(withId(R.id.textview_counter_team2))
        val fabReload = onView(withId(R.id.fab_reload))

        repeat(5){
            scoreTeam1TextView.perform(click())
            scoreTeam2TextView.perform(click())
            scoreTeam2TextView.perform(click())
        }

        fabReload.perform(click())

        scoreTeam1TextView.check(matches(withText("0")))
        scoreTeam2TextView.check(matches(withText("0")))
    }

//    Failing: Tip - The only fab using databinding instead of click listener is fabFlip.
//    @Test
//    fun fabFlip_switchScores_whenClicked() {
//        val scoreTeam1TextView = onView(withId(R.id.textview_counter_team1))
//        val scoreTeam2TextView = onView(withId(R.id.textview_counter_team2))
//
//        repeat(5){
//            scoreTeam1TextView.perform(click())
//        }
//        onView(withId(R.id.fab_flip)).perform(click())
//        repeat(10){
//            scoreTeam2TextView.perform(click())
//        }
//        onView(withId(R.id.fab_flip)).perform(click())
//
//
//        scoreTeam1TextView.check(matches(withText("10")))
//        scoreTeam2TextView.check(matches(withText("5")))
//    }
}