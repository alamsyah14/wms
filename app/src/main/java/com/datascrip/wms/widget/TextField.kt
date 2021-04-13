package com.datascrip.wms.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.datascrip.wms.R
import com.datascrip.wms.core.extention.*
import com.datascrip.wms.core.util.InputFilterUtilities
import kotlinx.android.synthetic.main.text_field_view.view.*
import java.text.NumberFormat
import java.util.*

open class TextField: ConstraintLayout {

    enum class FieldType {
        TEXT,
        SEARCH,
        DROP_DOWN,
        DATE_PICKER,
        CURRENCY,
        PASSWORD,
        NOT_EDITABLE
    }

    var errorText = ""
        set(value) {
            field = value
            error_txt.text = field
        }

    var text: String
        set(value) {
            edit_txt?.setText(value)
        }
        get() = edit_txt.text?.toString() ?: ""

    var hintTitle: String
        set(value) {
            hint_txt?.text = value
        }
        get() = hint_txt?.text.toString()

    var hintField: String
        set(value) {
            edit_txt?.hint = value
        }
        get() = edit_txt?.hint?.toString() ?: ""

    //variable hold value last time user click (to prevent double click)
    var lastTimeClick = 0L

    private val prefixCurrency = "Rp"

    private var textWatcher: TextWatcher? = null
    private var oneTextFieldInputFilter = InputFilterUtilities.filterOneTextField

    private var isPasswordVisible = false

    private var maximumLength = Int.MAX_VALUE
        @SuppressLint("SetTextI18n")
        set(value) {
            field = value
            counter_txt.text = "${edit_txt.text.length}/$maximumLength"
        }

    private var showCounterText = false

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

    var allowAllCharacter = false
        set(isAllowed) {
            field = isAllowed
            if(isAllowed) {
                removeOneTextFieldCharInputFilter()
            }
        }

    @SuppressLint("SetTextI18n")
    protected fun init(attrs: AttributeSet? = null) {
        inflate(context, R.layout.text_field_view, this)
        setFieldType(FieldType.TEXT)
        var allCapsText = false
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.OneTextField).apply {
                try {
                    hint_txt.text = getString(R.styleable.OneTextField_titleHint)
                    edit_txt.hint = getString(R.styleable.OneTextField_fieldHint)
                    edit_txt.apply {
                        inputType = getInt(
                            R.styleable.OneTextField_android_inputType,
                            EditorInfo.TYPE_CLASS_TEXT
                        )
                        isFocusable = getBoolean(R.styleable.OneTextField_android_focusable, true)
                        allCapsText =
                            getBoolean(R.styleable.OneTextField_android_textAllCaps, false)
                    }
                    val fieldTypeValue = getInt(R.styleable.OneTextField_fieldType, 0)
                    setFieldType(FieldType.values()[fieldTypeValue])

                    val isShowLine = getBoolean(R.styleable.OneTextField_showLine, true)
                    showLine(isShowLine)

                    showCounterText = getBoolean(R.styleable.OneTextField_showCounter, false)
                    if (showCounterText) {
                        counter_txt.visible()
                        counter_txt.text = "${edit_txt.text.length}/$maximumLength"
                    } else {
                        counter_txt.invisible()
                    }
                } finally {
                    recycle()
                }
            }
        }

        if (allCapsText)
            addInputFilter(arrayOf(InputFilter.AllCaps()))

        addInputFilter(arrayOf(oneTextFieldInputFilter))
        edit_txt?.afterTextChanged {
            clearError()
        }
    }

    fun multiline() {
        edit_txt.isSingleLine = false
    }

    fun singleLine() {
        edit_txt.isSingleLine = true
    }

    fun removeTextWatcher() {
        textWatcher?.run {
            edit_txt?.removeTextChangedListener(textWatcher)
        }
    }

    fun afterTextChanged(callback: (String) -> Unit) {
        removeTextWatcher()
        textWatcher = object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                clearError()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (showCounterText) {
                    counter_txt.text = "${p0.toString().length}/$maximumLength"
                }
                callback(p0.toString())
            }
        }
        edit_txt?.addTextChangedListener(textWatcher)
    }

    /**
     * Set type of field, it should be SEARCH, DROP_DOWN, or TEXT. Default is text
     */
    fun setFieldType(type: FieldType) {
        when (type) {
            FieldType.SEARCH -> {
                setIconField(
                    R.drawable.ic_search
                )
                removeOneTextFieldCharInputFilter()
            }
            FieldType.DROP_DOWN -> {
                setIconField(R.drawable.ic_chevron_right_black)
                removeOneTextFieldCharInputFilter()
            }
            FieldType.DATE_PICKER -> {
                setIconField(R.drawable.ic_date)
                removeOneTextFieldCharInputFilter()
            }
            FieldType.CURRENCY -> {
                addCurrencyTextWatcher { } // default textWatcher
                removeOneTextFieldCharInputFilter()
            }

            FieldType.PASSWORD -> {
                setIconField(R.drawable.ic_eye_hidden)
                setupPasswordTransformation()
            }
            FieldType.NOT_EDITABLE -> {
                setIconField(null)
                setupNotEditableTranformation()
                allowEdit(false)
            }
            else -> setIconField(null)
        }
    }

    private fun setupNotEditableTranformation() {
        edit_txt?.setPadding(
            getDimensionPx(R.dimen.space_x1),
            getDimensionPx(R.dimen.space_x1),
            getDimensionPx(R.dimen.space_x1),
            getDimensionPx(R.dimen.space_x1)
        )
        edit_txt?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.default_background
            )
        )
        line_view?.setPadding(
            getDimensionPx(R.dimen.space_x0),
            getDimensionPx(R.dimen.space_x0),
            getDimensionPx(R.dimen.space_x0),
            getDimensionPx(R.dimen.space_x1)
        )
    }

    fun addCurrencyTextWatcher(initialValue: String = "", callback: (String) -> Unit) {
        val numberFormat = NumberFormat.getInstance(Locale("in", "ID"))

        setInputType(EditorInfo.TYPE_CLASS_NUMBER)
        text = prefixCurrency + initialValue
        setSelection(prefixCurrency.length)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val cleanString = text.replace(Regex("[\\D]"), "")
                if (cleanString.isEmpty()) {
                    setText("$prefixCurrency$cleanString", this)
                    callback(text)
                    return
                }
                val parsed = java.lang.Double.parseDouble(cleanString)
                val formatted = numberFormat.format(parsed)
                setText("$prefixCurrency$formatted", this)
                callback(text)

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        }
        addTextWatcher(textWatcher)
    }

    private fun setText(textCurrency: String, textWatcher: TextWatcher) {
        removeTextWatcher()
        text = textCurrency
        setSelection(text.length)
        addTextWatcher(textWatcher)
    }

    private fun getRawValue(): String {
        return text.replace(Regex("[\\D]"), "")
    }

    /**
     * Set drawable icon for edit text
     * Always add drawable in right of edit text
     */
    private fun setIconField(@DrawableRes drawableRes: Int?) {
        drawableRes?.let {
            val drawable = ContextCompat.getDrawable(context, it)
            edit_txt?.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        }

    }

    /**
     * Hide Failed text
     */
    fun hideError() {
        error_txt.text = ""
    }

    /**
     * Show line under edit text
     */
    fun showLine(isShow: Boolean) {
        line_view?.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    fun setInputType(inputType: Int) {
        if (EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS == inputType)
            removeOneTextFieldCharInputFilter()

        edit_txt.inputType = inputType or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
    }

    fun removeOneTextFieldCharInputFilter() {
        val currInputFilter = edit_txt?.filters?.toMutableList()
        currInputFilter?.remove(oneTextFieldInputFilter)
        edit_txt?.filters = currInputFilter?.toTypedArray()
    }

    /**
     * Set listener for the icon inside edit text. For now just action for icon search and drop down
     */
    @SuppressLint("ClickableViewAccessibility")
    fun setIconClickListener(listener: (View) -> Unit) {
        edit_txt?.setOnTouchListener { view, motionEvent ->
            // index drawable right in compoundDrawables.
            // 0 = left, 1 = top, 2 = right, 3 = bottom
            val drawableRight = 2
            val editText = view as EditText

            if (motionEvent.action != MotionEvent.ACTION_UP || editText.compoundDrawables[drawableRight] == null)
                return@setOnTouchListener false

            if (motionEvent.rawX >= (right - editText.compoundDrawables[drawableRight].bounds.width())) {
                listener(view)
                return@setOnTouchListener false
            }
            false

        }
    }

    /**
     * Set listener for  edit text. It will make edit text not focusable, why not focus? \n
     * Because by default edit text will trigger on focus change if isFocusable, so click listener will not called
     *
     */
    fun setClickListener(listener: (View) -> Unit) {
        if (edit_txt.isFocusable) edit_txt.isFocusable = false

        edit_txt.setOnClickListener {

            // Check if user double click field, 1s should be enough
            if (SystemClock.elapsedRealtime() - lastTimeClick < 1000)
                return@setOnClickListener

            listener(it)

            lastTimeClick = SystemClock.elapsedRealtime()
        }
    }

    /**
     * reset clickListener, esp. when you use it in recyclerView. When the x position previously the type was text, then
     * not text, and back to text again, we need to reset the focusable and the click listener
     */
    fun resetTextClickListener() {
        edit_txt?.isFocusable = true
        edit_txt?.isFocusableInTouchMode = true
        edit_txt?.setOnClickListener {}
    }

    fun allowEdit(isAllow: Boolean) {
        edit_txt?.apply {
            isFocusable = isAllow
            isClickable = isAllow
        }
    }

    @SuppressLint("SetTextI18n")
    fun setMaxLength(length: Int) {
        maximumLength = length
        addInputFilter(arrayOf(InputFilter.LengthFilter(length)))
    }

    fun addInputFilter(inputFilters: Array<InputFilter>) {
        val currInputFilter = edit_txt?.filters?.toMutableList()
        currInputFilter?.addAll(inputFilters)
        edit_txt?.filters = currInputFilter?.toTypedArray()
    }

    private fun clearError() {
        if (errorText.isNotBlank())
            errorText = ""
    }

    fun setSelection(index: Int) {
        edit_txt?.setSelection(index)
    }

    fun hideEditText() {
        edit_txt?.gone()
        showLine(false)
    }

    fun addTextWatcher(txtWatcher: TextWatcher) {
        if (textWatcher != null) removeTextWatcher()
        textWatcher = txtWatcher
        edit_txt?.addTextChangedListener(textWatcher)
    }

    private fun setupPasswordTransformation() {
        edit_txt.transformationMethod = PasswordTransformationMethod.getInstance()
        setIconClickListener {
            edit_txt.apply {
                transformationMethod = if (isPasswordVisible) {
                    setIconField(R.drawable.ic_eye_visible)
                    HideReturnsTransformationMethod.getInstance()
                } else {
                    setIconField(R.drawable.ic_eye_hidden)
                    PasswordTransformationMethod.getInstance()

                }
                invalidate()
            }

            isPasswordVisible = !isPasswordVisible
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun resetIconClickListener(){
        edit_txt.apply {
            setOnTouchListener { v, event -> true }
        }
    }

}