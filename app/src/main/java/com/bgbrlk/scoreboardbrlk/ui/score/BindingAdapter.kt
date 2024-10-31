package com.bgbrlk.scoreboardbrlk.ui.score

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("settingIcon")
fun ImageView.setSettingIcon(@DrawableRes icon: Int){
    setImageResource(icon)
}