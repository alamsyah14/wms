package com.datascrip.wms.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatCheckedTextView
import com.datascrip.wms.R
import com.datascrip.wms.core.model.OptionSelected

class AnyAdapter: BaseAdapter() {
    private var listData = emptyList<Any>()

    fun loadData(list: List<Any>){
        listData = list
        notifyDataSetChanged()
    }

    fun getSelectedData(position: Int) : OptionSelected {
        return listData[position] as OptionSelected
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_action_wms, null, false)
        val textView = view.findViewById<AppCompatCheckedTextView>(R.id.itemAccountTitle)
        val checkItem = listData[position] as OptionSelected
        textView.text = checkItem.name

        return view
    }

    override fun getViewTypeCount(): Int  = 2

    override fun getItem(position: Int): Any = listData[position]

    override fun getItemId(position: Int): Long  = position.toLong()

    override fun getCount(): Int = listData.size
}