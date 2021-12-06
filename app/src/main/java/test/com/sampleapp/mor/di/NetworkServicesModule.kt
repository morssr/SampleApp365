package test.com.sampleapp.mor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import test.com.sampleapp.mor.data.api.PropertiesService
import test.com.sampleapp.mor.utilities.server.MockWebServer
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkServicesModule {

    @Provides
    @Singleton
    @RetrofitRemoteBaseUrl
    fun provideBaseUrlForRetrofitRemote(server: MockWebServer): String = server.getServerBaseUrl()

    @Singleton
    @Provides
    fun providePropertiesService(retrofit: Retrofit): PropertiesService {
        return retrofit.create(PropertiesService::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitRemoteBaseUrl