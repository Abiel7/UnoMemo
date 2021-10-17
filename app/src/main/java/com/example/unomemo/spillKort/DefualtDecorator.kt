package com.example.unomemo.spillKort

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DefualtDecorator(
        private val hSPacing: Int,
        private val vSpacing:Int
    ) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right =hSPacing
        outRect.left = hSPacing
        if(parent.getChildLayoutPosition(view) == 0){
            outRect.top= vSpacing
        }
        outRect.bottom = vSpacing
    }
}