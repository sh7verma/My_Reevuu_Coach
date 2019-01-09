package com.myreevuuCoach.activities

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.myreevuuCoach.R
import com.myreevuuCoach.adapters.FragmentAdapter
import com.myreevuuCoach.customViews.RegularTextView
import com.myreevuuCoach.fragments.AccountInfoFragment
import com.myreevuuCoach.fragments.PersonalInfoFragment
import com.myreevuuCoach.models.BaseSuccessModel
import com.myreevuuCoach.models.ProfileModel
import com.myreevuuCoach.models.SignUpModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.activity_create_profile.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_personal_info.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreateProfileActivity : BaseKotlinActivity() {

    private val mFragmentArray = ArrayList<Fragment>()
    var documentPath: String = Constants.EMPTY

    lateinit var mFileImage: File
    var userName = Constants.EMPTY
    var isUserNameAvailable = false
    //    var phoneCountryCode: String = Constants.EMPTY
//    var phoneNumber: String = Constants.EMPTY
    var gender: Int = 0
    var mAthleteArray = ArrayList<String>()
    lateinit var mFileContract: File

    var currency: String = "usd"
    var routingNumber: String = Constants.EMPTY
    var accountNumber: String = Constants.EMPTY
    var city: String = Constants.EMPTY
    var address: String = Constants.EMPTY
    var postalCode: String = Constants.EMPTY
    var state: String = Constants.EMPTY
    var dob: String = Constants.EMPTY
    var firstName: String = Constants.EMPTY
    var lastName: String = Constants.EMPTY
    var ssn: String = Constants.EMPTY
    var countryCode: String = "US"
    lateinit var mFileDcoument: File

    lateinit var mResponse: SignUpModel

    override fun getContentView(): Int {
        val decorView = window.decorView
        decorView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack))
        return R.layout.activity_create_profile
    }

    override fun initUI() {
        if (intent.hasExtra("displayAnimation"))
            displaySplashAnimation()
    }

    override fun onCreateStuff() {
        mResponse = mGson.fromJson(mUtils.getString("response", ""), SignUpModel::class.java)

        mFragmentArray.add(PersonalInfoFragment())
        mFragmentArray.add(AccountInfoFragment())
        vpCreateProfile.adapter = FragmentAdapter(supportFragmentManager, mFragmentArray)
    }

    override fun initListener() {
        txtPersonalInfo.setOnClickListener(this)
        txtAccountInfo.setOnClickListener(this)
        txtNEXTProfile.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View) {
        when (view) {
            txtPersonalInfo -> {
                if (vpCreateProfile.currentItem != 0) {
                    vpCreateProfile.currentItem = 0
                    selectPerosnal()
                }
            }
            txtAccountInfo -> {
                if (vpCreateProfile.currentItem != 1) {
                    vpCreateProfile.currentItem = 1
                    selectAccount()
                }
            }
            txtNEXTProfile -> {
                if (vpCreateProfile.currentItem == 0) {
                    val personalInfoFragment = mFragmentArray.get(0) as PersonalInfoFragment
                    when {
                        personalInfoFragment.mPath.isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_profile_pic))
                        personalInfoFragment.edUserName.text.toString().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_user_name))
//                        personalInfoFragment.txtCountryCode.text.toString().isEmpty() ->
//                            showAlert(txtNEXTProfile, getString(R.string.error_country_code))
//                        personalInfoFragment.edPhoneNumber.text.toString().isEmpty() ->
//                            showAlert(txtNEXTProfile, getString(R.string.error_phone_number))
                        personalInfoFragment.txtGender.text.toString().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_coach_gender))
                        documentPath.isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_contractor_form))
                        else -> {
//                            phoneNumber = personalInfoFragment.edPhoneNumber.text.toString()
                            vpCreateProfile.currentItem = 1
                            selectAccount()
                        }
                    }
                } else {
                    val accountInfoFragment = mFragmentArray[1] as AccountInfoFragment
                    when {
                        accountInfoFragment.edRountingNo.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_routing))
                        accountInfoFragment.edFirstName.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_first_name))
                        accountInfoFragment.edLastName.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_last_name))
                        accountInfoFragment.edAccountNumber.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_account_no))
                        accountInfoFragment.edAddress.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_address))
                        accountInfoFragment.edCity.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_city))
                        accountInfoFragment.txtState.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_state))
                        accountInfoFragment.edPostalCode.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_postal_code))
                        accountInfoFragment.txtDOB.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_dob))
                        accountInfoFragment.edSSN.text.toString().trim().isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_ssn))
                        accountInfoFragment.mPath.isEmpty() ->
                            showAlert(txtNEXTProfile, getString(R.string.error_document))
                        else -> {
                            routingNumber = accountInfoFragment.edRountingNo.text.toString().trim()
                            firstName = accountInfoFragment.edFirstName.text.toString().trim()
                            lastName = accountInfoFragment.edLastName.text.toString().trim()
                            accountNumber = accountInfoFragment.edAccountNumber.text.toString().trim()
                            address = accountInfoFragment.edAddress.text.toString().trim()
                            city = accountInfoFragment.edCity.text.toString().trim()
                            state = accountInfoFragment.txtState.text.toString().trim()
                            postalCode = accountInfoFragment.edPostalCode.text.toString().trim()
                            dob = accountInfoFragment.txtDOB.text.toString().trim()
                            ssn = accountInfoFragment.edSSN.text.toString().trim()

                            var emailsAthelte: String = Constants.EMPTY
                            if (mAthleteArray.isNotEmpty()) {
                                val stringBuilderExpertise = StringBuilder()
                                for (emails in mAthleteArray) {
                                    stringBuilderExpertise.append(emails).append(",")
                                }
                                emailsAthelte = stringBuilderExpertise.toString().substring(0,
                                        stringBuilderExpertise.toString().length - 1)
                            }

                            if (connectedToInternet())
                                hitCreateProfileAPI(emailsAthelte)
                            else
                                showInternetAlert(txtNEXTProfile)
                        }
                    }
                }
            }
        }
    }

    private fun selectAccount() {
        txtAccountInfo.setBackgroundResource(R.drawable.primary_ripple_extreme_round)
        txtAccountInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

        txtPersonalInfo.setBackgroundResource(R.drawable.unselected_default)
        txtPersonalInfo.setTextColor(ContextCompat.getColor(this, R.color.colorGrey))
        txtNEXTProfile.text = getString(R.string.submit)
    }

    private fun selectPerosnal() {
        txtPersonalInfo.setBackgroundResource(R.drawable.primary_ripple_extreme_round)
        txtPersonalInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

        txtAccountInfo.setBackgroundResource(R.drawable.unselected_default)
        txtAccountInfo.setTextColor(ContextCompat.getColor(this, R.color.colorGrey))
        txtNEXTProfile.text = getString(R.string.next)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                Constants.REQUEST_CODE_DOC -> {
                    val docPaths = ArrayList<String>()
                    docPaths.addAll(data!!.getStringArrayListExtra(Constants.KEY_SELECTED_DOCS))
                    documentPath = docPaths.get(0)
                    mFileContract = File(documentPath)
                    val fragment = mFragmentArray.get(0) as PersonalInfoFragment
                    fragment.documentSelected(documentPath)
                }
            }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun hitCreateProfileAPI(emailsAthelte: String) {
        showLoader()
        val call = RetrofitClient.getInstance().createProfile(
                createPartFromString(mUtils.getString("accessToken", "")),
                createPartFromString(userName),
                createPartFromString(Constants.EMPTY),
                createPartFromString(Constants.EMPTY),
                createPartFromString(gender.toString()),
                createPartFromString(emailsAthelte),
                createPartFromString(currency),
                createPartFromString(routingNumber),
                createPartFromString(accountNumber),
                createPartFromString(city),
                createPartFromString(address),
                createPartFromString(postalCode),
                createPartFromString(state),
                createPartFromString(dob),
                createPartFromString(firstName),
                createPartFromString(lastName),
                createPartFromString(ssn),
                createPartFromString(countryCode),
                prepareFilePart(mFileImage),
                prepareFilePartContractor(mFileContract),
                prepareFilePartDocumnet(mFileDcoument))

        call.enqueue(object : Callback<SignUpModel> {
            override fun onFailure(call: Call<SignUpModel>?, t: Throwable?) {
                dismissLoader()
                showAlert(txtNEXTProfile, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<SignUpModel>?, response: Response<SignUpModel>) {
                dismissLoader()
                if (response.body().error != null) {
                    showAlert(txtNEXTProfile, response.body().error.message)
                } else {
                    addDataToLocal(response.body().response)
                    setUserData(response.body())
                    moveToLanding()
                }
            }
        })
    }

    private fun addDataToLocal(response: ProfileModel) {
        mResponse.response = response
        mUtils.setString("response", mGson.toJson(mResponse))
        mUtils.setInt("profileStatus", response.profile_status)
    }

    private fun moveToLanding() {
        val intent = Intent(mContext, LandingActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    private fun createPartFromString(data: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), data)
    }

    private fun prepareFilePart(mFile: File): MultipartBody.Part {
        val requestFile = RequestBody.create(MediaType.parse("image/*"), mFile)
        return MultipartBody.Part.createFormData("user_image", mFile.name, requestFile)
    }

    private fun prepareFilePartDocumnet(mFile: File): MultipartBody.Part {
        val requestFile = RequestBody.create(MediaType.parse("image/*"), mFile)
        return MultipartBody.Part.createFormData("document", mFile.name, requestFile)
    }

    private fun prepareFilePartContractor(mFile: File): MultipartBody.Part {
        val requestFile = RequestBody.create(MediaType.parse("application/*"), mFile)
        return MultipartBody.Part.createFormData("contractor_forms", mFile.name, requestFile)
    }

    fun hitVerifyUsernameAPI(username: String, txtUsernameStatus: RegularTextView) {
        val call = RetrofitClient.getInstance().verifyUsername(username
                , mUtils.getString("accessToken", ""))
        call.enqueue(object : Callback<BaseSuccessModel> {
            override fun onFailure(call: Call<BaseSuccessModel>?, t: Throwable?) {
                dismissLoader()
                showAlert(llCoachGender, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<BaseSuccessModel>?, response: Response<BaseSuccessModel>) {
                if (response.body().error != null) {
                    showAlert(edUserName, response.body().error.message)
                    isUserNameAvailable = false
                    userName = edUserName.text.toString().trim()
                    txtUsernameStatus.text = getString(R.string.not_available)
                    txtUsernameStatus.setTextColor(ContextCompat.getColor(this@CreateProfileActivity, R.color.colorRed))
                } else {
                    txtUsernameStatus.visibility = View.VISIBLE
                    if (response.body().response.message.equals("1")) {
                        isUserNameAvailable = true
                        userName = edUserName.text.toString().trim()
                        txtUsernameStatus.text = getString(R.string.available)
                        txtUsernameStatus.setTextColor(ContextCompat.getColor(this@CreateProfileActivity, R.color.colorGreen))
                    } else {
                        isUserNameAvailable = false
                        userName = edUserName.text.toString().trim()
                        txtUsernameStatus.text = getString(R.string.not_available)
                        txtUsernameStatus.setTextColor(ContextCompat.getColor(this@CreateProfileActivity, R.color.colorRed))
                    }
                }
            }
        })
    }

}