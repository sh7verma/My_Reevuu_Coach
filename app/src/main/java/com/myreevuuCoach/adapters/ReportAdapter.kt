package com.myreevuuCoach.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.FeedDetailActivity
import com.myreevuuCoach.models.OptionsModel
import kotlinx.android.synthetic.main.item_options.view.*

class ReportAdapter(var moptionsArray: ArrayList<OptionsModel>
                    , val mContext: Context)
    : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vhItem: ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_options, parent,
                false)
        vhItem = ViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtOption.text = moptionsArray[position].name

        if (moptionsArray[position].name == (mContext as FeedDetailActivity).reportReason)
            holder.imgOption.visibility = View.VISIBLE
        else
            holder.imgOption.visibility = View.INVISIBLE

        holder.llOption.setOnClickListener {
            (mContext as FeedDetailActivity).reportReason = moptionsArray[position].name
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