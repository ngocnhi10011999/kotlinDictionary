package com.example.kotlindictionary

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

class SearchListAdapter(context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0){

    private class ViewHolder {
        var txtWord: TextView? = null
        var txtDescription: TextView? = null
        var txtPronounce: TextView? = null

        var wordColumnIndex: Int = 0
        var descriptionColumnIndex: Int = 0
        var pronounceColumnIndex: Int = 0
    }
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val newView = layoutInflater.inflate(R.layout.search_list_item, parent, false)

        val viewHolder = ViewHolder()
        viewHolder.txtWord = newView.findViewById<TextView>(R.id.txtWord)
        viewHolder.txtDescription = newView.findViewById<TextView>(R.id.txtDescription)
        viewHolder.txtPronounce = newView.findViewById<TextView>(R.id.txtPronounce)

        return newView
    }

    override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {
        TODO("Not yet implemented")
    }
}