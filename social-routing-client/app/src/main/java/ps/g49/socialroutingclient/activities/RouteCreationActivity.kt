package ps.g49.socialroutingclient.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_route_creation.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.adapters.OnPointClickListener
import ps.g49.socialroutingclient.adapters.PlacesOfInterestAdapter
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.domainModel.*
import ps.g49.socialroutingclient.model.inputModel.google.places.PlacesResponse
import ps.g49.socialroutingclient.utils.GoogleMapsManager
import ps.g49.socialroutingclient.utils.ImageDownloader
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import java.lang.Math.pow
import java.lang.Math.sqrt
import java.net.URL
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

class RouteCreationActivity : BaseActivity(), OnMapReadyCallback, OnPointClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var mMapManager: GoogleMapsManager
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var googleViewModel: GoogleViewModel
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var placeResults: MutableList<PlacesOfInterest>
    private lateinit var savePlaces: MutableList<PlacesOfInterest>
    private var termination: Boolean = false
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        const val MAX_DISTANCE_TO_CIRCULAR = 0.003
        const val DEFAULT_RADIUS = 100
        const val ROUTE_INFO_MESSAGE = "ROUTE_INFO_MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove notification bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_route_creation)
        socialRoutingApplication = application as SocialRoutingApplication
        stopSpinner()
        sliding_layout.isEnabled = false
        routeMetadataButton.visibility = View.GONE
        finishButton.visibility = View.VISIBLE

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Initialize the Route Repository.
        socialRoutingViewModel = getViewModel(viewModelFactory)
        googleViewModel = getViewModel(viewModelFactory)

        savePlaces = mutableListOf()
        placeResults = mutableListOf()
    }

    /*
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMapManager = GoogleMapsManager(googleMap, googleViewModel, ::handleRequestedData)

        mMap.setOnMapClickListener {
            if (!termination)
                mMapManager.addMarker(it)
        }
        getLocationFromViewInput()
    }

    private fun requestPlacesOfInterest(location: String) {
        val liveData = googleViewModel.findPlacesOfInterest(location, DEFAULT_RADIUS)
        handleRequestedData(liveData, ::requestSuccessHandlerPlacesOfInterest)
    }

    private fun requestNextPageOfPlacesOfInterest(pageToken: String) {
        val liveData = googleViewModel.findPlacesOfInterestNextPage(pageToken)
        handleRequestedData(liveData, ::requestSuccessHandlerPlacesOfInterest)
    }

    private fun requestImageFromReference(photoReference: String, maxHeight: Int, maxWidth: Int, func: (bitmap: Bitmap?) -> Unit ) {
        val liveData = googleViewModel.getPhotoFromReference(photoReference, maxHeight, maxWidth)
        handleRequestedData(liveData, func)
    }

    private fun requestSuccessHandlerPlacesOfInterest(placesResponse: PlacesResponse?) {
        if (placesResponse!!.nextPageToken != null)
            requestNextPageOfPlacesOfInterest(placesResponse.nextPageToken!!)

        val results = placesResponse.results.map {
            var photo: Photo? = null
            val firstPhoto = it.photos?.get(0)
            if (firstPhoto != null)
                photo = Photo(firstPhoto.photoReference, 500, 1000)

            PlacesOfInterest(
                it.name,
                it.placeId,
                it.rating,
                it.openingHours?.openNow?:false,
                false,
                it.types,
                Point(it.geometry.location.lat, it.geometry.location.lng),
                it.vicinity,
                photo,
                ::requestImageFromReference
            )
        }

        placeResults.addAll(results)
        val adapter = PlacesOfInterestAdapter(placeResults, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        dragView_recyclerView.layoutManager = layoutManager
        dragView_recyclerView.itemAnimator = DefaultItemAnimator()
        dragView_recyclerView.adapter = adapter

        finishButton.visibility = View.GONE
        routeMetadataButton.visibility = View.VISIBLE
        backImageButton.visibility = View.GONE
        termination = true
    }

    private fun isRouteCircular(): Boolean {
        val points = mMapManager.getMarkerPoints()
        if (points.size < 2)
            return false
        val firsPoint = points.first()
        val lastPoint = points.last()
        val sumPowDistance = pow(firsPoint.latitude - lastPoint.latitude, 2.0) + pow(firsPoint.longitude - lastPoint.longitude, 2.0)
        val distance = sqrt(sumPowDistance)
        if (distance <= MAX_DISTANCE_TO_CIRCULAR)
            return true
        return false
    }

    override fun onPointClick(position: Int, isButtonClick: Boolean, isSaved: Boolean) {
        val placesOfInterest = placeResults.get(position)
        if (!isButtonClick)
            mMapManager.addRepresentativeMarker(LatLng(placesOfInterest.location.latitude, placesOfInterest.location.longitude), placesOfInterest.name)
        else
            placesOfInterest.isSaved = isSaved
    }

    fun finishRouteMetadata(view: View) {
        val intent = Intent(this, RouteCreationMetadataActivity::class.java)
        val route = Route(
            mMapManager.getMarkerPoints(),
            savePlaces.map {
                BasicPointOfInterest(it.photo?.photoReference, it.location.latitude, it.location.longitude)
            },
            isRouteCircular()
        )
        intent.putExtra(ROUTE_INFO_MESSAGE, route)
        startActivity(intent)
    }

    private fun getLocationFromViewInput() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        val rowView: View = inflater.inflate(R.layout.form_location_dialog, null)
        val locationEditText = rowView.findViewById<EditText>(R.id.route_location_editText)
        val searchLocationButton = rowView.findViewById<ImageButton>(R.id.search_location_imageButton)

        val alertDialog = builder
            .setView(rowView)
            .create()

        searchLocationButton.setOnClickListener{
            val location = locationEditText.text.toString()
            if (location.isEmpty()) {
                finish()
                getLocationFromViewInput()
            }
            else {
                mMapManager.zoomInLocation(location)
                alertDialog.cancel()
            }
        }

        alertDialog.show()
    }

    fun back(view: View) {
        mMapManager.removeLastMarkerOption()
    }

    fun finish(view: View) {
        if (!mMapManager.mapIsMarked()) {
            val markersRequiredMessage = getString(R.string.markers_required)
            showToast(markersRequiredMessage)
        }
        else {
            sliding_layout.isEnabled = true
            sliding_layout.panelHeight = 525
            val markerPoints = mMapManager.getMarkerPoints()
            for (markerPoint in markerPoints) {
                val location = markerPoint.latitude.toString() + "," + markerPoint.longitude
                requestPlacesOfInterest(location)
            }
        }
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
        }
        else
            finish()
    }

}
