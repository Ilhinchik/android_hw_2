package com.example.hw2.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import com.example.hw2.R
import com.example.hw2.api.DogServiceDelegate

class MainFragment: Fragment(R.layout.main_fragment) {

    companion object SPAN_COUNT {
        const val LANDSCAPE = 3
        const val PORTRAIT = 2
    }

    private val adapter = ListAdapter(lifecycleScope)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val button = view.findViewById<Button>(R.id.add_dog_button)

        val currentOrientation = resources.configuration.orientation
        val spanCount = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            LANDSCAPE
        } else {
            PORTRAIT
        }
        val layoutManager = GridLayoutManager(view.context, spanCount)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager



        button.setOnClickListener {
            val deferred = lifecycleScope.async {
                DogServiceDelegate.getDogImageUrl()
            }

            adapter.addItems(deferred)
        }

        if (savedInstanceState != null) {
            savedInstanceState.getStringArrayList("Items")?.let { adapter.setItems(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArrayList("Items", adapter.getItems())
    }
}