package ps.g49.socialroutingclient.dagger.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
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
        fun baseUrl(@Named("baseUrl") baseUrl: String): Builder

        @BindsInstance
        fun applicationModule(applicationModule: ApplicationModule): Builder

        fun build(): ApplicationComponent
    }

}