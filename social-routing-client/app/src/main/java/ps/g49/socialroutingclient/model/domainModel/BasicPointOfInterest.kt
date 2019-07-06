package ps.g49.socialroutingclient.model.domainModel

import android.os.Parcel
import android.os.Parcelable

data class BasicPointOfInterest (
    val photoReference: String?,
    val latitude: Double,
    val longitude: Double
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(photoReference)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BasicPointOfInterest> {
        override fun createFromParcel(parcel: Parcel): BasicPointOfInterest {
            return BasicPointOfInterest(parcel)
        }

        override fun newArray(size: Int): Array<BasicPointOfInterest?> {
            return arrayOfNulls(size)
        }
    }
}