package com.datascrip.wms.widget

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.datascrip.wms.R
import com.datascrip.wms.core.model.OptionSelected
import kotlinx.android.synthetic.main.dialog_bottom_select_action.view.*

object CustomBottomDialog {

    private var dlg: BottomSheetDialog? = null
    private fun dismiss() = dlg?.dismiss()

    private fun createDialog(ctx: Context, layoutID: Int){

        val layoutInflater = (ctx as Activity).layoutInflater
        val dlgView = layoutInflater.inflate(layoutID, null)

        dlg = BottomSheetDialog(ctx, R.style.BottomDialogStyle)

        dlg?.apply {
            setContentView(dlgView)
            setCanceledOnTouchOutside(false)
            setOnShowListener {
                val dialog = it as BottomSheetDialog
                val bottomSheet= dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                BottomSheetBehavior.from<FrameLayout>(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.from<FrameLayout>(bottomSheet).skipCollapsed = true
                BottomSheetBehavior.from<FrameLayout>(bottomSheet).setHideable(true)
            }
        }
    }

    private fun createDialog(ctx: Context, view: View){
        dlg = BottomSheetDialog(ctx, R.style.BottomDialogStyle)
        dlg?.apply {
            setContentView(view)
            setCanceledOnTouchOutside(false)
            setOnShowListener {
                val dialog = it as BottomSheetDialog
                val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                BottomSheetBehavior.from<FrameLayout>(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.from<FrameLayout>(bottomSheet).skipCollapsed = true
            }
        }
    }

    fun showActionWMS(context: Context, container: ViewGroup, title:String, list: List<Any>,
                      adapter: AnyAdapter = AnyAdapter(),
                      applyBtnListener : (OptionSelected) -> Unit) {

        if (dlg?.isShowing == true) { return }

        adapter.loadData(list)
        val layoutInflater = (context as Activity).layoutInflater
        val binding = layoutInflater.inflate(R.layout.dialog_bottom_select_action, container,false)
        binding.apply {
            listView.adapter = adapter
            dlgClose.setOnClickListener { dismiss() }
            listView.setOnItemClickListener { _, _, position, _ ->
                dismiss()
                applyBtnListener(adapter.getSelectedData(position))
            }
            dlgTitle.text = title
        }
        createDialog(context, binding)
        dlg?.show()
    }
}