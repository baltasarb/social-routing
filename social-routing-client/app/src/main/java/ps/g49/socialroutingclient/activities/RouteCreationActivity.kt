package ps.g49.socialroutingclient.activities

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.chip.ChipGroup
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.inputModel.CategoryCollectionInput
import ps.g49.socialroutingclient.model.outputModel.CategoryOutput
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.utils.GoogleMapsManager
import ps.g49.socialroutingclient.viewModel.RouteViewModel

class RouteCreationActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapManager: GoogleMapsManager

    private lateinit var routeViewModel: RouteViewModel
    private var location: String = ""

    companion object {
        private const val SEARCH = "Search"
        private const val TITLE_DIALOG_ALERT = "Route location"
        private const val MARKERS_REQUIRED = "First add markers to path, to save the Route."
        private const val CANCEL = "Cancel"
        private const val CREATE = "Create"
        private const val ROUTE_CREATED_SUCCESS = "The Route was created Successfully!"
        private const val LOCATION_NOT_FOUND = "Location not Found."
        private const val NAME_REQUIRED = "Fill the Name field, at least."
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove notification bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_route_creation)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Initialize the Route Repository.
        routeViewModel = getViewModel()
    }

    /*
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMapManager = GoogleMapsManager(googleMap)

        mMap.setOnMapClickListener(mMapManager.onMapClickListener())

        getLocationFromViewInput()
        routeViewModel = getViewModel()
    }

    private fun getLocationFromViewInput() {
        val alertDialog = AlertDialog.Builder(this)

        // Set Title.
        alertDialog.setTitle(TITLE_DIALOG_ALERT)

        // Create the edit text for location.
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL

        // Set Dialog Message
        alertDialog
            .setCancelable(false)
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
        // TODO If the device is connected to the internet makes a post request to the service to save the routeDetailed in db, or if the device is not connected to internet, this work will wait until the connection is on again.
    }

    private fun showDialogForm() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        val rowView: View = inflater.inflate(R.layout.route_creation_dialog, null)
        val nameEditText = rowView.findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = rowView.findViewById<EditText>(R.id.descriptionEditText)
        val spinner = rowView.findViewById<ProgressBar>(R.id.creationProgressBar)
        val chipGroup = rowView.findViewById<ChipGroup>(R.id.categoriesChipGroup)
        setChipGroupView(chipGroup)

        spinner.visibility = View.GONE

        builder
            .setView(rowView)
            .setPositiveButton(CREATE) { dialog, which ->
                spinner.visibility = View.VISIBLE
                val name = nameEditText.text.toString()
                val description = descriptionEditText.text.toString()

                if (name.isEmpty())
                    showToast(NAME_REQUIRED)
                else {
                    val route = RouteOutput(
                        location, name, description, 100, mMapManager.getMarkerPoints(), listOf(CategoryOutput("Other"))
                    )
                    val liveData = routeViewModel.createRoute(route)
                    handleRequestedData(liveData, ::requestSuccessHandlerRouteCreation)
                }

            }
            .setNegativeButton(CANCEL) { dialog, which ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun setChipGroupView(chipGroup: ChipGroup) {
        val liveData = routeViewModel.getRouteCategories()
        handleRequestedData(liveData, ::requestSuccessHandlerRouteCategories)
    }

    private fun requestSuccessHandlerRouteCreation(result: String) {
        showToast(ROUTE_CREATED_SUCCESS)
    }

    private fun requestSuccessHandlerRouteCategories (categoriesCollection: CategoryCollectionInput) {
        val categories = categoriesCollection.categories
        /*categories.forEach {
            val chip = Chip(this)
            chip.text = it.name

            chip.isClickable = true
            chip.isCheckable = true
            chipGroup.addView(chip)
        }*/
    }
}