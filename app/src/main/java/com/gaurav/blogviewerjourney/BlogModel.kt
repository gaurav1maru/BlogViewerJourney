package com.gaurav.blogviewerjourney

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BlogModel(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
) : Parcelable