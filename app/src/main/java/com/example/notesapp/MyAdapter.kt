package com.example.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val onNotesClick: (id: Int) -> Unit,
    private val onNotesDelete: (id: Int) -> Unit
) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var notes = arrayListOf<Notes>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.notes_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = notes[position]
        holder.description.text = data.desc
        holder.createdAt.text = (data.createdAt.toString())
        holder.itemView.setOnClickListener {
            onNotesClick.invoke(position)
        }
        holder.deleteButton.setOnClickListener {
            onNotesDelete.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.title)
        val createdAt: TextView = itemView.findViewById(R.id.date)
        val deleteButton: Button = itemView.findViewById(R.id.delete)
    }
}