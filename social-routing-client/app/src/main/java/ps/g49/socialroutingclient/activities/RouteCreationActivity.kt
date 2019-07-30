package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_route_creation.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.adapters.listeners.OnPointClickListener
import ps.g49.socialroutingclient.adapters.PlacesOfInterestAdapter
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.domainModel.*
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.GeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.google.places.PlacesResponse
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteDetailedInput
import ps.g49.socialroutingclient.utils.GoogleMapsManager
import ps.g49.socialroutingclient.utils.ScrollListener
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import java.lang.Math.pow
import java.lang.Math.sqrt
import javax.inject.Inject

class RouteCreationActivity : BaseActivity(), OnMapReadyCallback,
    OnPointClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapManager: GoogleMapsManager

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel

    private lateinit var googleViewModel: GoogleViewModel

    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var placeResults: MutableList<PlacesOfInterest>
    private lateinit var placeResultsWaiting: MutableList<PlacesOfInterest>
    private lateinit var locationIdentifier: String
    private lateinit var nextPageTokens: MutableList<String>
    private var cityImage: Photo? = null
    private var routeUrl: String? = null
    private var isEditMode: Boolean = false
    private var termination: Boolean = false

    companion object {
        const val MAX_DISTANCE_TO_CIRCULAR = 0.0009
        const val DEFAULT_RADIUS = 100
        const val ROUTE_CREATION_MESSAGE = "ROUTE_CREATION_MESSAGE"
        const val MAX_WIDTH = 750
        const val MAX_HEIGHT = 750
        const val MAX_PLACES_OF_INTEREST_TO_SHOW = 60
        const val REQUEST_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove notification bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_route_creation)
        initView()

        routeUrl = intent.getStringExtra(ROUTE_CREATION_MESSAGE)
        if (routeUrl != null)
            isEditMode = true

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize the Route Repository.
        socialRoutingViewModel = getViewModel(viewModelFactory)
        googleViewModel = getViewModel(viewModelFactory)
        socialRoutingApplication = application as SocialRoutingApplication

        placeResults = mutableListOf()
        placeResultsWaiting = mutableListOf()
        nextPageTokens = mutableListOf()
    }

    override fun initView() {
        stopSpinner()
        routeMetadataButton.visibility = View.GONE
        finishButton.visibility = View.VISIBLE
    }

    /*
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMapManager = GoogleMapsManager(googleMap)

        mMap.setOnMapClickListener {
            if (!termination)
                mMapManager.addMarker(it)
        }

        processRoute()
    }

    override fun onBackPressed() {
        if (mMapManager.mapIsMarked()) {
            val deleteConfirmationMessage = getString(R.string.confirmation_to_delete)
            val yesMessage = getString(R.string.yes)
            val noMessage = getString(R.string.no)

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage(deleteConfirmationMessage)
            alertDialog
                .setCancelable(true)
                .setPositiveButton(yesMessage) { dialog, which -> finish() }
                .setNegativeButton(noMessage) { dialog, which -> dialog.cancel() }
            alertDialog.create()
            alertDialog.show()
        } else
            finish()
    }

    private fun processRoute() {
        if (isEditMode)
            requestRouteInformation()
        else
            getLocationFromViewInput()
    }

    private fun requestRouteInformation() {
        val liveData = socialRoutingViewModel.getRoute(routeUrl!!)
        handleRequestedData(liveData, ::successHandlerRoute)
    }

    private fun successHandlerRoute(routeDetailedInput: RouteDetailedInput?) {
        routeDetailedInput!!.points.forEach {
            mMapManager.addMarker(LatLng(it.latitude, it.longitude))
        }
        val firstPoint = routeDetailedInput.points.first()
        locationIdentifier = routeDetailedInput.location
        mMapManager.zoomInGeoCoordinatesCity(firstPoint.latitude, firstPoint.longitude)
    }

    private fun getLocationFromViewInput() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        val rowView: View = inflater.inflate(R.layout.form_location_dialog, null)
        val locationEditText = rowView.findViewById<EditText>(R.id.route_location_editText)
        val searchLocationButton = rowView.findViewById<ImageButton>(R.id.search_location_imageButton)

        val alertDialog = builder
            .setView(rowView)
            .setCancelable(false)
            .create()

        searchLocationButton.setOnClickListener {
            val locationStr = locationEditText.text.toString()
            if (locationStr.isEmpty())
                showToast("Fill the location first.")
            else {
                requestLocationGeoCoordinatesRequest(locationStr)
                alertDialog.cancel()
            }
        }

        alertDialog.show()
    }

    private fun requestNextPageOfPlacesOfInterest(pageToken: String) {
        val liveData = googleViewModel.findPlacesOfInterestNextPage(pageToken)
        handleRequestedData(liveData, ::successHandlerPointsOfInterest)
    }

    private fun successHandlerPointsOfInterest(placesResponse: PlacesResponse?) {
        placesResponse!!.nextPageToken?.let { nextPageTokens.add(it) }
        val placesOfInterest = mapPlacesOfPlaceResponseToPlacesOfInterest(placesResponse)
        if (placeResults.size + placesResponse.results.size > MAX_PLACES_OF_INTEREST_TO_SHOW) {
            placeResultsWaiting.addAll(placesOfInterest)
        }
        else {
            placeResults.addAll(placesOfInterest)
            saveCityImage()
            filterPlacesOfInterest(placeResults)
            setRecyclerView()
        }
    }

    private fun saveCityImage() {
        cityImage = placeResults.find {
            it.types.contains("locality") && it.types.contains("political")
        }!!.photo
    }

    private fun setRecyclerView() {
        val adapter = PlacesOfInterestAdapter(placeResults, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        dragView_recyclerView.layoutManager = layoutManager
        dragView_recyclerView.itemAnimator = DefaultItemAnimator()
        dragView_recyclerView.adapter = adapter
        dragView_recyclerView.addOnScrollListener(
            ScrollListener {
                if (placeResultsWaiting.isNotEmpty()) {
                    for (idx in 0 until MAX_PLACES_OF_INTEREST_TO_SHOW)
                        placeResults.add(placeResultsWaiting.removeAt(idx))
                    setRecyclerView()
                }
                else if (nextPageTokens.isNotEmpty())
                    requestNextPageOfPlacesOfInterest(nextPageTokens.removeAt(0))
            }
        )

        finishButton.visibility = View.GONE
        routeMetadataButton.visibility = View.VISIBLE
        backImageButton.visibility = View.GONE
        termination = true
    }

    private fun filterPlacesOfInterest(placesOfInterest: List<PlacesOfInterest>) {
        placeResults = placesOfInterest
            .distinctBy {
                it.placeId
            }.filter {
                !it.types.contains("locality") && !it.types.contains("political")
            }.toMutableList()
    }

    private fun mapPlacesOfPlaceResponseToPlacesOfInterest(placesResponse: PlacesResponse): List<PlacesOfInterest> {
        return placesResponse.results.map {
            var photo: Photo? = null
            val firstPhoto = it.photos?.get(0)

            if (firstPhoto != null)
                photo = Photo(firstPhoto.photoReference, MAX_HEIGHT, MAX_WIDTH)

            PlacesOfInterest(
                it.name,
                it.placeId,
                it.rating,
                it.openingHours?.openNow ?: false,
                false,
                it.types,
                Point(it.geometry.location.lat, it.geometry.location.lng),
                it.vicinity,
                photo,
                ::requestImageFromReference
            )
        }
    }

    private fun requestImageFromReference(
        photoReference: String,
        maxHeight: Int,
        maxWidth: Int,
        func: (bitmap: Bitmap?) -> Unit
    ) {
        val liveData = googleViewModel.getPhotoFromReference(photoReference, maxHeight, maxWidth)
        handleRequestedData(liveData, func)
    }

    private fun isRouteCircular(): Boolean {
        val points = mMapManager.getMarkerPoints()
        if (points.size < 2)
            return false
        val firsPoint = points.first()
        val lastPoint = points.last()
        val sumPowDistance =
            pow(firsPoint.latitude - lastPoint.latitude, 2.0) + pow(firsPoint.longitude - lastPoint.longitude, 2.0)
        val distance = sqrt(sumPowDistance)
        if (distance <= MAX_DISTANCE_TO_CIRCULAR)
            return true
        return false
    }

    override fun onPointClick(position: Int, isButtonClick: Boolean, isSaved: Boolean) {
        val placesOfInterest = placeResults[position]
        if (!isButtonClick) {
            mMapManager.addRepresentativeMarker(
                LatLng(
                    placesOfInterest.location.latitude,
                    placesOfInterest.location.longitude
                ),
                placesOfInterest.name
            )
            mMapManager.zoomInGeoCoordinates(placesOfInterest.location.latitude, placesOfInterest.location.longitude)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else
            placesOfInterest.isSaved = isSaved
    }

    fun finishRouteMetadata(view: View) {
        val pointsOfInterest = placeResults
            .filter {
                it.isSaved
            }.map {
                BasicPointOfInterest(it.placeId, it.photo, it.location.latitude, it.location.longitude)
            }

        socialRoutingApplication.routeCreated = Route(
            isEditMode,
            cityImage,
            routeUrl,
            locationIdentifier,
            mMapManager.getMarkerPoints(),
            pointsOfInterest,
            isRouteCircular()
        )

        val intent = Intent(this, RouteCreationMetadataActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun requestLocationGeoCoordinatesRequest(locationStr: String) {
        val liveData = googleViewModel.getGeoCoordinatesFromLocation(locationStr)
        handleRequestedData(liveData, ::successHandlerLocationInformation)
    }

    private fun successHandlerLocationInformation(geoCodingResponse: GeoCodingResponse?) {
        if (geoCodingResponse!!.results.isEmpty()) {
            showToast("Results Not Found.")
            getLocationFromViewInput()
            return
        }
        val firstLocation = geoCodingResponse.results.first()
        locationIdentifier = firstLocation.place_id
        val point = firstLocation.geometry.location
        mMapManager.zoomInGeoCoordinatesCity(point.lat, point.lng)
    }

    fun back(view: View) {
        mMapManager.removeLastMarkerOption()
    }

    fun finish(view: View) {
        if (!mMapManager.mapIsMarked()) {
            val markersRequiredMessage = getString(R.string.markers_required)
            showToast(markersRequiredMessage)
        } else {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            sliding_layout.panelHeight = displayMetrics.heightPixels / 3

            val markerPoints = mMapManager.getMarkerPoints()
            for (markerPoint in markerPoints) {
                val location = markerPoint.latitude.toString() + "," + markerPoint.longitude
                requestPlacesOfInterest(location)
            }
        }
    }

    private fun requestPlacesOfInterest(location: String) {
        val liveData = googleViewModel.findPlacesOfInterest(location, DEFAULT_RADIUS)
        handleRequestedData(liveData, ::successHandlerPointsOfInterest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE)
            finish()
    }
}