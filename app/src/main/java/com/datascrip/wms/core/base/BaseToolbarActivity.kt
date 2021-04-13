package com.datascrip.wms.core.base

import com.datascrip.wms.R
import com.datascrip.wms.core.extention.*
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseToolbarActivity: BaseActivity(), ToolbarTitleListener {
    override fun getLayoutRes(): Int = R.layout.container_base_activity

    override fun setToolbarTitle(title: String) {
        toolbar_title_txt.text = title
    }

    override fun showToolbar(isShow: Boolean) {
        if (isShow) toolbar.visible() else toolbar.gone()
    }

    override fun showBackButton(isShow: Boolean) {
        if (isShow) img_back.visible() else img_back.invisible()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
            supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1].onResume()
        } else
            finish()
    }

    protected fun addFragment(fragment: BaseFragment, tag: String) {
        val fmt = supportFragmentManager.beginTransaction()
        fmt.add(R.id.base_frame_container, fragment, tag)
        if (supportFragmentManager.backStackEntryCount > 0) {
            fmt.hide(supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1])
        }

        fmt.addToBackStack(tag)

        fmt.commit()
    }

    override fun subscribeState() {}
}

interface ToolbarTitleListener {
    fun setToolbarTitle(title : String)
    fun showToolbar(isShow : Boolean)
    fun showBackButton(isShow: Boolean)
}