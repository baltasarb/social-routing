package ps.g49.socialroutingclient.model.domainModel

import android.os.Parcel
import android.os.Parcelable


data class Route (
    val points: List<Point>,
    val pointsOfInterest: List<BasicPointOfInterest>,
    val isCircular: Boolean
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Point),
        parcel.createTypedArrayList(BasicPointOfInterest),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(points)
        parcel.writeTypedList(pointsOfInterest)
        parcel.writeByte(if (isCircular) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Route> {
        override fun createFromParcel(parcel: Parcel): Route {
            return Route(parcel)
        }

        override fun newArray(size: Int): Array<Route?> {
            return arrayOfNulls(size)
        }
    }
}