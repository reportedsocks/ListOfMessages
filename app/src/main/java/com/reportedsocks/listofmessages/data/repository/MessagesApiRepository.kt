package com.reportedsocks.listofmessages.data.repository

import com.reportedsocks.listofmessages.network.model.MessagesApiResponse
import com.reportedsocks.listofmessages.network.repository.MessagesApiService
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit repository
 */
@Singleton
class MessagesApiRepository @Inject constructor(val messagesApiService: MessagesApiService){
    fun getMessages(): Observable<MessagesApiResponse>{
        return messagesApiService.getMessages()
    }

    fun getNextPage( pageToken:String ): Observable<MessagesApiResponse>{
        return messagesApiService.getNextPage(pageToken)
    }
}