package com.example.businesscards.core.coreui.overlappingrecyclerview

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OverlappingItemDecoration(private val pos: () -> Int?) : RecyclerView.ItemDecoration() {
    private val overlapValue = -130
    override fun getItemOffsets(outRect : Rect, view : View, parent : RecyclerView, state : RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) return
        if (pos() != parent.getChildAdapterPosition(view) || pos() == null) {
            outRect.set(0, 0, 0, overlapValue.dpToPx(parent.context))
        }
    }
}

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}