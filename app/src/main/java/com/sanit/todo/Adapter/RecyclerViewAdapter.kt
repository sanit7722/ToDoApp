package com.sanit.todo.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sanit.todo.Model.Notes
import com.sanit.todo.R

import java.util.ArrayList

class RecyclerViewAdapter internal constructor(private val notesArrayList: ArrayList<Notes>) : RecyclerView.Adapter<RecyclerViewAdapter.NotesHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NotesHolder {
        val inflatedView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_does, viewGroup, false)
        return NotesHolder(inflatedView)
    }

    override fun onBindViewHolder(notesHolder: NotesHolder, i: Int) {

        val currentNote = notesArrayList[i]
        notesHolder.tvTitle.text = currentNote.title
        notesHolder.tvSubtitle.text = currentNote.subtitle

    }

    override fun getItemCount(): Int {
        return notesArrayList.size
    }

    inner class NotesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView = itemView.findViewById(R.id.titledoes)
        var tvSubtitle: TextView = itemView.findViewById(R.id.descdoes)

    }

}
