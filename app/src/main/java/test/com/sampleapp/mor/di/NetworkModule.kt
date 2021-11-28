package test.com.sampleapp.mor.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import test.com.sampleapp.mor.data.api.utils.NullToEmptyStringAdapter
import test.com.sampleapp.mor.utilities.server.ApiMockResponsesFactory
import test.com.sampleapp.mor.utilities.server.MockWebServer
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideMockWebServer(): MockWebServer =
        MockWebServer(ApiMockResponsesFactory.appMockResponses)

    @Singleton
    @Provides
    fun provideRetrofit(@RetrofitRemoteBaseUrl baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(NullToEmptyStringAdapter())
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
            .client(client)
            .baseUrl(baseUrl)
            .build()
    }
}