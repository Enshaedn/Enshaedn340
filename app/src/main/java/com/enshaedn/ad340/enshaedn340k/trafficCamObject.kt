package com.enshaedn.ad340.enshaedn340k

import android.os.Parcel
import android.os.Parcelable

//class to create movie objects based on the JSON object passed through
class trafficCam(coords: String, id: String, desc: String, image: String, type: String): Parcelable {
    val coords = coords
    val id = id
    val desc = desc
    var image = image
    val type = type

    constructor(parcel: Parcel): this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(coords)
        parcel.writeString(id)
        parcel.writeString(desc)
        parcel.writeString(image)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<trafficCam> {
        override fun createFromParcel(parcel: Parcel): trafficCam {
            return trafficCam(parcel)
        }

        override fun newArray(size: Int): Array<trafficCam?> {
            return arrayOfNulls(size)
        }
    }
}