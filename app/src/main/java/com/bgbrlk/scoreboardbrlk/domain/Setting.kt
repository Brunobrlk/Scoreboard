package com.bgbrlk.scoreboardbrlk.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Setting(
    @StringRes val name: Int,
    @DrawableRes val icon: Int,
    var value: Int,
)

