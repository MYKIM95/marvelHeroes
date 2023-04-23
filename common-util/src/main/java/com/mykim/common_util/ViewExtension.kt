package com.mykim.common_util

import android.view.View


fun View.visible(visible: Boolean = true) {
    visibility = if(visible) View.VISIBLE else View.GONE
}