package com.datascrip.wms.core.util

class Constant {

    companion object {
        val KEY_STORE = "AndroidKeyStore"
    }

}

object ResponseCode{
    const val SUCCESS = "200"
}

object Actions {
    const val INSERT_COMMENT = "insert_comment"
    const val REGISTER_RECEIPT = "register_receipt"
    const val PRINT_REGISTER = "print_register"
    const val REGISTER_RECEIPT_AND_PRINT = "register_receipt_and_print"
    const val REGISTER_WHS_ACTIVITY_HEADER = "register_whs_activity_header"
    const val REGISTER_WHS_DOC = "register_whs_document"
    const val REGISTER_WHS_AND_FINISH_PICK = "register_whs_and_finish_pick"
    const val REGISTER_PACKING_LIST = "register_packing_list"
    const val PRINT_PACKING_LIST = "print_packing_list"
    const val REGISTER_AND_PRINT_PACKING_LIST = "register_and_print_packing_list"
    const val REGISTER_WHS_MOVEMENT = "register_whs_movement"
    const val FINISH_WHS_PICK = "finish_whs_pick"
}