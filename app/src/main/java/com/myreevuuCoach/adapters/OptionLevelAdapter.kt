package com.myreevuuCoach.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myreevuuCoach.R
import com.myreevuuCoach.fragments.BaseKotlinFragment
import com.myreevuuCoach.interfaces.SelectOption
import com.myreevuuCoach.models.DefaultArrayModel
import kotlinx.android.synthetic.main.item_options.view.*

/**
 * Created by dev on 4/2/19.
 */

class OptionLevelAdapter(var moptionsArray: ArrayList<DefaultArrayModel.ResponseBean.LevelsBean>,
                         var selectedOption: String, var fragment: BaseKotlinFragment,
                         var enableMultipleOptions: Boolean)
    : RecyclerView.Adapter<OptionLevelAdapter.ViewHolder>() {

    private val selectOption: SelectOption = fragment
    private var mSelectedMultipleOptions = ArrayList<String>()

    constructor(moptionsArray: ArrayList<DefaultArrayModel.ResponseBean.LevelsBean>, selectedOption: String,
                fragment: BaseKotlinFragment, enableMultipleOptions: Boolean,
                mSelectedMultipleOptions: ArrayList<String>)
            : this(moptionsArray, selectedOption, fragment, enableMultipleOptions) {
        this.mSelectedMultipleOptions = mSelectedMultipleOptions
    }

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

        if (enableMultipleOptions) {
            if (mSelectedMultipleOptions.contains(moptionsArray[position].name))
                holder.imgOption.setImageResource(R.mipmap.ic_tick)
            else
                holder.imgOption.setImageResource(R.mipmap.ic_circle_unselected)
        } else {
            if (moptionsArray[position].name == selectedOption)
                holder.imgOption.visibility = View.VISIBLE
            else
                holder.imgOption.visibility = View.INVISIBLE
        }

        holder.llOption.setOnClickListener {
            if (enableMultipleOptions) {
                if (mSelectedMultipleOptions.contains(moptionsArray[position].name))
                    mSelectedMultipleOptions.remove(moptionsArray[position].name)
                else
                    mSelectedMultipleOptions.add(0,moptionsArray[position].name)
            } else {
                selectedOption = moptionsArray[position].name
                selectOption.onOptionSelected(selectedOption,moptionsArray[position].id)
            }
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