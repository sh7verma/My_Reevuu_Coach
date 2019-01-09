package com.myreevuuCoach.fragments

import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.RegisterCoachActivity
import com.myreevuuCoach.adapters.OptionsAdapter
import com.myreevuuCoach.models.OptionsModel
import com.myreevuuCoach.models.SignUpModel
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.OnViewGlobalLayoutListener
import kotlinx.android.synthetic.main.dialog_options.*
import kotlinx.android.synthetic.main.fragment_sports_info.*

class SportsInfoFragment : BaseKotlinFragment() {

    lateinit var fragmentCoach: CoachInfoFragment

    lateinit var fragment: SportsInfoFragment
    lateinit var optionDialog: BottomSheetDialog
    var optionSelected = 0
    lateinit var mRegisterCoachActivity: RegisterCoachActivity

    var experienceDataArray = ArrayList<OptionsModel>()
    var levelsArray = ArrayList<OptionsModel>()
    var sportsArray = ArrayList<OptionsModel>()
    var sportsPlayedDataArray = ArrayList<OptionsModel>()

    override fun setContentView() = R.layout.fragment_sports_info

    override fun initUI() {

    }

    override fun onActivityCreated() {
        mRegisterCoachActivity = activity as RegisterCoachActivity
        fragment = this
        fragmentCoach = mRegisterCoachActivity.mFragmentArray.get(1) as CoachInfoFragment
    }

    override fun initListener() {
        llSportExperience.setOnClickListener(this)
        llSportHighestLevel.setOnClickListener(this)
        llSportPlayed.setOnClickListener(this)
        llSportPlayedCollege.setOnClickListener(this)
        setPrefilledData()
    }


    fun clearData() {

        experienceDataArray.clear()
        levelsArray.clear()
        sportsArray.clear()
        sportsPlayedDataArray.clear()

        mRegisterCoachActivity.mResponse = mGson.fromJson(utils.getString("response", ""), SignUpModel::class.java)


        experienceDataArray.addAll(Constants.experienceData())
        sportsPlayedDataArray.addAll(Constants.sportsPlayedData())
        levelsArray.addAll(mRegisterCoachActivity.mResponse.levels)
        sportsArray.addAll(mRegisterCoachActivity.mResponse.sports)

        mRegisterCoachActivity.sportExperience = 0
        mRegisterCoachActivity.collegeSport = 0
        mRegisterCoachActivity.sportLevelId = 0

        txtSportExperience.text = ""
        txtSportPlayedCollege.text = ""
        txtSportHighestLevel.text = ""

        fragmentCoach.clearCoachData()
    }

    fun setPrefilledData() {
        experienceDataArray.addAll(Constants.experienceData())
        sportsPlayedDataArray.addAll(Constants.sportsPlayedData())
        levelsArray.addAll(mRegisterCoachActivity.mResponse.levels)
        sportsArray.addAll(mRegisterCoachActivity.mResponse.sports)

        if (mRegisterCoachActivity.mResponse.response.sport_info.sport != null) {

            for (j in sportsArray.indices) {
                if (mRegisterCoachActivity.mResponse.response.sport_info.sport.name
                        == sportsArray[j].name) {

                    sportsArray[j].isSelected = true
                    txtSportPlayed.text = sportsArray[j].name
                    mRegisterCoachActivity.sportId = sportsArray[j].id

                }
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

                if (mRegisterCoachActivity.mResponse.response.sport_info.experience == 0) {

                    experienceDataArray[0].isSelected = true
                    txtSportExperience.text = experienceDataArray[0].name
                    mRegisterCoachActivity.sportExperience = experienceDataArray[0].id

                } else if (experence == mRegisterCoachActivity.mResponse.response.sport_info
                                .experience) {
                    experienceDataArray[j].isSelected = true
                    txtSportExperience.text = experienceDataArray[j].name
                    mRegisterCoachActivity.sportExperience = experienceDataArray[j].id
                }

            }

            for (j in sportsPlayedDataArray.indices) {
                if (mRegisterCoachActivity.mResponse.response.sport_info.from_college
                        == sportsPlayedDataArray[j].id) {

                    sportsArray[j].isSelected = true
                    txtSportPlayedCollege.text = sportsPlayedDataArray[j].name
                    mRegisterCoachActivity.collegeSport = sportsPlayedDataArray[j].id
                }
            }

            for (j in levelsArray.indices) {
                if (mRegisterCoachActivity.mResponse.response.sport_info.level == levelsArray[j].id) {

                    levelsArray[j].isSelected = true
                    txtSportHighestLevel.text = levelsArray[j].name
                    mRegisterCoachActivity.sportLevelId = levelsArray[j].id

                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            llSportExperience -> {
                optionSelected = 2
                showOptions(experienceDataArray, txtSportExperience.text.toString(),
                        getString(R.string.playing_experience_in_years))
            }
            llSportHighestLevel -> {
                optionSelected = 4
                showOptions(levelsArray as ArrayList<OptionsModel>,
                        txtSportHighestLevel.text.toString(),
                        getString(R.string.highest_level_you_completed_in))
            }
            llSportPlayed -> {
                optionSelected = 1
                showOptions(sportsArray as ArrayList<OptionsModel>,
                        txtSportPlayed.text.toString(), getString(R.string.sports_played))
            }
            llSportPlayedCollege -> {
                optionSelected = 3
                showOptions(sportsPlayedDataArray, txtSportPlayedCollege.text.toString(),
                        getString(R.string.did_you_play_sports_in_college))
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

    override fun onOptionSelected(selectedOption: String, id: Int) {
        optionDialog.dismiss()
        when (optionSelected) {
            1 -> {
                if (!TextUtils.isEmpty(txtSportPlayed.text) && txtSportPlayed.text != selectedOption) {
                    clearData()
                }
                txtSportPlayed.text = selectedOption
                mRegisterCoachActivity.sportId = id

            }
            2 -> {
                txtSportExperience.text = selectedOption
                mRegisterCoachActivity.sportExperience = id
            }
            3 -> {
                txtSportPlayedCollege.text = selectedOption
                mRegisterCoachActivity.collegeSport = id
            }
            4 -> {
                txtSportHighestLevel.text = selectedOption
                mRegisterCoachActivity.sportLevelId = id
            }
        }
    }
}