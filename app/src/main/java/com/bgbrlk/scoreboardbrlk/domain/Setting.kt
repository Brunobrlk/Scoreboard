package com.bgbrlk.scoreboardbrlk.domain

import androidx.annotation.DrawableRes

data class Setting(
    val name: String,
    @DrawableRes val icon: Int,
    var value: Int,
)

