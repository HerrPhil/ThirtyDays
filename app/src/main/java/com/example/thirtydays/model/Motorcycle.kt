package com.example.thirtydays.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Motorcycle(
    @StringRes val makeRes: Int,
    @StringRes val quoteRes: Int,
    @DrawableRes val imageRes: Int
)
