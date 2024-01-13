package com.hilguener.marvelsuperheroes.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.SerieItemBinding
import com.hilguener.marvelsuperheroes.model.series.Serie

class SeriesAdapter(
    private val onItemClickListener: (Serie) -> Unit
) : PagingDataAdapter<Serie, SeriesAdapter.SeriesViewHolder>(SeriesComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val binding = SerieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SeriesViewHolder(private val binding: SerieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val story = getItem(position)
                    story?.let { onItemClickListener(it) }
                }
            }
        }

        fun bind(serie: Serie?) {
            binding.apply {
                if (serie?.title != null) {
                    serieTitle.text = serie.title

                    val requestOptions = RequestOptions().transform(RoundedCorners(16))

                    Glide.with(itemView)
                        .load(R.drawable.marvel)
                        .apply(requestOptions)
                        .into(serieCover)

                    root.visibility = View.VISIBLE // Certifique-se de que a visibilidade é definida como VISIBLE se a descrição não for nula
                } else {
                    root.visibility = View.GONE // Se a descrição for nula, esconda o item do RecyclerView
                }
            }
        }


    }

    object SeriesComparator : DiffUtil.ItemCallback<Serie>() {
        override fun areItemsTheSame(oldItem: Serie, newItem: Serie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Serie, newItem: Serie): Boolean {
            return oldItem == newItem
        }
    }
}