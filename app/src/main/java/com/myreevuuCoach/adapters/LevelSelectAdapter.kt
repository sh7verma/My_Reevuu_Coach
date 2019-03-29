package com.myreevuuCoach.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.AdapterClickInterface
import kotlinx.android.synthetic.main.item_options.view.*

/**
 * Created by dev on 5/2/19.
 */

class LevelSelectAdapter(var moptionsArray: ArrayList<String>)
    : RecyclerView.Adapter<LevelSelectAdapter.ViewHolder>() {
    private var itemClick: AdapterClickInterface? = null

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vhItem: ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_options, parent,
                false)
        vhItem = ViewHolder(v)
        return vhItem
    }

    fun onAdapterItemClick(click: AdapterClickInterface) {
        itemClick = click
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtOption.text = moptionsArray[position]

        holder.imgOption.visibility = View.INVISIBLE

        holder.llOption.setOnClickListener {
            itemClick!!.onItemClick(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return moptionsArray.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llOption = itemView.llOption!!
        val txtOption = itemView.txtOption!!
        val imgOption = itemView.imgOption!!
    }
}