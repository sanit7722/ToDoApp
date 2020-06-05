package com.sanit.todo.Adapter

import android.content.Context
import android.icu.text.Transliterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sanit.todo.R
import com.sanit.todo.ToDoDAO
import com.sanit.todo.ToDoStore
import kotlinx.android.synthetic.main.item_does.view.*

class ToDoAdapter(val items: List<ToDoStore>, val context: Context) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todotitle.text = items.get(position).todotitle
        holder.tododesc.text = items.get(position).tododesc
        holder.tododate.text = items.get(position).tododate
        holder.id = items.get(position).id
        var todoStore: ToDoStore
        holder.imgdelete.setOnClickListener {
            var id: Long?
            id = items.get(position).id
            Toast.makeText(context, items.get(position).id.toString(), Toast.LENGTH_LONG).show()


        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todotitle = view.titledoes
        val tododesc = view.descdoes
        val tododate = view.datedoes
        val imgdelete = view.deletebtn
        var id: Long? = null

    }
}