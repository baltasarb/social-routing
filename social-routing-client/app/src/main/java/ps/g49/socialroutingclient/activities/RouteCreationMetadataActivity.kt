package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_route_creation_metadata.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.adapters.ImageToRouteAdapter
import ps.g49.socialroutingclient.adapters.listeners.OnImageClickListener
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.domainModel.Category
import ps.g49.socialroutingclient.model.domainModel.ImageReferenceToAdapter
import ps.g49.socialroutingclient.model.domainModel.Route
import ps.g49.socialroutingclient.model.inputModel.socialRouting.CategoryCollectionInput
import ps.g49.socialroutingclient.model.outputModel.PointOfInterestOutput
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject



class RouteCreationMetadataActivity : BaseActivity(),
    OnImageClickListener {

    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var googleViewModel: GoogleViewModel
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var route: Route
    private lateinit var imageList: MutableList<ImageReferenceToAdapter>
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var imageReferenceClicked: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_creation_metadata)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        socialRoutingApplication = application as SocialRoutingApplication
        socialRoutingViewModel = getViewModel(viewModelFactory)
        googleViewModel = getViewModel(viewModelFactory)

        route = socialRoutingApplication.routeCreated
        initView()

        requestCategories()
        setPlacesOfInterestImages()
    }

    override fun initView() {
        noCategoriesFoundTextView.visibility = View.GONE
        noImagesTextView.visibility = View.GONE
        circularCheckBox.isChecked = route.isCircular
        if (route.isCircular)
            orderedCheckBox.isClickable = false
        else circularCheckBox.visibility = View.GONE
    }

    private fun setPlacesOfInterestImages() {
        if (route.cityPhoto != null)
            imageReferenceClicked = route.cityPhoto!!.photoReference

        imageList = route.pointsOfInterest.filter {
            it.photo != null
        }.map {
            ImageReferenceToAdapter(
                it.photo!!,
                ::requestImageFromReference
            )
        }.toMutableList()

        val adapter = ImageToRouteAdapter(imageList, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        pointsOfInterestRecyclerView.layoutManager = layoutManager
        pointsOfInterestRecyclerView.itemAnimator = DefaultItemAnimator()
        pointsOfInterestRecyclerView.adapter = adapter

        if (imageList.isEmpty())
            noImagesTextView.visibility = View.VISIBLE
    }

    private fun requestCategories() {
        val categoriesUrl = socialRoutingApplication.getSocialRoutingRootResource().categoriesUrl
        val liveData = socialRoutingViewModel.getRouteCategories(categoriesUrl)
        handleRequestedData(liveData, ::successHandlerCategories, ::errorHandlerCategories)
    }

    private fun errorHandlerCategories() {
        noCategoriesFoundTextView.visibility = View.VISIBLE
    }

    private fun successHandlerCategories(categoriesCollection: CategoryCollectionInput?) {
        val categories = categoriesCollection!!.categories
        categories.forEach {
            val checkBox = CheckBox(this)
            checkBox.text = it.name
            categoriesLinearLayout.addView(checkBox)
        }
    }

    private fun requestImageFromReference(
        photoReference: String,
        maxHeight: Int,
        maxWidth: Int,
        successHandler: (bitmap: Bitmap?) -> Unit
    ) {
        val liveData = googleViewModel.getPhotoFromReference(photoReference, maxHeight, maxWidth)
        handleRequestedData(liveData, successHandler)
    }

    fun createRoute(view: View) {
        val routeName = nameEditText.text.toString()
        val routeDescription = descriptionEditText.text.toString()
        val categories = mutableListOf<String>()
        val isRouteOrdered = orderedCheckBox.isChecked
        for (idx in 0 until categoriesLinearLayout.childCount) {
            val checkBox = categoriesLinearLayout.getChildAt(idx) as CheckBox
            if (checkBox.isChecked)
                categories.add(checkBox.text.toString())
        }
        if (routeName.isEmpty() || routeDescription.isEmpty() || categories.isEmpty()) {
            showToast("Fill the form first.")
            return
        }

        val duration = when {
            shortRadioButton.isChecked -> "short"
            mediumRadioButton.isChecked -> "medium"
            else -> "long"
        }

        val routeOutput = RouteOutput(
            route.placeId,
            routeName,
            routeDescription,
            route.points,
            categories.map { Category(it) },
            route.isCircular,
            isRouteOrdered,
            route.pointsOfInterest.map { PointOfInterestOutput(it.identifier, it.latitude, it.longitude) },
            imageReferenceClicked.orEmpty(),
            duration
        )

        if (!route.existingRoute)
            requestRouteCreation(routeOutput)
        else
            requestRouteUpdate(routeOutput)

    }

    private fun requestRouteCreation(routeOutput: RouteOutput) {
        val liveData = socialRoutingViewModel.createRoute(routeOutput)
        handleRequestedData(liveData, ::successHandlerRouteCreation, ::errorHandlerRouteCreation)
    }

    private fun requestRouteUpdate(routeOutput: RouteOutput) {
        val liveData = socialRoutingViewModel.updateRoute(route.routeUrl!!, routeOutput)
        handleRequestedData(liveData, ::successHandlerRouteUpdate, ::errorHandlerRouteUpdate)
    }

    private fun errorHandlerRouteCreation() {
        showToast("Could not create the route.")
    }

    private fun errorHandlerRouteUpdate() {
        showToast("Could not update the route.")
    }

    private fun successHandlerRouteCreation(routeUrl: String?) {
        showToast("Route Created!")
        val correctUrl = socialRoutingApplication.setCorrectUrlToDevice(routeUrl!!)
        val intent = Intent(this, RouteRepresentationActivity::class.java)
        intent.putExtra(getString(R.string.route_creation_intent_message), correctUrl)
        setResult(RouteCreationActivity.REQUEST_CODE)
        startActivity(intent)
        finish()
    }

    private fun successHandlerRouteUpdate(unit: Unit?) {
        showToast("Route Updated")
        val correctUrl = socialRoutingApplication.setCorrectUrlToDevice(route.routeUrl!!)
        val intent = Intent(this, RouteRepresentationActivity::class.java)
        intent.putExtra(getString(R.string.route_creation_intent_message), correctUrl)
        setResult(RouteCreationActivity.REQUEST_CODE)
        startActivity(intent)
        finish()
    }

    override fun onClick(position: Int) {
        imageReferenceClicked = imageList.get(position).photo.photoReference
    }

}
