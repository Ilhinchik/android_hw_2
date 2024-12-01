package com.example.hw2.view

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.hw2.R
import com.example.hw2.view.ImageState
import java.lang.Exception

class ViewHolder(
    view: View,
    private val onDelete: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    val text = view.findViewById<TextView>(R.id.text_1)
    val image = view.findViewById<ImageView>(R.id.image_view)
    val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
    private val deleteButton = view.findViewById<ImageButton>(R.id.delete_button)

    private var deferredUrl: DeferredHolder<String?>? = null
    var imageState = ImageState.EMPTY
        private set

    init {
        deleteButton.setOnClickListener {
            onDelete(adapterPosition)
        }
    }

    fun bind(url: DeferredHolder<String?>) {
        setStartState()
        deferredUrl = url
        deferredHandler(url.deferred)
    }



    fun onClick(url: Deferred<String?>) {
        deferredUrl?.let { it.deferred = url } ?: return

        setStartState()
        deferredHandler(url)
    }

    private fun deferredHandler(deferred: Deferred<String?>) {
        deferred.invokeOnCompletion { cause ->
            if (cause == null) {
                loadImage()
            } else {
                setFailState()
            }
        }
    }

    private fun setStartState() {
        imageState = ImageState.LOADING
        image.setImageDrawable(null)
        text.text = WAITING_MESSAGE
        progressBar.visibility = View.VISIBLE
    }

    private fun setSuccessState() {
        imageState = ImageState.LOADED
        text.text = SUCCESS_MESSAGE
        progressBar.visibility = View.INVISIBLE
    }

    private fun setFailState() {
        imageState = ImageState.EMPTY
        text.text = FAIL_MESSAGE
        progressBar.visibility = View.INVISIBLE
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadImage() {
        if (!deferredUrl!!.deferred.isCompleted || deferredUrl!!.deferred.isCancelled) {
            return
        }

        val url = deferredUrl!!.deferred.getCompleted()
        if (url == null) {
            setFailState()
            return
        }

        Picasso.get().load(deferredUrl!!.deferred.getCompleted()).into(image, object : Callback {
            override fun onSuccess() {
                setSuccessState()
            }

            override fun onError(e: Exception?) {
                setFailState()
            }
        })
    }

}

const val WAITING_MESSAGE = "Загружаем собачку"
const val FAIL_MESSAGE = "Что-то пошло не так:("
const val SUCCESS_MESSAGE = ""