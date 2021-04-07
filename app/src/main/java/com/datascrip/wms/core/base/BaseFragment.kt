package com.datascrip.wms.core.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    abstract fun getToolbarTitle(): String
    abstract fun getLayoutRes(): Int
    abstract fun showToolbar(): Boolean
    abstract fun setupView(view: View)
    abstract fun showBackButton(): Boolean

    private var _action: () -> Unit = {}
    private var _onDismiss: () -> Unit = { activity?.onBackPressed() }

    private lateinit var container: ViewGroup

    private lateinit var toolbarTitleListener: ToolbarTitleListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseToolbarActivity) {
            throw IllegalArgumentException("Activity must be BaseToolbarActivity")
        }
        toolbarTitleListener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {
            this.container = it
        }
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.background = ContextCompat.getDrawable(view.context, R.color.default_background)
        view.isClickable = true
        setupView(view)
        subscribeState()
        toolbarTitleListener.setToolbarTitle(getToolbarTitle())
        toolbarTitleListener.showToolbar(showToolbar())
        toolbarTitleListener.showBackButton(showBackButton())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_CANCELED -> _onDismiss()
            else -> _action.invoke()
        }
    }

    override fun onResume() {
        super.onResume()
        toolbarTitleListener.setToolbarTitle(getToolbarTitle())
    }

    abstract fun subscribeState()

    open fun showLoading(isShow: Boolean) {
        val baseActivity = activity as BaseActivity
        baseActivity.showLoading(isShow)
    }

    fun activityRestart() {
        val intent = activity?.intent
        activity?.finish()
        startActivity(intent)
    }

    fun changeTitleToolbar(title:String) {
        toolbarTitleListener.setToolbarTitle(title)
    }

}