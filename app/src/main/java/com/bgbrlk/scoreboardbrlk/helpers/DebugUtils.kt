package com.bgbrlk.scoreboardbrlk.helpers

import android.util.Log

object DebugUtils {
    private const val DEBUG_TAG = "DEBUG_BRLK"

    fun reportDebug(msg: String) {
        Log.d(DEBUG_TAG, msg)
    }
}
