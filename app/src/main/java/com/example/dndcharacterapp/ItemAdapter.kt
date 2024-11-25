package com.example.dndcharacterapp

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private var cursor : Cursor, private val itemAdapterListener: ItemAdapterListener): RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    interface ItemAdapterListener {
        fun click(position: Int)
    }

    class ViewHolder(view: View, private val itemAdapterListener: ItemAdapterListener): RecyclerView.ViewHolder(view) {

        var characterID = 0

        init{
            view.setOnClickListener{
                itemAdapterListener.click(characterID)
            }
        }

        fun update(cursor: Cursor){
            cursor.moveToPosition(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_layout, parent, false)
        return ViewHolder(view, itemAdapterListener)
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.update(cursor)
    }

    fun updateCursor(cursor: Cursor)
    {
        this.cursor.close()
        this.cursor = cursor
        notifyDataSetChanged()
    }
}