package com.example.marsrover.service

import com.example.marsrover.BuildConfig
import com.example.marsrover.service.model.RoverPhotoRemoteModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarsRoverPhotoService {

    @GET("mars-photos/api/v1/rovers/{rover_name}/photos?api_key=DEMO_KEY")
    suspend fun getMarsRoverPhotos(
        @Path("rover_name") roverName: String,
        @Query("sol") sol: String
    ) : RoverPhotoRemoteModel

    companion object {
        private const val BASE_URL = "https://api.nasa.gov/"

        fun create(): MarsRoverPhotoService {
            val logger = HttpLoggingInterceptor()
            logger.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MarsRoverPhotoService::class.java)
        }
    }

}