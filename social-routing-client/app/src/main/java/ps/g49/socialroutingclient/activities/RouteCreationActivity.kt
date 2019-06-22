package ps.g49.socialroutingclient.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.inputModel.CategoryCollectionInput
import ps.g49.socialroutingclient.model.outputModel.CategoryOutput
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.repositories.GoogleRepository
import ps.g49.socialroutingclient.utils.GoogleMapsManager
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject

class RouteCreationActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapManager: GoogleMapsManager
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private var location: String = ""
    private lateinit var categoriesLinearLayout: LinearLayout
    val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    val LOCATION_PERMISSION_REQUEST = 1234

    private lateinit var socialRoutingApplication: SocialRoutingApplication
    @Inject
    lateinit var googleRepository: GoogleRepository
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        private const val SEARCH = "Search"
        private const val TITLE_DIALOG_ALERT = "Route location"
        private const val MARKERS_REQUIRED = "First add markers to path, to save the Route."
        private const val CANCEL = "Cancel"
        private const val CREATE = "Create"
        private const val ROUTE_CREATED_SUCCESS = "The Route was created Successfully!"
        private const val LOCATION_NOT_FOUND = "Location not Found."
        private const val NAME_AND_CATEGORIES_REQUIRED = "Need to fill at least the name and one category"
        private const val QUESTION_TO_DELETE = "Are you sure you want to erase your creation ?"
        private const val YES = "Yes"
        private const val NO = "No"
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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Initialize the Route Repository.
        socialRoutingViewModel = getViewModel(viewModelFactory)
    }

    /*
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMapManager = GoogleMapsManager(googleMap, googleRepository)

        mMap.setOnMapClickListener(mMapManager.onMapClickListener())
        getLocationFromViewInput()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.isMyLocationEnabled = true
        else
        // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST)
    }

    override fun onBackPressed() {
        if (mMapManager.mapIsMarked()) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage(QUESTION_TO_DELETE)
            alertDialog
                .setCancelable(true)
                .setPositiveButton(YES) { dialog, which -> finish() }
                .setNegativeButton(NO) { dialog, which -> dialog.cancel() }
            alertDialog.create()
            alertDialog.show()
        }
        else
            finish()
    }

    private fun getLocationFromViewInput() {
        val alertDialog = AlertDialog.Builder(this)

        // Set Title.
        alertDialog.setTitle(TITLE_DIALOG_ALERT)

        // Create the edit text for location.
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS

        // Set Dialog Message
        alertDialog
            .setCancelable(true)
            .setView(editText)

        // Set up the buttons
        alertDialog
            .setPositiveButton(SEARCH) { dialog, which ->

                location = editText.text.toString()
                if (location.isEmpty())
                    getLocationFromViewInput()
                else
                    mMapManager.zoomInLocation(location) {
                        showToast(LOCATION_NOT_FOUND)
                        getLocationFromViewInput()
                    }
            }
            .setNegativeButton(CANCEL) { dialog, which ->
                finish()
            }

        // Show in UI.
        alertDialog.show()
    }

    fun back(view: View) {
        mMapManager.removeLastMarkerOption()
    }

    fun finish(view: View) {
        if (!mMapManager.mapIsMarked())
            showToast(MARKERS_REQUIRED)
        else
            showDialogForm()
    }

    private fun showDialogForm() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        val rowView: View = inflater.inflate(R.layout.route_creation_dialog, null)
        val nameEditText = rowView.findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = rowView.findViewById<EditText>(R.id.descriptionEditText)
        categoriesLinearLayout = rowView.findViewById(R.id.categoriesLinearLayout)

        setChipGroupView()

        builder
            .setView(rowView)
            .setPositiveButton(CREATE) { dialog, which ->
                val name = nameEditText.text.toString()
                val description = descriptionEditText.text.toString()

                val categoriesChecked = mutableListOf<CheckBox>()
                for (i in 0 until categoriesLinearLayout.childCount)
                    categoriesChecked.add(categoriesLinearLayout.getChildAt(i) as CheckBox)
                val categories = categoriesChecked
                    .filter { it.isChecked }
                    .map { CategoryOutput(it.text.toString()) }
                    .toList()

                if (name.isEmpty() || categories.isEmpty())
                    showToast(NAME_AND_CATEGORIES_REQUIRED)
                else {
                    val route = RouteOutput(
                        location,
                        name,
                        description,
                        100,
                        mMapManager.getMarkerPoints(),
                        categories
                    )
                    val accessToken = socialRoutingApplication.getUser().accessToken
                    val liveData = socialRoutingViewModel.createRoute(route)
                    handleRequestedData(liveData, ::requestSuccessHandlerRouteCreation)
                }

            }
            .setNegativeButton(CANCEL) { dialog, which -> dialog.cancel() }
            .create()
            .show()
    }

    private fun setChipGroupView() {
        val accessToken = socialRoutingApplication.getUser().accessToken
        val liveData = socialRoutingViewModel.getRouteCategories()
        handleRequestedData(liveData, ::requestSuccessHandlerRouteCategories)
    }

    private fun requestSuccessHandlerRouteCreation(identifier: String?) {
        showToast(ROUTE_CREATED_SUCCESS)
        val intent = Intent(this, RouteRepresentationActivity::class.java)
        intent.putExtra(RouteRepresentationActivity.ROUTE_ID_MESSAGE, identifier!!.toInt())
        startActivity(intent)
        finish()
    }

    private fun requestSuccessHandlerRouteCategories(categoriesCollection: CategoryCollectionInput?) {
        val categories = categoriesCollection!!.categories
        categories.forEach {
            val checkBox = CheckBox(applicationContext)
            checkBox.text = it.name
            categoriesLinearLayout.addView(checkBox)
        }
    }

}