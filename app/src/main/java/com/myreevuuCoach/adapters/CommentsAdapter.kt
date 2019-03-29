package com.myreevuuAthlete.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.CommentActivity

import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.CommentModel
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.RoundedTransformation
import com.myreevuuCoach.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_comments.view.*
import java.util.*

class CommentsAdapter(mConetxt: Context, mCommentArray: ArrayList<CommentModel.ResponseBean>, mComments:
CommentActivity) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    var mCommentArray = ArrayList<CommentModel.ResponseBean>()
    var mContext: Context? = null
    var mUtils: Utils? = null
    var mComments: CommentActivity? = null
    protected var mWidth: Int = 0
    protected var mHeight: Int = 0
    protected var myUserID: Int = 0


    init {
        this.mCommentArray = mCommentArray
        this.mContext = mConetxt
        mUtils = Utils(mContext)
        this.mComments = mComments
        mWidth = Utils(mContext).getInt("width", mWidth)
        mHeight = Utils(mContext).getInt("height", mHeight)
        myUserID = mUtils!!.getInt(InterConst.ID, -1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vhItem: ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_comments, parent, false)
        vhItem = ViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
                .load(mCommentArray[position].profile_pic)
                .transform(RoundedTransformation(mHeight * 10, 0))
                .resize((mHeight * 0.125).toInt(), (mHeight * 0.125).toInt())
                .placeholder(R.mipmap.ic_profile)
                .centerCrop(Gravity.TOP)
                .error(R.mipmap.ic_profile).into(holder.imgComments)
        holder.txtNameComments.text = mCommentArray[position].name
        holder.txtMessageComments.text = mCommentArray[position].comment
        /*if (mCommentArray[position].id != 0)
            holder.txtTimeComments.text = mUtils!!.convertTime(mCommentArray[position].created_at)
        else
            holder.txtTimeComments.text = mCommentArray[position].created_at
*/
        // holder.txtTimeComments.text = mUtils!!.convertTime(mCommentArray[position].created_at)
        holder.txtTimeComments.text = Constants.Companion.displayDateTime(mCommentArray[position].created_at)

        // holder.txtTimeComments.text = mUtils!!.convertTime(mCommentArray[position].created_at)

        if (mCommentArray.get(position).user_id.toString().equals(myUserID)) {
            holder.imgDeleteComment.visibility = View.VISIBLE
        } else {
            holder.imgDeleteComment.visibility = View.GONE
        }

        holder.imgDeleteComment.setOnClickListener {
            // mComments!!.deleteComment(holder.adapterPosition)
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            if (mCommentArray[position].user_id == myUserID)
                mComments!!.deleteComment(holder.adapterPosition, mCommentArray[position].id)

            true
        })

        if (mCommentArray[position].isSelected) {
            holder.rlBackround.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.gray))
        } else {
            holder.rlBackround.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.black))
        }

    }

    override fun getItemCount() = mCommentArray.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llClickComments = itemView.llClickComments!!
        val imgComments = itemView.imgComments!!
        val txtNameComments = itemView.txtNameComments!!
        val imgDeleteComment = itemView.img_delete_comment!!
        val txtMessageComments = itemView.txtMessageComments!!
        val txtTimeComments = itemView.txtTimeComments!!
        val viewForeground = itemView.view_forground!!
        val rlBackround = itemView.rlBackround!!

        init {

        }
    }

    fun removeItem(position: Int) {
        mCommentArray.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }
}