package com.aloysius.dicoding.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.aloysius.dicoding.R
import com.google.android.material.textfield.TextInputLayout

class EmailEditText : AppCompatEditText {
    private lateinit var context: Context

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        this.context = context
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parent = parent.parent
                if (parent is TextInputLayout) {
                    validateEmail(s.toString(), parent)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateEmail(email: String, parent: TextInputLayout) {
        if (email.isEmpty()) {
            parent.error = context.getString(R.string.email_required)
            parent.isErrorEnabled = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            parent.error = context.getString(R.string.invalid_email_error)
            parent.isErrorEnabled = true
        } else {
            parent.error = null
            parent.isErrorEnabled = false
        }
    }
}