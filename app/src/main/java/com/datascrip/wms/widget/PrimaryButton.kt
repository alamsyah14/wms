package com.datascrip.wms.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.datascrip.wms.R
import com.datascrip.wms.core.extention.*
import kotlinx.android.synthetic.main.primary_button_widget.view.*

enum class PrimaryButtonState {
    DISABLED,
    LOADING,
    ENABLED
}

open class PrimaryButton: ConstraintLayout {
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
        btn_view.setOnClickListener(l)
    }

    @SuppressLint("CustomViewStyleable")
    protected open fun init(attrs: AttributeSet? = null) {
        inflate(context, R.layout.primary_button_widget, this)
        attrs.let {
            context.obtainStyledAttributes(it, R.styleable.PrimaryButton).apply {
                try {
                    btn_text.text = getString(R.styleable.PrimaryButton_android_text)
                    btn_text.isAllCaps = getBoolean(R.styleable.PrimaryButton_android_textAllCaps, false)
                    btn_view.isEnabled = getBoolean(R.styleable.PrimaryButton_android_enabled, true)
                } finally {
                    recycle()
                }
            }
        }
    }

    var text: String = ""
        set(value) {
            btn_text.text = value
            field = value
        }
        get() = btn_text.text.toString()

    fun setState(state: PrimaryButtonState) {
        when (state) {
            PrimaryButtonState.DISABLED -> {
                btn_text.visible()
                btn_progress.invisible()
                btn_view.isEnabled = false
            }
            PrimaryButtonState.LOADING -> {
                btn_text.invisible()
                btn_progress.visible()
                btn_view.isEnabled = true
            }
            PrimaryButtonState.ENABLED -> {
                btn_text.visible()
                btn_progress.invisible()
                btn_view.isEnabled = true
            }
        }
    }

}