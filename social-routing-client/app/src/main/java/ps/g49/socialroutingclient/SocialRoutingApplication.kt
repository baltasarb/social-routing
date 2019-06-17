package ps.g49.socialroutingclient

import android.app.Application
import ps.g49.socialroutingclient.model.UserAccount
import ps.g49.socialroutingclient.webService.GoogleWebService
import ps.g49.socialroutingclient.webService.RetrofitClient
import ps.g49.socialroutingclient.webService.SocialRoutingWebService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class SocialRoutingApplication : Application() {

    private lateinit var user: UserAccount
    private lateinit var idToken: String

    override fun onCreate() {
        super.onCreate()

        // fazer pedido á base
    }
    fun setUser(userAccount: UserAccount) {
        this.user = userAccount
    }

    fun getUser(): UserAccount = user

}