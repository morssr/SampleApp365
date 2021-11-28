package test.com.sampleapp.mor.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import test.com.sampleapp.mor.data.api.resposes.PropertiesResponse
import test.com.sampleapp.mor.data.api.utils.PROPERTIES_REQUEST_BASE_URL

interface PropertiesService {

    @GET(PROPERTIES_REQUEST_BASE_URL)
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): Response<PropertiesResponse>
}