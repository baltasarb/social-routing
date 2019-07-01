package ps.g49.socialroutingclient

import ps.g49.socialroutingclient.model.UserAccount
import android.app.Activity
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import ps.g49.socialroutingclient.dagger.components.DaggerApplicationComponent
import ps.g49.socialroutingclient.dagger.modules.ApplicationModule
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SocialRoutingRootResource
import javax.inject.Inject

class SocialRoutingApplication : DaggerApplication() {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    private lateinit var user: UserAccount
    private lateinit var socialRoutingRootResource: SocialRoutingRootResource

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val applicationComponent =  DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .socialRoutingAPIBaseUrl("http://10.0.2.2:8080/api.sr/")
            .googleMapsAPIBaseUrl("https://maps.googleapis.com/maps/api/")
            .application(this)
            .build()
        applicationComponent.inject(this)
        return applicationComponent
    }

    fun setUser(userAccount: UserAccount) { this.user = userAccount }

    fun getUser(): UserAccount = user

    fun setSocialRoutingRootResource(socialRoutingRootResource: SocialRoutingRootResource) {
        this.socialRoutingRootResource = socialRoutingRootResource
    }

    fun getSocialRoutingRootResource() : SocialRoutingRootResource {
        return socialRoutingRootResource
    }

}