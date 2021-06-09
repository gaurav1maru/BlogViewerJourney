package com.gaurav.blogviewerjourney.io.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BlogDetailModel(
    var blogModel: BlogModel? = null,
    var userModel: UserModel? = null
) : Parcelable