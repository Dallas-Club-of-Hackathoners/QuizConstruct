package com.stu.navigation.navigation

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import kotlinx.parcelize.Parcelize

@Parcelize
class NavTab(

    /**
     * Destination ID which is launched when a user taps on this tab.
     */
    @IdRes val destinationId: Int,

    /**
     * Tab name displayed under the icon.
     */
    val title: String,

    /**
     * Tab icon.
     */
    @DrawableRes val iconRes: Int

) : Parcelable
