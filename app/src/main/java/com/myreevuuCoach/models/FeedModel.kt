package com.myreevuuCoach.models

import android.os.Parcel
import android.os.Parcelable


data class FeedModel(
        var response: List<Response>,
        var code: Int,
        var unread_notification: Int,
        var request_count: Int
) : ErrorModelJava() {

    data class Response(
            var id: Int,
            var user_id: Int,
            var user_type: Int,
            var profile_pic: String,
            var sport_id: Int,
            var sport: String,
            var privacy: Int,
            var improvement: List<OptionsModel>,
            var url: String,
            var thumbnail: String,
            var fullname: String,
            var title: String,
            var views: Int,
            var created_at: String,
            var description: String,
            var post_type: Int, //Video = 2,Image = 1
            var likes_count: Int,
            var liked: Int,
            var comments_count: Int,
            var video_width: Int,
            var video_height: Int

            ) :Parcelable {
        constructor(source: Parcel) : this(
                source.readInt(),
                source.readInt(),
                source.readInt(),
                source.readString(),
                source.readInt(),
                source.readString(),
                source.readInt(),
                source.createTypedArrayList(OptionsModel.CREATOR),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readInt(),
                source.readInt(),
                source.readInt(),
                source.readInt(),
                source.readInt(),
                source.readInt()
                )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeInt(id)
            writeInt(user_id)
            writeInt(user_type)
            writeString(profile_pic)
            writeInt(sport_id)
            writeString(sport)
            writeInt(privacy)
            writeTypedList(improvement)
            writeString(url)
            writeString(thumbnail)
            writeString(fullname)
            writeString(title)
            writeInt(views)
            writeString(created_at)
            writeString(description)
            writeInt(post_type)
            writeInt(likes_count)
            writeInt(liked)
            writeInt(comments_count)
            writeInt(video_width)
            writeInt(video_height)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Response> = object : Parcelable.Creator<Response> {
                override fun createFromParcel(source: Parcel): Response = Response(source)
                override fun newArray(size: Int): Array<Response?> = arrayOfNulls(size)
            }
        }
    }
}