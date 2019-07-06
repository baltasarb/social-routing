package ps.g49.socialroutingclient.dagger.components

import android.app.Application
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.OkHttpClient
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.activities.LoginActivity
import ps.g49.socialroutingclient.dagger.modules.*
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ActivityBindingModule::class,
    NetworkModule::class,
    ViewModelModule::class,
    RepositoriesModule::class
])
interface ApplicationComponent: AndroidInjector<SocialRoutingApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun socialRoutingAPIBaseUrl(@Named("socialRoutingAPIBaseUrl") baseUrl: String): Builder

        @BindsInstance
        fun googleMapsAPIBaseUrl(@Named("googleMapsAPIBaseUrl") baseUrl: String): Builder

        @BindsInstance
        fun applicationModule(applicationModule: ApplicationModule): Builder

        @BindsInstance
        fun networkModule(networkModule: NetworkModule): Builder

        fun build(): ApplicationComponent
    }

}