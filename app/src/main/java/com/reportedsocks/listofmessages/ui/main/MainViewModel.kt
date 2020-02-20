package com.reportedsocks.listofmessages.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reportedsocks.listofmessages.data.repository.MessagesApiRepository
import com.reportedsocks.listofmessages.network.model.Message
import com.reportedsocks.listofmessages.ui.adapter.ItemTouchHelperAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(val messagesApiRepository: MessagesApiRepository) : ViewModel() {
    // saves scroll for fragment
    var recyclerViewScroll: List<Int?> = listOf(null, null)
    // observable list of messages
    private var messages: MutableLiveData<List<Message>>? = null
    // saves page token
    private var pageToken: String? = null

    /**
     * @return Returns Observable list of messages. If list is null, performs a request
     */
    fun getMessages(): MutableLiveData<List<Message>>{
        if (messages == null){
            messages = MutableLiveData()
            requestMessagesFromApi()
        }
        return messages!!
    }

    /**
     * Sets the value of messages list
     * @param messages os a new value which will be set
     */
    fun setMessages( messages: List<Message> ){
        this.messages!!.value = messages
    }

    /**
     * Requests next page using saved pageToken and updates messages LiveData
     */
    @SuppressLint("CheckResult")
    fun getNextPage(){
        if (pageToken != null){
            messagesApiRepository.getNextPage(pageToken!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    addValuesToLiveData(result.messages)
                    pageToken = result.pageToken
                }, { error ->
                    Log.d("MyLogs", error.message?:"request error")
                })
        }

    }

    //adds new messages to the existing list
    private fun addValuesToLiveData( newMessages: List<Message>) {
        val mList = mutableListOf<Message>()
        mList.addAll(messages!!.value!!)
        mList.addAll(newMessages)
        messages!!.postValue(mList)
    }

    // request 1st page from the Api
    @SuppressLint("CheckResult")
    private fun requestMessagesFromApi() {
        messagesApiRepository.getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                messages!!.postValue(result.messages)
                pageToken = result.pageToken
            }, { error ->
                Log.d("MyLogs", error.message?:"request error")
            })
    }
}
