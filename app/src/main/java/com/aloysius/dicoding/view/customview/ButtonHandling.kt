package com.aloysius.dicoding.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.aloysius.dicoding.R

class ButtonHandling : AppCompatButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var txtColor: Int = ContextCompat.getColor(context, android.R.color.background_light)
    private var enabledBackground: Drawable = ContextCompat.getDrawable(context, R.drawable.bg_button)!!
    private var disabledBackground: Drawable = ContextCompat.getDrawable(context, R.drawable.bg_button_disable)!!

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        gravity = Gravity.CENTER
    }
}
