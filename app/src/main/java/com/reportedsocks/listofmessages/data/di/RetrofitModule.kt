package com.reportedsocks.listofmessages.data.di

import com.google.gson.GsonBuilder
import com.reportedsocks.listofmessages.network.repository.MessagesApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module to provide Retrofit
 */
@Module
class RetrofitModule {
    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        // needed to parse date
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://message-list.appspot.com")
            .build()
    }
    @Singleton
    @Provides
    fun getMessagesApiService(retrofit: Retrofit): MessagesApiService {
        return retrofit.create(MessagesApiService::class.java)
    }
}