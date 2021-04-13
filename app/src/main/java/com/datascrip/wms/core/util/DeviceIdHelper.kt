package com.datascrip.wms.core.util

import java.util.*

class DeviceIdHelper(private val localFileHelper: LocalFileHelper)  {

    private val deviceFileName = "device_id.txt"
    private val LENGTH_DEVICE_ID = 16

    private companion object {
        var deviceId = ""
        var isExistDeviceId = false
    }

    private fun saveOrCreateDeviceId(): String {
        val uniqueId = UUID.randomUUID().toString().take(LENGTH_DEVICE_ID)
        localFileHelper.saveFile(deviceFileName, uniqueId)
        return uniqueId
    }

    fun getCurrentDeviceId(): String {
        if (deviceId.isNotBlank()) return deviceId

        if (!isExistDeviceId) {
            isExistDeviceId =
                localFileHelper.loadFile(deviceFileName).readContentFile().isNotBlank()
        }

        return when {
            deviceId.isBlank() && !isExistDeviceId -> {
                deviceId = saveOrCreateDeviceId()
                deviceId
            }

            deviceId.isBlank() -> {
                deviceId = localFileHelper.loadFile(deviceFileName).readContentFile()
                deviceId
            }
            else -> deviceId
        }

    }


}