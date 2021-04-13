package com.datascrip.wms.feature.entrance

import android.os.Handler
import com.datascrip.wms.R
import com.datascrip.wms.core.base.BaseActivity
import com.datascrip.wms.core.extention.subscribeSingleState
import com.datascrip.wms.feature.login.LoginActivity
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class EntranceActivity: BaseActivity() {

    private val vm:EntranceVM by viewModel()
    override fun getLayoutRes(): Int = R.layout.entrance_activity
    override fun setupView() {
        vm.onEvent(EntranceVM.Event.OnCreate)
    }

    override fun subscribeState() {
        subscribeSingleState(vm.state){
            when(it) {
                is EntranceVM.State.OpenNextPage    -> handleOpenLoginPages()
                else                                -> finish()
            }
        }
    }

    private fun handleOpenLoginPages() {
        Handler().postDelayed({
            startActivity<LoginActivity>()
            finish()
        }, 3500)
    }

    private fun handleOpenMainMenu() {}

}