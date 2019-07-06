package ps.g49.socialroutingclient.dagger.modules

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ps.g49.socialroutingclient.repositories.GoogleRepository
import ps.g49.socialroutingclient.repositories.SocialRoutingRepository
import ps.g49.socialroutingclient.services.webService.GoogleWebService
import ps.g49.socialroutingclient.services.webService.SocialRoutingWebService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Named
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
    fun provideGoogleWebService(
        okHttpClient: OkHttpClient,
        objectMapper: ObjectMapper,
        @Named("googleMapsAPIBaseUrl") baseUrl: String
    ): GoogleWebService {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(GoogleWebService::class.java)
    }

    @Provides
    @Singleton
    fun provideSocialRoutingRepository(
        socialRoutingWebService: SocialRoutingWebService,
        okHttpClient: OkHttpClient,
        objectMapper: ObjectMapper,
        @Named("socialRoutingAPIBaseUrl") baseUrl: String
    ): SocialRoutingRepository {
        return SocialRoutingRepository(socialRoutingWebService, okHttpClient, objectMapper, baseUrl)
    }

    @Provides
    @Singleton
    fun provideGoogleRepository(googleWebService: GoogleWebService): GoogleRepository {
        return GoogleRepository(googleWebService)
    }

}