package com.datascrip.wms.feature.entrance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.datascrip.wms.core.base.BaseViewModel
import com.datascrip.wms.core.util.DeviceIdHelper
import com.datascrip.wms.core.util.StateWrapper
import kotlinx.coroutines.launch

class EntranceVM (private val deviceIdHelper: DeviceIdHelper) :  BaseViewModel() {

    sealed class Event {
        object OnCreate : Event()
    }

    sealed class State {
        data class OpenNextPage(val deviceId: String) : State()
        data class ShowError(val errMessage: String) : State()
    }

    private val _state = MutableLiveData<StateWrapper<State>>()
    val state: LiveData<StateWrapper<State>> = _state

    private lateinit var _deviceId: String

    fun onEvent(event: Event) {
        when (event) {
            Event.OnCreate -> handleOnCreate()
        }
    }

    private fun handleOnCreate() = launch {
        _deviceId = deviceIdHelper.getCurrentDeviceId()
        if (_deviceId.isNullOrEmpty()) {
            setState(State.ShowError(""))
            return@launch
        }

        setState(State.OpenNextPage(_deviceId))
    }

    private fun setState(state: State) {
        _state.value = StateWrapper(state)
    }

}