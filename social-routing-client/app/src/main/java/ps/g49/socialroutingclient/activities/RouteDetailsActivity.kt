package ps.g49.socialroutingclient.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_route_details.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.model.domainModel.RouteDetails

class RouteDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        val routeDetails = intent.getParcelableExtra<RouteDetails>(RouteRepresentationActivity.ROUTE_REPRESENTATION_DETAILS_MESSAGE)
        setView(routeDetails)
    }

    private fun setView(routeDetails: RouteDetails) {
        nameRouteTextView.text = routeDetails.name
        descriptionRouteTextView.text = routeDetails.description
        creationDateTextView.text = routeDetails.dateCreated
        ratingRoutetextView.text = routeDetails.rating.toString()
    }
}
