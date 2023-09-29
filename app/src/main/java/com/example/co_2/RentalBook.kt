package com.example.co_2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RentalBook(
    val name: String,
    val rating: Float,
    val genre: String,
    val pricePerDay: Int,
    val imageResId: Int
) : Parcelable
