package com.datascrip.wms.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.datascrip.wms.R
import com.datascrip.wms.core.extention.*
import kotlinx.android.synthetic.main.error_view.view.*

open class ErrorView: ConstraintLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        primary_button.setOnClickListener(l)
        secondary_button.setOnClickListener(l)
    }

    var errorMessage: String = ""
        get() = tv_error_message.text.toString()
        set(value) {
            tv_error_message.text = value
            field = value
        }

    @SuppressLint("CustomViewStyleable")
    protected open fun init(attrs: AttributeSet? = null) {
        inflate(context, R.layout.error_view, this)
        attrs.let {
            context.obtainStyledAttributes(it, R.styleable.ErrorView).apply {
                try {
                    tv_error_message.text = getString(R.styleable.ErrorView_errorMessage)

                    if (getBoolean(R.styleable.ErrorView_isTransparentMode, false)) {
                        primary_button.invisible()
                        secondary_button.visible()
                        tv_error_message.setTextColor(context.getColor(R.color.white))
                    } else {
                        primary_button.visible()
                        secondary_button.invisible()
                        tv_error_message.setTextColor(context.getColor(R.color.black))
                    }
                } finally {
                    recycle()
                }
            }
        }
    }
}