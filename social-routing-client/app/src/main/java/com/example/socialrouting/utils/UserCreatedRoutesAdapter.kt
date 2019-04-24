package com.example.socialrouting.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.example.socialrouting.R
import com.example.socialrouting.SocialRoutingApplication
import com.example.socialrouting.model.inputModel.RouteInput

class UserCreatedRoutesAdapter(
    socialRoutingApplication: SocialRoutingApplication,
    val routes: List<RouteInput>): ArrayAdapter<RouteInput>(socialRoutingApplication, -1, routes){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView: View? = convertView

        if (rowView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.created_routes_list_item_layout, parent, false)
        }

        val textViewRouteName = rowView!!.findViewById<TextView>(R.id.routeTitleTextView)
        val ratingBarRoute = rowView.findViewById<RatingBar>(R.id.routeRatingBar)

        val route = routes[position]
        textViewRouteName.text = route.name
        ratingBarRoute.rating = route.rating.toFloat()

        return rowView
    }
}