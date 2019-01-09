package com.myreevuuCoach.fragments

import android.graphics.Typeface
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.RegisterCoachActivity
import com.myreevuuCoach.adapters.OptionsAdapter
import com.myreevuuCoach.customViews.FlowLayout
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.OptionsModel
import com.myreevuuCoach.models.SignUpModel
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.OnViewGlobalLayoutListener
import kotlinx.android.synthetic.main.dialog_options.*
import kotlinx.android.synthetic.main.fragment_coach_info.*
import kotlinx.android.synthetic.main.layout_certificates.view.*
import kotlinx.android.synthetic.main.layout_expertise.view.*

class CoachInfoFragment : BaseKotlinFragment() {

    private lateinit var fragment: CoachInfoFragment
    private lateinit var optionDialog: BottomSheetDialog
    private var optionSelected = 0

    lateinit var mRegisterCoachActivity: RegisterCoachActivity

    override fun setContentView() = R.layout.fragment_coach_info

    var experienceDataArray = ArrayList<OptionsModel>()
    var levelsArray = ArrayList<OptionsModel>()
    var expertiesArray = ArrayList<OptionsModel>()
    var certificatesArray = ArrayList<OptionsModel>()

    override fun initUI() {

    }

    override fun onActivityCreated() {
        mRegisterCoachActivity = activity as RegisterCoachActivity
        fragment = this

        val typeface = Typeface.createFromAsset(activity!!.assets, "regular.ttf")
        edAboutYou.typeface = typeface
        edOtherCertificates.typeface = typeface

        setPrefilledData()
    }

    override fun initListener() {
        llHighestLevelCoached.setOnClickListener(this)
        llCoachExperience.setOnClickListener(this)
        llCoachCertificate.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            llHighestLevelCoached -> {
                optionSelected = 1
                showOptions(levelsArray as ArrayList<OptionsModel>,
                        txtHightestLevelCoached.text.toString(),
                        getString(R.string.highest_level_you_completed_in), false)
            }
            llCoachExperience -> {
                optionSelected = 2
                showOptions(experienceDataArray, txtCoachExperience.text.toString(),
                        getString(R.string.years_coaching_experience), false)
            }
            llCoachCertificate -> {
                showOptions(mRegisterCoachActivity.mResponse.certificates as ArrayList<OptionsModel>,
                        txtCoachExperience.text.toString(),
                        getString(R.string.select_coaching_certificate), true)
            }
        }
    }

    fun clearCoachData() {
        experienceDataArray.clear()
        levelsArray.clear()
        expertiesArray.clear()
        certificatesArray.clear()

        mRegisterCoachActivity.mResponse = mGson.fromJson(utils.getString("response", ""), SignUpModel::class.java)

        experienceDataArray.addAll(Constants.experienceData())
        levelsArray.addAll(mRegisterCoachActivity.mResponse.levels)
        expertiesArray.addAll(mRegisterCoachActivity.mResponse.experties)
        certificatesArray.addAll(mRegisterCoachActivity.mResponse.certificates)

        flCertificates.removeAllViews()
        flExpertise.removeAllViews()
        mRegisterCoachActivity.mSelectedCertificateArray.clear()
        mRegisterCoachActivity.mSelectedExpertiseArray.clear()

        mRegisterCoachActivity.coachLevelId = 0
        mRegisterCoachActivity.coachExperience = 0

        txtCoachExperience.text = ""
        txtHightestLevelCoached.text = ""
        edAboutYou.setText("")
        edOtherCertificates.setText("")

        for (expertise in expertiesArray)
            flExpertise.addView(inflateExpertiseView(expertise))
    }

    fun setPrefilledData() {

        experienceDataArray.addAll(Constants.experienceData())
        levelsArray.addAll(mRegisterCoachActivity.mResponse.levels)
        expertiesArray.addAll(mRegisterCoachActivity.mResponse.experties)
        certificatesArray.addAll(mRegisterCoachActivity.mResponse.certificates)

        if (mRegisterCoachActivity.mResponse.response.coach_info.about != null) {
            for (j in certificatesArray.indices) {
                for (i in mRegisterCoachActivity.mResponse.response.coach_info.certificates.indices) {
                    if (certificatesArray[j].name.equals(mRegisterCoachActivity.mResponse.response.coach_info.certificates[i].name)) {
                        certificatesArray[j].isSelected = true
                    }
                }
            }

            for (i in mRegisterCoachActivity.mResponse.response.coach_info.certificates.indices) {
                mRegisterCoachActivity.mSelectedCertificateArray.add(mRegisterCoachActivity.mResponse.response.coach_info.certificates[i].name)
            }

            displaySelectedCertificates()

            for (j in expertiesArray.indices) {
                for (i in mRegisterCoachActivity.mResponse.response.coach_info.experties.indices) {
                    if (expertiesArray[j].id == mRegisterCoachActivity.mResponse.response.coach_info.experties[i].id) {
                        expertiesArray[j].isSelected = true
                    }
                }
            }

            for (j in levelsArray.indices) {
                if (mRegisterCoachActivity.mResponse.response.coach_info.coach_level == levelsArray[j].id) {
                    levelsArray[j].isSelected = true
                    txtHightestLevelCoached.text = levelsArray[j].name
                }
            }

            for (j in levelsArray.indices) {
                if (mRegisterCoachActivity.mResponse.response.coach_info.coach_level == levelsArray[j].id) {
                    levelsArray[j].isSelected = true
                    txtHightestLevelCoached.text = levelsArray[j].name
                    mRegisterCoachActivity.coachLevelId = levelsArray[j].id
                }
            }
            if (!TextUtils.isEmpty(mRegisterCoachActivity.mResponse.response.coach_info.about)) {
                edAboutYou.setText(mRegisterCoachActivity.mResponse.response.coach_info.about)
            }

            for (j in experienceDataArray.indices) {
                var experence: Int

                if (experienceDataArray[j].name.startsWith("<")) {

                    var string: String
                    string = experienceDataArray[j].name.removePrefix("< ")
                    experence = string.removeSuffix(" Year").toInt()
                } else if (experienceDataArray[j].name.equals("1 Year")) {
                    experence = experienceDataArray[j].name.removeSuffix(" Year").toInt()
                } else {
                    experence = experienceDataArray[j].name.removeSuffix(" Years").toInt()
                }

                if (mRegisterCoachActivity.mResponse.response.coach_info.coach_experience == 0) {
                    experienceDataArray[0].isSelected = true
                    txtCoachExperience.text = experienceDataArray[0].name
                    mRegisterCoachActivity.coachExperience = experienceDataArray[0].id

                } else if (experence == mRegisterCoachActivity.mResponse.response.coach_info
                                .coach_experience) {
                    experienceDataArray[j].isSelected = true
                    txtCoachExperience.text = experienceDataArray[j].name
                    mRegisterCoachActivity.coachExperience = experienceDataArray[j].id
                }
            }
        }

        for (expertise in expertiesArray)
            flExpertise.addView(inflateExpertiseView(expertise))

    }


    private fun showOptions(optionsArray: ArrayList<OptionsModel>, selectedOption: String, title: String,
                            enableMultipleSelection: Boolean) {
        optionDialog = BottomSheetDialog(activity!!)
        optionDialog.setContentView(R.layout.dialog_options)

        val dialogParms = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT)
        dialogParms.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        val bottomSheet = optionDialog.window.findViewById(android.support.design.R.id.design_bottom_sheet) as FrameLayout
        bottomSheet.setBackgroundResource(R.drawable.white_default)
        bottomSheet.layoutParams = dialogParms
        bottomSheet.viewTreeObserver
                .addOnGlobalLayoutListener(OnViewGlobalLayoutListener(bottomSheet, Constants.dpToPx(400)))

        val txtOptionTitle = optionDialog.txtOptionTitle
        val rvOptions = optionDialog.rvOptions
        val txtDONE = optionDialog.txtDone

        txtOptionTitle.text = title

        rvOptions.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?

        if (enableMultipleSelection) {
            rvOptions.adapter = OptionsAdapter(optionsArray, selectedOption, fragment, true,
                    mRegisterCoachActivity.mSelectedCertificateArray)
            txtDONE.visibility = View.VISIBLE
        } else
            rvOptions.adapter = OptionsAdapter(optionsArray, selectedOption, fragment, false)

        txtDONE.setOnClickListener {
            // form chips here
            optionDialog.dismiss()
            displaySelectedCertificates()
        }

        optionDialog.show()
    }

    private fun displaySelectedCertificates() {
        if (mRegisterCoachActivity.mSelectedCertificateArray.isNotEmpty()) {
            flCertificates.removeAllViews()
            if (!mRegisterCoachActivity.mSelectedCertificateArray.contains("add more"))
                mRegisterCoachActivity.mSelectedCertificateArray.add("add more")

            txtCoachCertificate.visibility = View.GONE
            flCertificates.visibility = View.VISIBLE

            for ((index, certificates) in mRegisterCoachActivity.mSelectedCertificateArray.withIndex()) {
                if (index == mRegisterCoachActivity.mSelectedCertificateArray.size - 1)
                    flCertificates.addView(inflateCertificateView(certificates, true))
                else
                    flCertificates.addView(inflateCertificateView(certificates, false))
            }
        } else {
            txtCoachCertificate.visibility = View.VISIBLE
            flCertificates.visibility = View.GONE
        }
    }

    override fun onOptionSelected(selectedOption: String, id: Int) {
        optionDialog.dismiss()
        when (optionSelected) {
            1 -> {
                mRegisterCoachActivity.coachLevelId = id
                txtHightestLevelCoached.text = selectedOption
            }
            2 -> {
                mRegisterCoachActivity.coachExperience = id
                txtCoachExperience.text = selectedOption
            }
        }
    }

    private fun inflateCertificateView(certificate: String, visibleLastElement: Boolean): View {
        val certificateChip = LayoutInflater.from(activity).inflate(R.layout.layout_certificates, null, false)

        val innerParms = FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        certificateChip.llMainCertificates.layoutParams = innerParms

        if (visibleLastElement) {
            certificateChip.llCertificate.visibility = View.GONE
            certificateChip.txtAddmore.visibility = View.VISIBLE
        } else {
            certificateChip.llCertificate.visibility = View.VISIBLE
            certificateChip.txtAddmore.visibility = View.GONE
        }

        certificateChip.txtCertificateName.text = certificate

        certificateChip.txtAddmore.setOnClickListener {
            showOptions(mRegisterCoachActivity.mResponse.certificates as ArrayList<OptionsModel>,
                    txtCoachExperience.text.toString(),
                    getString(R.string.choose_certificate), true)
        }

        certificateChip.imgRemoveCertificate.setOnClickListener {
            mRegisterCoachActivity.mSelectedCertificateArray.remove(certificate)
            mRegisterCoachActivity.mSelectedCertificateArray.remove("add more")
            displaySelectedCertificates()
        }
        return certificateChip
    }

    private fun inflateExpertiseView(optionsModel: OptionsModel): View {
        val interestChip = LayoutInflater.from(activity).inflate(R.layout.layout_expertise, null, false)

        val innerParms = FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        interestChip.llExpertise.layoutParams = innerParms

        interestChip.txtExpertise.text = optionsModel.name

        if (optionsModel.isSelected) {
            mRegisterCoachActivity.mSelectedExpertiseArray.add(optionsModel.id)
            interestChip.txtExpertise.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
            when (optionsModel.color) {
                1 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_first)
                2 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_second)
                3 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_third)
                4 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_fourth)
                5 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_fifth)
                6 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_sixth)
            }
        }

        interestChip.txtExpertise.setOnClickListener {
            if (mRegisterCoachActivity.mSelectedExpertiseArray.contains(optionsModel.id)) {
                interestChip.txtExpertise.setTextColor(ContextCompat.getColor(activity!!, R.color.colorGrey))
                mRegisterCoachActivity.mSelectedExpertiseArray.remove(optionsModel.id)
                interestChip.txtExpertise.setBackgroundResource(R.drawable.unselected_expertise)
            } else {
                interestChip.txtExpertise.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
                mRegisterCoachActivity.mSelectedExpertiseArray.add(optionsModel.id)
                when (optionsModel.color) {
                    1 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_first)
                    2 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_second)
                    3 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_third)
                    4 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_fourth)
                    5 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_fifth)
                    6 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_sixth)
                }
            }
        }

        return interestChip
    }

}