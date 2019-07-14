package ps.g49.socialroutingclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.adapters.listeners.OnImageClickListener
import ps.g49.socialroutingclient.model.domainModel.ImageReferenceToAdapter

class ImageToRouteAdapter(
    private val imageReferenceToAdapter: List<ImageReferenceToAdapter>,
    private val onImageClickListener: OnImageClickListener
): RecyclerView.Adapter<ImageToRouteAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(view: View, onImageClickListener: OnImageClickListener) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener
    {

        val placePhotoImageView: ImageView = view.findViewById(R.id.imageToShowFromPlace)
        val checkBox: CheckBox = view.findViewById(R.id.imageSelectionCheckBox)

        private val listener: OnImageClickListener = onImageClickListener

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            listener.onClick(adapterPosition)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val cardView: CardView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.choose_image_for_route_item_layout, parent, false) as CardView

        cardView.radius = 12F
        cardView.useCompatPadding = true

        return ViewHolder(cardView, onImageClickListener)
    }

    override fun getItemCount(): Int = imageReferenceToAdapter.size

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoReference = imageReferenceToAdapter.get(position)
        val photo = photoReference.photo
        photoReference.funcToPhoto(photo.photoReference, photo.maxHeight, photo.maxWidth) {
            holder.placePhotoImageView.setImageBitmap(it)
        }
    }
}

