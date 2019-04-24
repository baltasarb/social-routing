package com.example.socialrouting.activities

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.socialrouting.R
import com.example.socialrouting.kotlinx.getViewModel
import com.example.socialrouting.model.Point
import com.example.socialrouting.model.outputModel.RouteOutput
import com.example.socialrouting.repositories.RouteRepository
import com.example.socialrouting.utils.GoogleMapsManager
import com.example.socialrouting.viewModel.RouteViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.route_creation_dialog.*

class RouteCreationActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapManager: GoogleMapsManager

    private lateinit var routeViewModel: RouteViewModel
    private var location: String = ""

    companion object {
        private const val SEARCH = "Search"
        private const val TITLE_DIALOG_ALERT = "Route location"
        private const val MARKERS_NEEDED = "First add markers to path, to save the Route."
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
        alertDialog.setPositiveButton(SEARCH) { dialog, which ->

            location = editText.text.toString()
            if (location.isEmpty())
                getLocationFromViewInput()
            else
                mMapManager.zoomInLocation(location) {
                    viewToast("Location not Found.")
                    getLocationFromViewInput()
                }
        }

        // Show in UI.
        alertDialog.show()
    }

    fun back(view: View) {
        mMapManager.removeLastMarkerOption()
    }

    fun finish(view: View) {
        if (!mMapManager.mapIsMarked())
            viewToast(MARKERS_NEEDED)
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
        // TODO fazer/obter categories
        spinner.visibility = View.GONE

        builder
            .setView(rowView)
            .setPositiveButton("Create") { dialog, which ->
                spinner.visibility = View.VISIBLE
                val name = nameEditText.text.toString()
                val description = descriptionEditText.text.toString()

                if (name.isEmpty())
                    viewToast("Fill the Name field, at least.")
                else {
                    val route = RouteOutput(location, name, description, mMapManager.getMarkerPoints(), 100, listOf("Other"))
                    val liveData = routeViewModel.createRoute(route)
                    handleRequestedData(liveData)
                }

            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun viewToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun <T> requestSuccessHandler(result: T) {
        viewToast("The Route was created Successfully!")
    }
}