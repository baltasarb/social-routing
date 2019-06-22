package ps.g49.socialroutingclient.dagger.modules

import dagger.Module
import dagger.Provides
import ps.g49.socialroutingclient.repositories.GoogleRepository
import ps.g49.socialroutingclient.repositories.SocialRoutingRepository
import ps.g49.socialroutingclient.webService.GoogleWebService
import ps.g49.socialroutingclient.webService.SocialRoutingWebService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideSocialRoutingWebService(retrofit: Retrofit): SocialRoutingWebService {
        return retrofit.create(SocialRoutingWebService::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleWebService(retrofit: Retrofit): GoogleWebService {
        return retrofit.create(GoogleWebService::class.java)
    }

    @Provides
    @Singleton
    fun provideSocialRoutingRepository(socialRoutingWebService: SocialRoutingWebService): SocialRoutingRepository {
        return SocialRoutingRepository(socialRoutingWebService)
    }

    @Provides
    @Singleton
    fun provideGoogleRepository(googleWebService: GoogleWebService): GoogleRepository {
        return GoogleRepository(googleWebService)
    }

}