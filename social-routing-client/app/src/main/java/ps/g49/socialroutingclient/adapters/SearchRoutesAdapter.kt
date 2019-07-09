package ps.g49.socialroutingclient.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.activities.RouteCreationActivity
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteInput

class SearchRoutesAdapter(
    val context: Context,
    val routes: List<RouteInput>,
    val onRouteListener: OnRouteListener,
    val isEditable: Boolean
) : RecyclerView.Adapter<SearchRoutesAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(view: View, onRouteListener: OnRouteListener) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener
    {

        val principalImage: ImageView = view.findViewById(R.id.principal_imageView)
        val nameTextView: TextView = view.findViewById(R.id.name_textView)
        val ratingTextView: TextView = view.findViewById(R.id.rating_textView)
        val editImageView: ImageView = view.findViewById(R.id.editImageView)

        private val listener: OnRouteListener = onRouteListener

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            listener.onRouteClick(adapterPosition)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val cardView: CardView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_view_item_layout, parent, false) as CardView

        cardView.radius = 12F
        cardView.useCompatPadding = true

        return ViewHolder(cardView, onRouteListener)
    }

    override fun getItemCount(): Int = routes.size

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val route = routes.get(position)
        holder.nameTextView.text = route.name
        holder.ratingTextView.text = route.rating.toString()
        if (isEditable) {
            holder.editImageView.visibility = View.VISIBLE
            holder.editImageView.setOnClickListener {
                val intent = Intent(context, RouteCreationActivity::class.java)
                //intent.putExtra(RouteCreationActivity.ROUTE_CREATION_MESSAGE, route.routeUrl)
                context.startActivity(intent)
            }
        }
        //holder.principalImage.setImageBitmap()
    }

}