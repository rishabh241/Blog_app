package com.example.blog_app.Model

import android.os.Parcel
import android.os.Parcelable

data class Blog_item_model(
    val userId: String?="null",
    val heading: String?="null",
    val userName: String?="null",
    val date: String?="null",
    val desc: String?="null",
    val likeCount: Int=0,
    val imageUrl: String?="null",
    val postId:String="null",
    var isSaved:Boolean = false
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"null",
        parcel.readString()?:"null",
        parcel.readString()?:"null",
        parcel.readString()?:"null",
        parcel.readString()?:"null",
        parcel.readInt(),
        parcel.readString()?:"null",
        parcel.readString()?:"null",
        parcel.readByte()!=0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(heading)
        parcel.writeString(userName)
        parcel.writeString(date)
        parcel.writeString(desc)
        parcel.writeInt(likeCount)
        parcel.writeString(imageUrl)
        parcel.writeString(postId)
        parcel.writeString(isSaved.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Blog_item_model> {
        override fun createFromParcel(parcel: Parcel): Blog_item_model {
            return Blog_item_model(parcel)
        }

        override fun newArray(size: Int): Array<Blog_item_model?> {
            return arrayOfNulls(size)
        }
    }

}
