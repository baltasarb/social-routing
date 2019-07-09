package ps.g49.socialroutingclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.model.domainModel.PlacesOfInterest

class PlacesOfInterestAdapter(
    val placesOfInterest: List<PlacesOfInterest>,
    val onPointClickListener: OnPointClickListener
) : RecyclerView.Adapter<PlacesOfInterestAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(view: View, onPointClickListener: OnPointClickListener) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val placePhotoImageView: ImageView = view.findViewById(R.id.place_photo_imageView)
        val placeNameTextView: TextView = view.findViewById(R.id.place_name_textView)
        val placeRatingTextView: TextView = view.findViewById(R.id.place_rating_textView)
        val placeRatingRatingBar: RatingBar = view.findViewById(R.id.place_rating_ratingBar)
        val openNowTextView: TextView = view.findViewById(R.id.openNow_textView)
        val placeTypesTextView: TextView = view.findViewById(R.id.place_types_textView)
        val addPlaceButton: Button = view.findViewById(R.id.add_place_button)
        val removePlaceButton: Button = view.findViewById(R.id.remove_place_button)

        private val listener: OnPointClickListener = onPointClickListener

        init {
            view.setOnClickListener(this)
            addPlaceButton.setOnClickListener {
                addPlaceButton.visibility = View.GONE
                removePlaceButton.visibility = View.VISIBLE
                listener.onPointClick(adapterPosition, true, true)
            }
            removePlaceButton.setOnClickListener {
                addPlaceButton.visibility = View.VISIBLE
                removePlaceButton.visibility = View.GONE
                listener.onPointClick(adapterPosition, true, false)
            }
        }

        override fun onClick(view: View) {
            listener.onPointClick(adapterPosition)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val cardView: CardView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.places_of_interest_item_layout, parent, false) as CardView

        cardView.radius = 12F
        cardView.useCompatPadding = true

        return ViewHolder(cardView, onPointClickListener)
    }

    override fun getItemCount(): Int = placesOfInterest.size

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placeOfInterest = placesOfInterest.get(position)

        holder.placeNameTextView.text = placeOfInterest.name
        holder.placeTypesTextView.text = placeOfInterest.types.joinToString(", ")
        holder.addPlaceButton.visibility = View.VISIBLE
        holder.removePlaceButton.visibility = View.GONE
        holder.placeRatingRatingBar.visibility = View.GONE
        holder.placeRatingTextView.visibility = View.GONE
        holder.openNowTextView.visibility = View.GONE

        if (placeOfInterest.rating != null) {
            holder.placeRatingTextView.text = placeOfInterest.rating.toString()
            holder.placeRatingRatingBar.rating = placeOfInterest.rating.toFloat()
            holder.placeRatingTextView.visibility = View.VISIBLE
            holder.placeRatingRatingBar.visibility = View.VISIBLE
        }

        if (placeOfInterest.openingHours != null && placeOfInterest.openingHours)
            holder.openNowTextView.visibility = View.VISIBLE

        if (placeOfInterest.isSaved) {
            holder.removePlaceButton.visibility = View.VISIBLE
            holder.addPlaceButton.visibility = View.GONE
        }

        if (placeOfInterest.photo != null) {
            val photo = placeOfInterest.photo
            placeOfInterest.funcToPhoto(photo.photoReference, photo.maxHeight, photo.maxWidth) {
                holder.placePhotoImageView.setImageBitmap(it)
            }
        }

    }
}

