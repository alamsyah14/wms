package com.datascrip.wms.widget

import com.datascrip.wms.core.model.OptionSelected
import com.datascrip.wms.core.util.Actions

object ActionWMS {

    val registerItems = listOf(
        OptionSelected(1, Actions.INSERT_COMMENT, "Insert Comment"),
        OptionSelected(2, Actions.REGISTER_RECEIPT, "Register Penerimaan"),
        OptionSelected(3, Actions.PRINT_REGISTER, "Print Register"),
        OptionSelected(4,Actions.REGISTER_RECEIPT_AND_PRINT, "Register & Register")
    )

    val putAway = listOf(
        OptionSelected(1, Actions.REGISTER_WHS_ACTIVITY_HEADER, "Register Whs. activity header")
    )

    val pick = listOf(
        OptionSelected(1, Actions.REGISTER_WHS_DOC, "Register Warehouse Document"),
        OptionSelected(2, Actions.REGISTER_WHS_AND_FINISH_PICK, "Register And Finish Pick")
    )

    val packingList = listOf(
        OptionSelected(1, Actions.REGISTER_PACKING_LIST, "Register Packing List"),
        OptionSelected(2, Actions.PRINT_PACKING_LIST, "Print"),
        OptionSelected(3, Actions.REGISTER_AND_PRINT_PACKING_LIST, "Register & Print")
    )

    val warehouseMovement = listOf(
        OptionSelected(1, Actions.REGISTER_WHS_MOVEMENT, "Register Whs. Movement")
    )

    val finishWarehousePick = listOf(
        OptionSelected(1, Actions.FINISH_WHS_PICK, "Finish Whs. Pick")
    )


}