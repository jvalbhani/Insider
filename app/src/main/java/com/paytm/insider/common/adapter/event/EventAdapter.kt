package com.paytm.insider.common.adapter.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paytm.insider.R
import com.paytm.insider.common.datamodel.Event
import com.paytm.insider.common.utility.ImageLoader
import kotlinx.android.synthetic.main.layout_event_standard.view.*

class EventAdapter(private val items: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null
    override fun getItemCount() = items.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return EventViewHolder(
            view,
            onItemClickListener
        )
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.layout_event_standard
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class EventViewHolder(view: View, private val listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view) {
        fun bind(event: Event) {
            with(itemView) {
                ImageLoader.loadImageFromUrl(event.horizontalCoverImage, ivThumb)
                tvTitle.text = event.name
                tvDateTime.text = event.venueDateString
                tvVenue.text = event.venueName
                tvTag.text = event.type
                tvPrice.text =
                    context.getString(R.string.rupee_symbol, event.priceDisplayString)

                setOnClickListener {
                    listener?.onClick(event)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(event: Event)
    }
}
