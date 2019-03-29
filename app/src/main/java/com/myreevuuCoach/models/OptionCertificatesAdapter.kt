package com.myreevuuCoach.models

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myreevuuCoach.R
import com.myreevuuCoach.fragments.BaseKotlinFragment
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.interfaces.SelectOption
import kotlinx.android.synthetic.main.item_options.view.*

/**
 * Created by dev on 4/2/19.
 */

class OptionsCertificatesAdapter(var moptionsArray: ArrayList<DefaultArrayModel.ResponseBean.CertificatesBean>,
                                 var selectedOption: String, var fragment: BaseKotlinFragment,
                                 var enableMultipleOptions: Boolean)
    : RecyclerView.Adapter<OptionsCertificatesAdapter.ViewHolder>() {

    private val selectOption: SelectOption = fragment
    private var mSelectedMultipleOptions = ArrayList<String>()
    private var mSelectedMultipleOptionsId = ArrayList<Int>()

    constructor(moptionsArray: ArrayList<DefaultArrayModel.ResponseBean.CertificatesBean>, selectedOption: String,
                fragment: BaseKotlinFragment, enableMultipleOptions: Boolean,
                mSelectedMultipleOptions: ArrayList<String>, selectedMultipleOptionsIds: ArrayList<Int>)
            : this(moptionsArray, selectedOption, fragment, enableMultipleOptions) {
        this.mSelectedMultipleOptions = mSelectedMultipleOptions
        this.mSelectedMultipleOptionsId = selectedMultipleOptionsIds
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
            if (mSelectedMultipleOptionsId.contains(moptionsArray[position].id))
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
                if (mSelectedMultipleOptionsId.contains(moptionsArray[position].id)) {
                    for (i in 0 until mSelectedMultipleOptions.size) {
                        if (mSelectedMultipleOptions[i].startsWith(moptionsArray[position].name)) {
                            mSelectedMultipleOptions.removeAt(i)
                            mSelectedMultipleOptionsId.removeAt(i)
                            selectedOption = InterConst.DATA_REMOVED
                            selectOption.onOptionSelected(selectedOption, moptionsArray[position].id)
                            break
                        }
                    }
                } else {
                    mSelectedMultipleOptions.add(0, moptionsArray[position].name)
                    mSelectedMultipleOptionsId.add(0, moptionsArray[position].id)

                    selectedOption = moptionsArray[position].name
                    selectOption.onOptionSelected(selectedOption, moptionsArray[position].id)
                }

            } else {
                selectedOption = moptionsArray[position].name
                selectOption.onOptionSelected(selectedOption, moptionsArray[position].id)
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