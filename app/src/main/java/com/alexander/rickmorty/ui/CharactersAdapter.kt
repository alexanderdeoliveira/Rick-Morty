package com.alexander.rickmorty.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexander.rickmorty.BR
import com.alexander.rickmorty.databinding.ItemCharacterBinding
import com.alexander.rickmorty.model.viewobject.CharacterViewObject

class CharactersAdapter(
        private val characterClicked: (CharacterViewObject) -> Unit
) : PagingDataAdapter<CharacterViewObject, CharactersAdapter.CharacterViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(ItemCharacterBinding.inflate(LayoutInflater.from(parent.context)), characterClicked)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class CharacterViewHolder(
            private val binding: ItemCharacterBinding,
            private val characterClicked: (CharacterViewObject) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterViewObject) {
            binding.apply {
                setVariable(BR.character, character)
                executePendingBindings()
                root.setOnClickListener { characterClicked.invoke(character) }
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<CharacterViewObject>() {
            override fun areItemsTheSame(oldItem: CharacterViewObject, newItem: CharacterViewObject): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CharacterViewObject, newItem: CharacterViewObject): Boolean {
                return oldItem == newItem
            }
        }
    }
}
