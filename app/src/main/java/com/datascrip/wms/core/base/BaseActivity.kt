package com.datascrip.wms.core.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.datascrip.wms.R

abstract class BaseActivity: AppCompatActivity() {
    abstract fun getLayoutRes(): Int
    abstract fun setupView()

    private var _action: () -> Unit = {}
    private var _onDismiss: () -> Unit = { onBackPressed() }

    private var _progressView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(getLayoutRes())
        setupView()
        subscribeState()
    }

    abstract fun subscribeState()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_CANCELED -> _onDismiss()
            else -> _action.invoke()
        }
    }

    fun showLoading(isShow: Boolean) {
        val viewGroup = window.decorView as ViewGroup
        if (_progressView != null) {
            viewGroup.removeView(_progressView)
        } else {
            _progressView = LayoutInflater.from(this).inflate(R.layout.loading_screen, viewGroup, false)
        }
        if (isShow) viewGroup.addView(_progressView)
        else {
            viewGroup.removeView(_progressView)
            _progressView = null
        }
    }

    fun activityRestart() {
        val intent = intent
        startActivity(intent)
        finish()
    }

}