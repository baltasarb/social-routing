package ps.g49.socialroutingclient.activities

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import kotlinx.android.synthetic.main.activity_route_creation_metadata.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.domainModel.Route
import ps.g49.socialroutingclient.model.inputModel.socialRouting.CategoryCollectionInput
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject

class RouteCreationMetadataActivity : BaseActivity() {

    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var googleViewModel: GoogleViewModel
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var route: Route
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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

        route = intent.getParcelableExtra(RouteCreationActivity.ROUTE_INFO_MESSAGE)

        requestCategories()
        circularRadioButton.isChecked = route.isCircular
        if (route.isCircular)
            orderedRadioButton.isClickable = false
    }

    private fun requestCategories() {
        val categoriesUrl = socialRoutingApplication.getSocialRoutingRootResource().categoriesUrl
        val liveData = socialRoutingViewModel.getRouteCategories(categoriesUrl)
        handleRequestedData(liveData, ::requestSuccessHandlerRouteCategories)
    }

    private fun requestSuccessHandlerRouteCategories(categoriesCollection: CategoryCollectionInput?) {
        val categories = categoriesCollection!!.categories
        categories.forEach {
            val checkBox = CheckBox(this)
            checkBox.text = it.name
            categoriesLinearLayout.addView(checkBox)
        }
    }

    fun createRoute(view: View) {

    }



}
