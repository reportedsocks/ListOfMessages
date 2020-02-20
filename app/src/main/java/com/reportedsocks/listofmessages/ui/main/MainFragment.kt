package com.reportedsocks.listofmessages.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reportedsocks.listofmessages.MyApplicationComponent
import com.reportedsocks.listofmessages.R
import com.reportedsocks.listofmessages.data.di.ViewModelFactory
import com.reportedsocks.listofmessages.network.model.Message
import com.reportedsocks.listofmessages.network.repository.MessagesApiService
import com.reportedsocks.listofmessages.ui.adapter.MessagesAdapter
import com.reportedsocks.listofmessages.ui.adapter.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.main_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel
    private var hasSavedScroll = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as MyApplicationComponent).appComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get a ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        //setup RecyclerView
        val linearLayoutManager = LinearLayoutManager(activity?.baseContext)
        val adapter = MessagesAdapter(viewModel)
        val callback = SimpleItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        recycler_view.adapter = adapter
        touchHelper.attachToRecyclerView(recycler_view)
        recycler_view.layoutManager = linearLayoutManager

        //check if VM has saved scroll
        var savedIndex: Int? = 0
        var savedTop: Int? = 0
        if(viewModel.recyclerViewScroll[0] != null ){
            savedIndex = viewModel.recyclerViewScroll[0]
            savedTop = viewModel.recyclerViewScroll[1]
            hasSavedScroll = true
        }

        // observe messages
        viewModel.getMessages().observe(viewLifecycleOwner, Observer<List<Message>>{ listOfMessages ->

            // save scroll before list changes.
            // Don't do it if any scroll was made before and fragment is being restarted
            if(!hasSavedScroll){
                viewModel.recyclerViewScroll = getCurrentScroll(linearLayoutManager)
            }
            // update adapter
            adapter.addItems(listOfMessages.toMutableList())

            //set previous scroll after adapter update
            if(hasSavedScroll){
                linearLayoutManager.scrollToPositionWithOffset(savedIndex!!, savedTop!!)
                hasSavedScroll = false
            } else {
                if(viewModel.recyclerViewScroll[0] != null ){
                    linearLayoutManager.scrollToPositionWithOffset(
                        viewModel.recyclerViewScroll[0]!!,
                        viewModel.recyclerViewScroll[1]?:0)
                }
            }
        })

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //if user is scrolling close to the end of list, request next page
                if(linearLayoutManager.findLastVisibleItemPosition() >= recyclerView.adapter!!.itemCount - 4){
                    viewModel.getNextPage()
                }
            }
        })
    }

    private fun getCurrentScroll(linearLayoutManager: LinearLayoutManager):List<Int?>{
        val index: Int = linearLayoutManager.findFirstVisibleItemPosition()
        val v: View? = linearLayoutManager.getChildAt(0)
        val top =
            if (v == null) 0 else v.top - linearLayoutManager.paddingTop
        return listOf(index, top)
    }

}
