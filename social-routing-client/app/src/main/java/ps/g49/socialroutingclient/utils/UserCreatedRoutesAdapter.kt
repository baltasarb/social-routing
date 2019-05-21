package ps.g49.socialroutingclient.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import ps.g49.socialroutingclient.model.inputModel.RouteInput
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ps.g49.socialroutingclient.R

class UserCreatedRoutesAdapter(
    val routes: List<RouteInput>,
    val onRouteListener: OnRouteListener
)
    : RecyclerView.Adapter<UserCreatedRoutesAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(view: View, onRouteListener: OnRouteListener)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        val textView: TextView = view.findViewById(R.id.routeTitleTextView)
        val ratingBar: RatingBar = view.findViewById(R.id.routeRatingBar)

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
        // create a new view
        val constraintLayout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.created_routes_list_item_layout, parent, false)

        return ViewHolder(constraintLayout, onRouteListener)
    }

    override fun getItemCount(): Int = routes.size

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val route = routes.get(position)
        holder.textView.text = route.name
        holder.ratingBar.rating = route.rating.toFloat()
    }




}