package ps.g49.socialroutingclient.dagger.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjection
import ps.g49.socialroutingclient.activities.*

//Allows to inject things into Activities using AndroidInjection.inject(this) in the onCreate() method.
@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeNavigationActivity(): NavigationActivity

    @ContributesAndroidInjector
    abstract fun contributeRouteCreationActivity(): RouteCreationActivity

    @ContributesAndroidInjector
    abstract fun contributeRouteSearchActivity(): RoutesSearchActivity

    @ContributesAndroidInjector
    abstract fun contributeRouteRepresentationActivity(): RouteRepresentationActivity

    @ContributesAndroidInjector
    abstract fun contributeRouteUserProfileActivity(): UserProfileActivity

}