package com.hilguener.superheroesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hilguener.marvelsuperheroes.databinding.EventItemBinding
import com.hilguener.superheroesapp.model.events.Event

class EventAdapter(
    private val onItemClickListener: (Event) -> Unit
) : PagingDataAdapter<Event, EventAdapter.EventViewHolder>(EventComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class EventViewHolder(private val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = getItem(position)
                    event?.let { onItemClickListener(it) }
                }
            }
        }

        fun bind(event: Event?) {
            binding.apply {
                eventTitle.text = event?.title
                eventDescription.text = event?.description

                // Substitua o código abaixo com a lógica para carregar a imagem do evento, se houver

                val imageUrl =
                    "${event?.thumbnail?.path}/landscape_incredible.${event?.thumbnail?.extension}"
                val requestOptions = RequestOptions().transform(RoundedCorners(16))

                Glide.with(itemView)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(eventCover)
            }
        }
    }

    object EventComparator : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}
