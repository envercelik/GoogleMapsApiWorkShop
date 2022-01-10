package com.envercelik.googlemapsapiworkshop.di

import com.envercelik.googlemapsapiworkshop.common.Constants.BASE_URL
import com.envercelik.googlemapsapiworkshop.data.remote.GoogleDirectionService
import com.envercelik.googlemapsapiworkshop.data.repository.DirectionRepositoryImpl
import com.envercelik.googlemapsapiworkshop.domain.repository.DirectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleDirectionService(retrofit: Retrofit): GoogleDirectionService {
        return retrofit.create(GoogleDirectionService::class.java)
    }

    @Provides
    @Singleton
    fun provideDirectionRepository(service: GoogleDirectionService): DirectionRepository {
        return DirectionRepositoryImpl(service)
    }
}