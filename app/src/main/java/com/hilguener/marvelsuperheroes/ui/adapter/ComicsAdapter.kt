package com.hilguener.superheroesapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hilguener.marvelsuperheroes.databinding.ComicItemBinding
import com.hilguener.superheroesapp.model.comics.Comic

class ComicsAdapter(private var listener: OnItemClickListener) :
    PagingDataAdapter<Comic, ComicsAdapter.ComicsViewHolder>(
        ComicComparator()
    ) {

    interface OnItemClickListener {
        fun onItemClick(comicId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {
        val binding = ComicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        val comic = getItem(position)
        comic?.let {
            holder.bind(it, listener)
        }
    }

    inner class ComicsViewHolder(private val binding: ComicItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(comic: Comic, listener: OnItemClickListener) {
            binding.apply {
                comicTitle.text = comic.title
                val imageUrl =
                    "${comic.thumbnail.path}/portrait_xlarge.${comic.thumbnail.extension}"
                val requestOptions = RequestOptions().transform(RoundedCorners(16))

                Glide.with(itemView)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(binding.comicCover)
            }
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                getItem(position)?.id?.let {
                    listener.onItemClick(it) // Passes the comic ID to the listener
                }
            }
        }
    }


    class ComicComparator : DiffUtil.ItemCallback<Comic>() {
        override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem?.id == newItem?.id // Use a property that uniquely identifies a Comic object
        }

        override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem == newItem // Replace with your comparison logic for Comic objects
        }
    }
}
