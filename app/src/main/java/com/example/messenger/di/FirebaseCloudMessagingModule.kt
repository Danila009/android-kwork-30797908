package com.example.messenger.di

import com.example.messenger.data.network.messagingService.api.MessagingServiceApi
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FirebaseCloudMessagingModule {

    @[Provides Singleton]
    fun providerFirebaseMessaging(

    ): FirebaseMessaging = FirebaseMessaging.getInstance()

    @[Provides Singleton]
    fun providerMessagingServiceRetrofit(
        okHttpClient:OkHttpClient
    ):Retrofit = Retrofit.Builder()
        .baseUrl(MessagingServiceApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @[Provides Singleton]
    fun providerMessagingServiceApi(
        retrofit: Retrofit
    ):MessagingServiceApi = retrofit.create()

    @[Provides Singleton]
    fun providerOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
        .writeTimeout(5, TimeUnit.MINUTES) // write timeout
        .readTimeout(5, TimeUnit.MINUTES) // read timeout
        .build()

}