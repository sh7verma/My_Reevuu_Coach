package com.myreevuuCoach.activities
import android.view.View
import com.myreevuuCoach.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_view_message.*

class FullViewMessageActivity : BaseActivity() {

    internal var mName = ""
    internal var mPic = ""
    internal var mMessage = ""

    override fun getContentView() = R.layout.activity_full_view_message

    override fun initUI() {

    }

    override fun onCreateStuff() {
        mName = intent.extras!!.getString("name")
        mPic = intent.extras!!.getString("pic")
        mMessage = intent.extras!!.getString("message")

//      txtName.setText(mName)
        txtMessage.text = mMessage
        Picasso.get().load(mPic).placeholder(R.drawable.placeholder_image).into(imgProfileAvatar)
    }

    override fun initListener() {
        imgBack.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(v: View?) {
        when (v) {
            imgBack -> {
                moveBack()
            }
        }
    }

    override fun onBackPressed() {
        moveBack()
    }

    private fun moveBack() {
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

}