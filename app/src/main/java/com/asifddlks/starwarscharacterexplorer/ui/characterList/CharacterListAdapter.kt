package com.asifddlks.starwarscharacterexplorer.ui.characterList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asifddlks.starwarscharacterexplorer.databinding.CharacterListItemBinding
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel

class CharacterListAdapter(
    private val onClick: (CharacterModel) -> Unit,
) : PagingDataAdapter<CharacterModel, CharacterListAdapter.ViewHolder>(Character_COMPARATOR) {
    class ViewHolder(
        private val binding: CharacterListItemBinding,
        val onClick: (CharacterModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var character: CharacterModel? = null

        init {
            binding.root.setOnClickListener {
                character?.let {
                    onClick(it)
                }
            }
        }

        fun bind(p: CharacterModel?) {
            character = p
            binding.peopleListItemNameTextView.text = character?.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharacterListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val Character_COMPARATOR = object : DiffUtil.ItemCallback<CharacterModel>() {
            override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
                oldItem.uid == newItem.uid
        }
    }
}
