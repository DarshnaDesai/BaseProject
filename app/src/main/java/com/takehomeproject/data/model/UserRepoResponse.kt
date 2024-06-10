package com.takehomeproject.data.model

import android.os.Parcel
import android.os.Parcelable

data class UserRepoResponse(
    val name: String? = null,
    val description: String? = null,
    val updated_at: String? = null,
    val stargazers_count: Int,
    val forks_count: Int,
    val open_issues_count: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(updated_at)
        parcel.writeInt(stargazers_count)
        parcel.writeInt(forks_count)
        parcel.writeInt(open_issues_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserRepoResponse> {
        override fun createFromParcel(parcel: Parcel): UserRepoResponse {
            return UserRepoResponse(parcel)
        }

        override fun newArray(size: Int): Array<UserRepoResponse?> {
            return arrayOfNulls(size)
        }
    }
}