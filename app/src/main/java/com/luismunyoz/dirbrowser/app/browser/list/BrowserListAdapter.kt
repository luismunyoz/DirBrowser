package com.luismunyoz.dirbrowser.app.browser.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luismunyoz.dirbrowser.app.browser.list.model.ItemUIItem
import com.luismunyoz.dirbrowser.databinding.ItemFolderBinding
import com.luismunyoz.dirbrowser.databinding.ItemImageBinding
import com.luismunyoz.dirbrowser.databinding.ItemOtherBinding
import com.luismunyoz.domain.browser.model.Item
import com.squareup.picasso.Picasso

class BrowserListAdapter(
    private val baseUrl: String,
    private val picasso: Picasso
) : RecyclerView.Adapter<BrowserListAdapter.ViewHolder>() {

    private var items: MutableList<ItemUIItem> = mutableListOf()

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            HOLDERS.HOLDER_FOLDER.ordinal -> ViewHolder.FolderViewHolder(
                ItemFolderBinding.inflate(LayoutInflater.from(parent.context))
            )
            HOLDERS.HOLDER_IMAGE.ordinal -> ViewHolder.ImageViewHolder(
                ItemImageBinding.inflate(LayoutInflater.from(parent.context))
            )
            else -> ViewHolder.OtherViewHolder(
                ItemOtherBinding.inflate(LayoutInflater.from(parent.context))
            )
        }
    }

    override fun getItemCount() = items.count()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ItemUIItem.Folder -> HOLDERS.HOLDER_FOLDER.ordinal
            is ItemUIItem.Image -> HOLDERS.HOLDER_IMAGE.ordinal
            is ItemUIItem.Other -> HOLDERS.HOLDER_OTHER.ordinal
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.FolderViewHolder -> {
                val item = (items[position] as ItemUIItem.Folder)
                holder.binding.apply {
                    name.text = item.name
                    modified.text = item.modified
                    card.setOnClickListener {
                        listener?.onItemClicked(item.id)
                    }
                }
            }
            is ViewHolder.ImageViewHolder -> {
                val item = (items[position] as ItemUIItem.Image)
                holder.binding.apply {
                    name.text = item.name
                    modified.text = item.modified
                    picasso
                        .load(item.getUrl(baseUrl))
                        .fit()
                        .centerCrop()
                        .into(image)
                    card.setOnClickListener {
                        listener?.onItemClicked(item.id)
                    }
                }
            }
            is ViewHolder.OtherViewHolder -> {
                val item = (items[position] as ItemUIItem.Other)
                holder.binding.apply {
                    name.text = item.name
                    modified.text = item.modified
                }
            }
        }
    }

    fun setItems(newItems: List<ItemUIItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class FolderViewHolder(val binding: ItemFolderBinding) : ViewHolder(binding.root)

        class ImageViewHolder(val binding: ItemImageBinding) : ViewHolder(binding.root)

        class OtherViewHolder(val binding: ItemOtherBinding) : ViewHolder(binding.root)
    }

    enum class HOLDERS {
        HOLDER_FOLDER,
        HOLDER_IMAGE,
        HOLDER_OTHER
    }

    interface Listener {
        fun onItemClicked(id: String)
    }
}