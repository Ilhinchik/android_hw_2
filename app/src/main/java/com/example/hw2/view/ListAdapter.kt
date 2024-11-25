package com.example.hw2.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import com.example.hw2.R
import com.example.hw2.api.DogServiceDelegate

class ListAdapter(private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<ViewHolder>() {

    private var _items = ArrayList<DeferredHolder<String?>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        val viewHolder = ViewHolder(view)

        view.setOnClickListener {
            if (viewHolder.imageState == ImageState.EMPTY) {
                viewHolder.onClick(coroutineScope.async { DogServiceDelegate.getDogImageUrl() })
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_items[position])
    }

    fun addItems(url: Deferred<String?>) {
        _items.add(DeferredHolder(url))
        notifyItemInserted(_items.size - 1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getItems(): ArrayList<String?> = ArrayList(_items.map {
        if (it.deferred.isCompleted && !it.deferred.isCancelled) {
            it.deferred.getCompleted()
        } else {
            null
        }
    })

    fun setItems(urls: ArrayList<String?>) {
        _items = ArrayList(urls.map { DeferredHolder(CompletableDeferred(it)) })
    }
}