package ps.g49.socialroutingclient

import ps.g49.socialroutingclient.model.domainModel.UserAccount
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import ps.g49.socialroutingclient.dagger.components.DaggerApplicationComponent
import ps.g49.socialroutingclient.dagger.modules.ApplicationModule
import ps.g49.socialroutingclient.dagger.modules.NetworkModule
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SocialRoutingRootResource
import ps.g49.socialroutingclient.services.LocationService
import javax.inject.Inject

class SocialRoutingApplication : DaggerApplication() {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    private lateinit var user: UserAccount
    private lateinit var socialRoutingRootResource: SocialRoutingRootResource
    private lateinit var userCurrentLocation: Location
    private var isLocationFound: Boolean = false

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .socialRoutingAPIBaseUrl("http://10.0.2.2:8080/api.sr/")
            .googleMapsAPIBaseUrl("https://maps.googleapis.com/maps/api/")
            .application(this)
            .build()
        applicationComponent.inject(this)
        return applicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(getMessageReceiver(), IntentFilter(LocationService.INTENT_FILTER))
        startService(Intent(this, LocationService::class.java))
    }

    private fun getMessageReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    isLocationFound = true
                    userCurrentLocation = intent.getParcelableExtra(LocationService.INTENT_MESSAGE)
                }
            }
        }
    }

    fun getUserCurrentLocation(): Location = userCurrentLocation

    fun isLocationFound(): Boolean = isLocationFound

    fun setUser(userAccount: UserAccount) {
        this.user = userAccount
    }

    fun getUser(): UserAccount = user

    fun setSocialRoutingRootResource(socialRoutingRootResource: SocialRoutingRootResource) {
        this.socialRoutingRootResource = correctUrl(socialRoutingRootResource)
    }

    private fun correctUrl(socialRoutingRootResource: SocialRoutingRootResource): SocialRoutingRootResource {
        val personUrl = removeUrlLastResource(socialRoutingRootResource.personUrl)
        val routeSearchUrl = removeUrlQueryString(socialRoutingRootResource.routeSearchUrl)
        return SocialRoutingRootResource(
            personUrl,
            routeSearchUrl,
            socialRoutingRootResource.categoriesUrl,
            socialRoutingRootResource.authenticationUrls,
            socialRoutingRootResource.documentationUrl
        )
    }

    private fun removeUrlQueryString(url: String): String = url.split("?")[0]

    private fun removeUrlLastResource(url: String): String =
        url.split("{")[0]

    fun getSocialRoutingRootResource(): SocialRoutingRootResource {
        return socialRoutingRootResource
    }

}