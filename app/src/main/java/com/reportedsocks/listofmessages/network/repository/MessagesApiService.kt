package com.reportedsocks.listofmessages.network.repository

import com.reportedsocks.listofmessages.network.model.MessagesApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Interface for Retrofit calls
 */
interface MessagesApiService {

    @GET("/messages")
    fun getMessages(): Observable<MessagesApiResponse>
    @GET("/messages")
    fun getNextPage( @Query("pageToken") pageToken: String ): Observable<MessagesApiResponse>

}