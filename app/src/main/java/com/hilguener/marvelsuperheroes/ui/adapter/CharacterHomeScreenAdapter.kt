package com.hilguener.marvelsuperheroes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hilguener.marvelsuperheroes.databinding.CharacterItemBinding
import com.hilguener.marvelsuperheroes.databinding.CharacterItemHomeScreenBinding
import com.hilguener.superheroesapp.model.character.Character

class CharacterHomeScreenAdapter(
    private val onItemClickListener: (Character) -> Unit
) : PagingDataAdapter<Character, CharacterHomeScreenAdapter.CharacterViewHolder>(CharacterComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = CharacterItemHomeScreenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CharacterViewHolder(private val binding: CharacterItemHomeScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val character = getItem(position)
                    character?.let { onItemClickListener(it) }
                }
            }
        }

        fun bind(character: Character?) {
            binding.apply {
                val imageUrl =
                    "${character?.thumbnail?.path}/landscape_amazing.${character?.thumbnail?.extension}"
                val requestOptions = RequestOptions().transform(RoundedCorners(16))

                Glide.with(itemView)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(villainCover)
            }
        }
    }

    object CharacterComparator : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}
