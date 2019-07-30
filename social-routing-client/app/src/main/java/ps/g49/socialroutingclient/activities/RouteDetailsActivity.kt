package ps.g49.socialroutingclient.activities

import android.os.Bundle
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_route_details.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.model.domainModel.RouteDetails

class RouteDetailsActivity : BaseActivity() {

    private lateinit var routeDetails: RouteDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        routeDetails = intent.getParcelableExtra(RouteRepresentationActivity.ROUTE_REPRESENTATION_DETAILS_MESSAGE)
        initView()
    }

    override fun initView() {
        nameRouteTextView.text = routeDetails.name
        descriptionRouteTextView.text = routeDetails.description
        creationDateTextView.text = routeDetails.dateCreated
        ratingRoutetextView.text = routeDetails.rating.toString()
    }
}
