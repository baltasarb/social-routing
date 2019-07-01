package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.inputModel.socialRouting.CategoryCollectionInput
import ps.g49.socialroutingclient.model.outputModel.CategoryOutput
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.repositories.GoogleRepository
import ps.g49.socialroutingclient.utils.GoogleMapsManager
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject

class RouteCreationActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapManager: GoogleMapsManager
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var googleViewModel: GoogleViewModel
    private var location: String = ""
    private lateinit var categoriesLinearLayout: LinearLayout
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    @Inject lateinit var googleRepository: GoogleRepository
    @Inject lateinit var viewModelFactory: ViewModelFactory

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Initialize the Route Repository.
        socialRoutingViewModel = getViewModel(viewModelFactory)
        googleViewModel = getViewModel(viewModelFactory)
    }

    /*
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMapManager = GoogleMapsManager(googleMap, googleViewModel, ::handleRequestedData)

        mMap.setOnMapClickListener(mMapManager.onMapClickListener())
        getLocationFromViewInput()
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
            location = locationEditText.text.toString()
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
        else
            showDialogForm()
    }

    private fun showDialogForm() {
        val createMessage = getString(R.string.create)
        val nameAndCategoriesRequiredMessage = getString(R.string.fill_name_categories_form)
        val cancelMessage = getString(R.string.cancel)

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        val rowView: View = inflater.inflate(R.layout.route_creation_dialog, null)
        val nameEditText = rowView.findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = rowView.findViewById<EditText>(R.id.descriptionEditText)
        categoriesLinearLayout = rowView.findViewById(R.id.categoriesLinearLayout)

        setChipGroupView()

        builder
            .setView(rowView)
            .setPositiveButton(createMessage) { dialog, which ->
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
                    showToast(nameAndCategoriesRequiredMessage)
                else {
                    val userId = socialRoutingApplication
                        .getUser()
                        .userUrl
                        .split("/")
                        .last()
                        .toInt()
                    val route = RouteOutput(
                        location,
                        name,
                        description,
                        userId,
                        mMapManager.getMarkerPoints(),
                        categories
                    )
                    val liveData = socialRoutingViewModel.createRoute(route)
                    handleRequestedData(liveData, ::requestSuccessHandlerRouteCreation)
                }

            }
            .setNegativeButton(cancelMessage) { dialog, which -> dialog.cancel() }
            .create()
            .show()
    }

    private fun setChipGroupView() {
        val categoriesUrl = socialRoutingApplication.getSocialRoutingRootResource().categoriesUrl
        val liveData = socialRoutingViewModel.getRouteCategories(categoriesUrl)
        handleRequestedData(liveData, ::requestSuccessHandlerRouteCategories)
    }

    private fun requestSuccessHandlerRouteCreation(identifier: String?) {
        val successRouteCreationMessage = getString(R.string.success_route_creation)
        val routeIdIntentMessage = getString(R.string.route_id_intent_message)
        val routeIntentMessage = getString(R.string.route_intent_message)
        val routeUrl = getString(R.string.route_url)

        showToast(successRouteCreationMessage)

        val intent = Intent(this, RouteRepresentationActivity::class.java)
        intent.putExtra(routeIntentMessage, routeUrl)
        intent.putExtra(routeIdIntentMessage, identifier!!.toInt())
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
