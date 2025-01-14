package com.bgbrlk.scoreboardbrlk.helpers

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

@BindingAdapter("settingIcon")
fun ImageView.setSettingIcon(@DrawableRes icon: Int){
    setImageResource(icon)
}

@BindingAdapter("settingValue")
fun TextView.setSettingValue(@StringRes value: Int){
    text = this.context.getString(value)
}
