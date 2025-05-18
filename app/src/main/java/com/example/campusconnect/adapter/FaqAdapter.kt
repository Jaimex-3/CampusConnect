package com.example.campusconnect.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.campusconnect.R

class FaqAdapter(
    private val context: Context,
    private val titles: List<String>,
    private val content: Map<String, List<String>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int = titles.size

    override fun getChildrenCount(groupPosition: Int): Int =
        content[titles[groupPosition]]?.size ?: 0

    override fun getGroup(groupPosition: Int): Any = titles[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Any =
        content[titles[groupPosition]]?.get(childPosition) ?: ""

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getGroup(groupPosition) as String
        textView.setTextColor(ContextCompat.getColor(context, R.color.textPrimary))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        textView.setTypeface(null, Typeface.BOLD)
        textView.setPadding(48, 24, 48, 24)
        return view
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getChild(groupPosition, childPosition) as String
        textView.setTextColor(ContextCompat.getColor(context, R.color.textSecondary))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.setPadding(64, 16, 64, 16)
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = false
}
