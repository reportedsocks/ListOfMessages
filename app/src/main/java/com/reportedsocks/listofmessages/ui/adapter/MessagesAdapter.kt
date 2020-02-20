package com.reportedsocks.listofmessages.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reportedsocks.listofmessages.R
import com.reportedsocks.listofmessages.network.model.Message
import com.reportedsocks.listofmessages.ui.main.MainViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.messages_list_item.view.*
import java.util.*

class MessagesAdapter ( private val viewModel: MainViewModel):
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>(),
    ItemTouchHelperAdapter {

    private var items = mutableListOf<Message>()

    override fun onItemDismiss(position: Int) {
        this.items.removeAt(position)
        viewModel.setMessages(items.toList())
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.messages_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.author_text_view.text = item.author.name
        holder.itemView.message_time_text_view.text = formatDate(item.updated)
        holder.itemView.message_content_text_view.text = item.content
        Picasso.get().load("http://message-list.appspot.com/${item.author.photoUrl}")
            .fit()
            .centerInside()
            .placeholder(R.drawable.ic_person_grey_30dp)
            .error(R.drawable.ic_person_grey_30dp)
            .into(holder.itemView.author_image_view)

    }

    fun addItems(items: MutableList<Message>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    private fun formatDate(date: Date): String{
        val hours = (date.time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val minutes = (date.time % (1000 * 60 * 60 )) / (1000 * 60 )
        var hString = "$hours"
        if( hours < 10 ){
            hString = "0$hours"
        }
        var mString = "$minutes"
        if( minutes < 10 ){
            mString = "0$minutes"
        }
        return "$hString:$mString"
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

}