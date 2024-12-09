package com.example.testapp.common

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

class CustomLinearLayout(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    //If set to false, the children are clickable. If set to true, they are not.
    private var mDisableChildrenTouchEvents = true
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mDisableChildrenTouchEvents
    }

    fun setDisableChildrenTouchEvents(flag: Boolean) {
        mDisableChildrenTouchEvents = flag
    }
}