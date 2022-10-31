package com.alexander.rickmorty.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoResponse(
        val pages: Int,
        val next: String?
) : Parcelable
