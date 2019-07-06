package ps.g49.socialroutingclient.dagger.modules

import dagger.Module
import dagger.Provides
import ps.g49.socialroutingclient.SocialRoutingApplication
import javax.inject.Singleton

@Module
class ApplicationModule (val application: SocialRoutingApplication)