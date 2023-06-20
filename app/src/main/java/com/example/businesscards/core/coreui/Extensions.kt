package com.example.businesscards.core.coreui

import android.view.View

fun visible(vararg views: View?) {
    for (view in views) {
        view?.visibility = View.VISIBLE
    }
}

fun visible(view: View?) {
    view?.visibility = View.VISIBLE
}

fun invisible(vararg views: View) {
    for (view in views) {
        view.visibility = View.INVISIBLE
    }
}

fun invisible(view: View) {
    view.visibility = View.INVISIBLE
}

fun gone(vararg views: View?) {
    for (view in views) {
        view?.visibility = View.GONE
    }
}

fun gone(view: View?) {
    view?.visibility = View.GONE
}
