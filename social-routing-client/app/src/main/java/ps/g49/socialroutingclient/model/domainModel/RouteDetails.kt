package ps.g49.socialroutingclient.model.domainModel

import android.os.Parcel
import android.os.Parcelable

class RouteDetails(
    val categories: List<Category>,
    val dateCreated: String,
    val name: String,
    val description: String,
    val imageReference: String,
    val rating: Double
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Category),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(categories)
        parcel.writeString(dateCreated)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(imageReference)
        parcel.writeDouble(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RouteDetails> {
        override fun createFromParcel(parcel: Parcel): RouteDetails {
            return RouteDetails(parcel)
        }

        override fun newArray(size: Int): Array<RouteDetails?> {
            return arrayOfNulls(size)
        }
    }
}