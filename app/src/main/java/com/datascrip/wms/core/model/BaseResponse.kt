package com.datascrip.wms.core.model

data class BaseResponse<T>(val response_code : String, var data : T?)