package com.hilguener.superheroesapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.StorieItemBinding
import com.hilguener.superheroesapp.model.stories.Story

class StoryAdapter(
    private val onItemClickListener: (Story) -> Unit
) : PagingDataAdapter<Story, StoryAdapter.StoriesViewHolder>(StoryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding = StorieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class StoriesViewHolder(private val binding: StorieItemBinding) :
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

        fun bind(story: Story?) {
            binding.apply {
                storyTitle.text = story?.title

                val requestOptions = RequestOptions().transform(RoundedCorners(16))

                Glide.with(itemView)
                    .load(R.drawable.marvel)
                    .apply(requestOptions)
                    .into(storyCover)
            }
        }
    }

    object StoryComparator : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }
}
