package com.myreevuuCoach.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Rect
import android.graphics.Typeface
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.CreateProfileActivity
import com.myreevuuCoach.activities.OptionPhotoSelection
import com.myreevuuCoach.adapters.OptionsAdapter
import com.myreevuuCoach.models.OptionsModel
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.OnViewGlobalLayoutListener
import com.myreevuuCoach.utils.RoundCornersTransformation
import kotlinx.android.synthetic.main.activity_create_profile.*
import kotlinx.android.synthetic.main.dialog_options.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AccountInfoFragment : BaseKotlinFragment() {

    private var DOCUMENT = 0
    var optionSelected = 0

    var calDOB: Calendar = Calendar.getInstance(TimeZone.getDefault())
    var mShowStartDate = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    lateinit var optionDialog: BottomSheetDialog

    lateinit var mCreateProfileActivity: CreateProfileActivity
    var mPath: String = Constants.EMPTY

    lateinit var fragment: AccountInfoFragment

    override fun setContentView() = R.layout.fragment_accounts

    override fun initUI() {

    }

    override fun onActivityCreated() {
        mCreateProfileActivity = activity as CreateProfileActivity
        fragment = this
        val typeface = Typeface.createFromAsset(activity!!.assets, "regular.ttf")

        edRountingNo.typeface = typeface
        edFirstName.typeface = typeface
        edLastName.typeface = typeface
        edAccountNumber.typeface = typeface
        edAddress.typeface = typeface
        edCity.typeface = typeface
//        edState.typeface = typeface
        edPostalCode.typeface = typeface
        edSSN.typeface = typeface

//        svAccounts.viewTreeObserver.addOnGlobalLayoutListener {
//            val r = Rect()
//            svAccounts.getWindowVisibleDisplayFrame(r)
//            val heightDiff = svAccounts.rootView.height - (r.bottom - r.top)
//
//            if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
//                //ok now we know the keyboard is up...
//                mCreateProfileActivity.txtNEXTProfile.visibility = View.INVISIBLE
//            } else {
//                //ok now we know the keyboard is down...
//                mCreateProfileActivity.txtNEXTProfile.visibility = View.VISIBLE
//            }
//        }
    }

    override fun initListener() {
        llDOB.setOnClickListener(this)
        rlDocument.setOnClickListener(this)
        llState.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            llDOB -> {
                selectDOB()
            }
            rlDocument -> {
                val inProfile = Intent(activity, OptionPhotoSelection::class.java)
                startActivityForResult(inProfile, DOCUMENT)
                activity!!.overridePendingTransition(0, 0)
            }
            llState -> {
                optionSelected = 1
                showOptions(mCreateProfileActivity.mResponse.states as ArrayList<OptionsModel>, txtState.text.toString(),
                        getString(R.string.state))
            }
        }
    }

    override fun onOptionSelected(selectedOption: String, id: Int) {
        optionDialog.dismiss()
        when (optionSelected) {
            1 -> {
                txtState.text = selectedOption
            }
        }
    }

    private fun showOptions(optionsArray: ArrayList<OptionsModel>, selectedOption: String, title: String) {
        optionDialog = BottomSheetDialog(activity!!)
        optionDialog.setContentView(R.layout.dialog_options)

        val dialogParms = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT)
        dialogParms.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        val bottomSheet = optionDialog.window.findViewById(android.support.design.R.id.design_bottom_sheet) as FrameLayout
        bottomSheet.setBackgroundResource(R.drawable.white_default)
        bottomSheet.layoutParams = dialogParms
        bottomSheet.viewTreeObserver
                .addOnGlobalLayoutListener(OnViewGlobalLayoutListener(bottomSheet, Constants.dpToPx(300)))

        val txtOptionTitle = optionDialog.txtOptionTitle
        val rvOptions = optionDialog.rvOptions

        txtOptionTitle.text = title

        rvOptions.layoutManager = LinearLayoutManager(activity)
        rvOptions.adapter = OptionsAdapter(optionsArray, selectedOption, fragment, false)

        optionDialog.show()
    }


    private val dobPickerListener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
        calDOB.set(Calendar.YEAR, selectedYear)
        calDOB.set(Calendar.MONTH, selectedMonth)
        calDOB.set(Calendar.DATE, selectedDay)
        try {
            txtDOB.text = mShowStartDate.format(calDOB.time)
            mCreateProfileActivity.dob = txtDOB.text.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    fun selectDOB() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.add(Calendar.YEAR, -13)
        val datePickerDOB = DatePickerDialog(activity,
                R.style.DatePickerTheme,
                dobPickerListener,
                calDOB.get(Calendar.YEAR),
                calDOB.get(Calendar.MONTH),
                calDOB.get(Calendar.DAY_OF_MONTH))

        datePickerDOB.setCancelable(false)
        datePickerDOB.datePicker.maxDate = calendar.timeInMillis
        datePickerDOB.setTitle("")
        datePickerDOB.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                DOCUMENT -> {
                    mPath = data!!.getStringExtra("filePath")
                    mCreateProfileActivity.mFileDcoument = File(mPath)

                    val options = RequestOptions().transforms(MultiTransformation(CenterCrop(),
                            RoundCornersTransformation(activity, Constants.dpToPx(5), 0)))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    Glide.with(activity!!).load(mCreateProfileActivity.mFileDcoument)
                            .apply(options).into(imgDocument)
                }
            }
        super.onActivityResult(requestCode, resultCode, data)
    }
}