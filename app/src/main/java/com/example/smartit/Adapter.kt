package com.example.smartit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_item_view.view.*

class Adapter(var items : ArrayList<Books>, var clickListner: OnItemClickListner) : RecyclerView.Adapter<ViewHolder>(){
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        lateinit var ViewHolder : ViewHolder
        ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_view,parent,false ))
        return ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.initialize(items.get(position),clickListner)

    }
}

class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
    var Name = itemView.name
    var Description = itemView.description
    var Logo = itemView.logo

    fun initialize(item: Books, action:OnItemClickListner){
        Name.text = item.name
        Description.text = item.description
        Logo.setImageResource(item.logo)

        itemView.setOnClickListener{
            action.onItemClick(item,adapterPosition)
        }

    }

}

interface OnItemClickListner{
    fun onItemClick(item: Books, position: Int)
}