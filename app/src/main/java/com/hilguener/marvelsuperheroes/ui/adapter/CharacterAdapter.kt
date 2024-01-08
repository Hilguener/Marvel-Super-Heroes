package com.hilguener.superheroesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hilguener.marvelsuperheroes.databinding.CharacterItemBinding
import com.hilguener.superheroesapp.model.character.Character

class CharacterAdapter(
    private val onItemClickListener: (Character) -> Unit
) : PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = CharacterItemBinding.inflate(
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

    inner class CharacterViewHolder(private val binding: CharacterItemBinding) :
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
                villainTitle.text = character?.name
                val imageUrl =
                    "${character?.thumbnail?.path}/portrait_fantastic.${character?.thumbnail?.extension}"
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
