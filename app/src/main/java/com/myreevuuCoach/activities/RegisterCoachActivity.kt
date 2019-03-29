package com.myreevuuCoach.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.widget.Toast
import com.myreevuuCoach.R
import com.myreevuuCoach.adapters.FragmentAdapter
import com.myreevuuCoach.fragments.CoachInfoFragment
import com.myreevuuCoach.fragments.ProfileInfoFragment
import com.myreevuuCoach.fragments.SportsInfoFragment
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.DefaultArrayModel
import com.myreevuuCoach.models.ProfileModel
import com.myreevuuCoach.models.SignUpModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.CustomLoadingDialog
import kotlinx.android.synthetic.main.activity_register_coach.*
import kotlinx.android.synthetic.main.dialog_profile_submit.*
import kotlinx.android.synthetic.main.fragment_coach_info.*
import kotlinx.android.synthetic.main.fragment_sports_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterCoachActivity : BaseKotlinActivity() {

    val mFragmentArray = ArrayList<Fragment>()

    private var moveToCoach = false
    private lateinit var dialogProfileSubmit: Dialog
    lateinit var mResponse: SignUpModel
    lateinit var mDefaultResponse: DefaultArrayModel

    var sportId: Int = 0
    var sportExperience: Int = 0
    var collegeSport: Int = 0
    var sportLevelId: Int = 0

    var coachLevelId: Int = 0
    var collageName: String = ""
    var coachExperience: Int = 0
    var mSelectedExpertiseArray = ArrayList<Int>()
    lateinit var coachAbout: String
    var mSelectedCertificateArray = ArrayList<String>()

    override fun getContentView(): Int {
        val decorView = window.decorView
        decorView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack))
        return R.layout.activity_register_coach
    }

    override fun initUI() {
        if (intent.hasExtra("displayAnimation"))
            displaySplashAnimation()
    }

    override fun onCreateStuff() {
        mResponse = mGson.fromJson(mUtils.getString("response", ""), SignUpModel::class.java)

        mFragmentArray.add(SportsInfoFragment())
        mFragmentArray.add(CoachInfoFragment())
        vpRegister.adapter = FragmentAdapter(supportFragmentManager, mFragmentArray)

        if (intent.hasExtra(InterConst.EDIT_INFO)) {
            rlToolBar.visibility = View.VISIBLE
            txtTitle.visibility = View.GONE
            txtDetail.visibility = View.GONE
        }
    }


    override fun initListener() {
        txtSportInfo.setOnClickListener(this)
        txtCoachInfo.setOnClickListener(this)
        txtNEXT.setOnClickListener(this)
        imgBack.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View) {
        when (view) {
            txtSportInfo -> {
                if (vpRegister.currentItem != 0) {
                    vpRegister.currentItem = 0
                    selectSport()
                }
            }
            imgBack -> {
                finish()
            }
            txtCoachInfo -> {
                if (vpRegister.currentItem != 1 && moveToCoach) {
                    vpRegister.currentItem = 1
                    selectCoach()
                }
            }
            txtNEXT -> {
                if (vpRegister.currentItem == 0) {
                    val sportsInfoFragment = mFragmentArray.get(0) as SportsInfoFragment
                    when {
                        sportsInfoFragment.txtSportPlayed.text.toString().isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_sport_played))
                        sportsInfoFragment.txtSportExperience.text.toString().isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_sport_experience))
                        sportsInfoFragment.txtSportPlayedCollege.text.toString().isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_sport_college))
                        sportsInfoFragment.txtSportHighestLevel.text.toString().isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_sport_highest))
                        else -> {
                            moveToCoach = true
                            vpRegister.currentItem = 1
                            selectCoach()
                        }
                    }
                } else {
                    val coachInfoFragment = mFragmentArray.get(1) as CoachInfoFragment
                    when {
                        coachInfoFragment.txtHightestLevelCoached.text.toString().isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_level_coached))
                        coachInfoFragment.txtCoachExperience.text.toString().isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_coaching_experience))
                        mSelectedExpertiseArray.isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_expertise))
                        coachInfoFragment.edAboutYou.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_enter_about))
                        mSelectedCertificateArray.isEmpty() ->
                            showAlert(txtNEXT, getString(R.string.error_coaching_certificate))
                        else -> {
                            if (connectedToInternet()) {

                                if (mSelectedCertificateArray.contains("add more"))
                                    mSelectedCertificateArray.remove("add more")

                                if (coachInfoFragment.edOtherCertificates.text.toString().trim().isNotEmpty()) {
                                    mSelectedCertificateArray.add(coachInfoFragment.edOtherCertificates.text.toString().trim())
                                }

                                val stringBuilderExpertise = StringBuilder()
                                for (expertise in mSelectedExpertiseArray) {
                                    stringBuilderExpertise.append(expertise).append(",")
                                }

                                val stringBuilderCertificates = StringBuilder()
                                for (certificates in mSelectedCertificateArray) {
                                    stringBuilderCertificates.append(certificates).append(",")
                                }

                                coachAbout = coachInfoFragment.edAboutYou.text.toString().trim()
                                hitCoachSetupAPI(stringBuilderExpertise.toString(), stringBuilderCertificates.toString())
                            } else {
                                showAlert(txtNEXT, mErrorInternet)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hitCoachSetupAPI(expertise: String, certificates: String) {
        showLoader()
        val expertiseServer = expertise.substring(0, expertise.length - 1)
        val certificatesServer = certificates.substring(0, certificates.length - 1)

        val call = RetrofitClient.getInstance().setupCoach(mUtils.getString("accessToken", ""),
                sportId, sportExperience, collegeSport,
                sportLevelId, coachLevelId, coachExperience,
                expertiseServer, coachAbout, collageName, certificatesServer)
        call.enqueue(object : Callback<SignUpModel> {
            override fun onFailure(call: Call<SignUpModel>?, t: Throwable?) {
                dismissLoader()
                showAlert(txtNEXT, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<SignUpModel>?, response: Response<SignUpModel>) {
                dismissLoader()
                if (response.body().error != null) {
                    showAlert(txtNEXT, response.body().error.message)
                } else {
                    addDataToLocal(response.body().response)
                    setUserData(response.body())
                    hitProfileDataApi(response.body().response.sport_info.sport.id)

                    if (!intent.hasExtra(InterConst.EDIT_INFO)) {
                        displayDialogProfileSubmit()
                    } else {
                        ProfileInfoFragment.getInstance().setData()
                        finish()
                    }

                }
            }
        })
    }

    fun hitProfileDataApi(id: Int) {
        CustomLoadingDialog.getLoader().showLoader(mContext)

        val call = RetrofitClient.getInstance().profile_data(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                id.toString())
        call.enqueue(object : Callback<DefaultArrayModel> {
            override fun onResponse(call: Call<DefaultArrayModel>, response: Response<DefaultArrayModel>) {
                CustomLoadingDialog.getLoader().dismissLoader()
                if (response.body().response != null) {
                    mUtils.setString(InterConst.SPORTS_RESPONSE, mGson.toJson(response.body()))
                    mDefaultResponse =
                            mGson.fromJson(mGson.toJson(response.body()),
                                    DefaultArrayModel::class.java)
                } else {
                    Toast.makeText(mContext, response.body().error!!.message!!, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DefaultArrayModel>, t: Throwable) {
                CustomLoadingDialog.getLoader().dismissLoader()
            }
        })
    }


    private fun selectCoach() {
        txtCoachInfo.setBackgroundResource(R.drawable.primary_ripple_extreme_round)
        txtCoachInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

        txtSportInfo.setBackgroundResource(R.drawable.unselected_default)
        txtSportInfo.setTextColor(ContextCompat.getColor(this, R.color.colorGrey))
        txtNEXT.text = getString(R.string.submit)
    }

    private fun selectSport() {
        txtSportInfo.setBackgroundResource(R.drawable.primary_ripple_extreme_round)
        txtSportInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

        txtCoachInfo.setBackgroundResource(R.drawable.unselected_default)
        txtCoachInfo.setTextColor(ContextCompat.getColor(this, R.color.colorGrey))
        txtNEXT.text = getString(R.string.next)
    }

    private fun addDataToLocal(response: ProfileModel) {
        mResponse.response = response
        mUtils.setString(InterConst.RESPONSE, mGson.toJson(mResponse))
    }

    fun displayDialogProfileSubmit() {
        dialogProfileSubmit = Dialog(this@RegisterCoachActivity)
        dialogProfileSubmit.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogProfileSubmit.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialogProfileSubmit.setCancelable(false)
        dialogProfileSubmit.setContentView(R.layout.dialog_profile_submit)

        val txtOK = dialogProfileSubmit.txtOK
        txtOK.setOnClickListener {
            // move to create profile.
            dialogProfileSubmit.dismiss()
            val intent = Intent(mContext, CreateProfileActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        dialogProfileSubmit.show()
    }


}