package com.takehomeproject.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.takehomeproject.R


fun EditText.hideKeyboardOnClick() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
                .error(R.drawable.ic_launcher_foreground) // Error image in case of loading failure
        )
        .into(this)
}