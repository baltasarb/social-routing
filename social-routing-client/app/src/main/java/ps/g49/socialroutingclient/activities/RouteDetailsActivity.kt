package ps.g49.socialroutingclient.activities

import android.graphics.Bitmap
import android.os.Bundle
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_route_details.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.domainModel.RouteDetails
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import javax.inject.Inject

class RouteDetailsActivity : BaseActivity() {

    private lateinit var routeDetails: RouteDetails
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var googleViewModel: GoogleViewModel
    companion object {
        const val MAX_WIDTH = 750
        const val MAX_HEIGHT = 750
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        routeDetails = intent.getParcelableExtra(RouteRepresentationActivity.ROUTE_REPRESENTATION_DETAILS_MESSAGE)
        googleViewModel = getViewModel(viewModelFactory)
        initView()
    }

    override fun initView() {
        nameRouteTextView.text = routeDetails.name
        descriptionRouteTextView.text = routeDetails.description
        creationDateTextView.text = routeDetails.dateCreated
        ratingRoutetextView.text = routeDetails.rating.toString()
        val liveData = googleViewModel.getPhotoFromReference(routeDetails.imageReference, MAX_HEIGHT, MAX_WIDTH)
        handleRequestedData(liveData, ::successHandlerPhotoRequest)
    }

    private fun successHandlerPhotoRequest(bitmap: Bitmap?) {
        route_image_imageView.setImageBitmap(bitmap)
    }

}
