package com.myreevuuCoach.fragments

import android.app.Activity.RESULT_OK
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.CreateProfileActivity
import com.myreevuuCoach.activities.OptionPhotoSelection
import com.myreevuuCoach.adapters.OptionsAdapter
import com.myreevuuCoach.chipsLibs.ChipInterface
import com.myreevuuCoach.chipsLibs.ChipsInput
import com.myreevuuCoach.filePicker.FilePickerBuilder
import com.myreevuuCoach.models.OptionsModel
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.OnViewGlobalLayoutListener
import kotlinx.android.synthetic.main.dialog_document.*
import kotlinx.android.synthetic.main.dialog_options.*
import kotlinx.android.synthetic.main.fragment_personal_info.*
import java.io.File
import java.lang.Exception


class PersonalInfoFragment : BaseKotlinFragment() {

    private val PIC = 2

    lateinit var fragment: PersonalInfoFragment
    lateinit var optionDialog: BottomSheetDialog
    private var mDocumentPath: String = Constants.EMPTY
    var mPath: String = Constants.EMPTY
    lateinit var mCreateProfileActivity: CreateProfileActivity

    override fun setContentView() = R.layout.fragment_personal_info

    override fun initUI() {

    }

    override fun onActivityCreated() {
        mCreateProfileActivity = activity as CreateProfileActivity
        fragment = this
//        getCountryCode()

        val typeface = Typeface.createFromAsset(activity!!.assets, "regular.ttf")

        ciEmail.setTypeface(typeface)
        ciEmail.setTextSize(resources.getDimension(R.dimen._2sdp))

        ciEmail.addChipsListener(object : ChipsInput.ChipsListener {
            override fun onChipAdded(chip: ChipInterface, newSize: Int) {
                mCreateProfileActivity.mAthleteArray.add(chip.label)
            }

            override fun onChipRemoved(chip: ChipInterface, newSize: Int) {
                mCreateProfileActivity.mAthleteArray.remove(chip.label)
            }

            override fun onTextChanged(text: CharSequence) {

            }
        })

//        svPersonalInfo.viewTreeObserver.addOnGlobalLayoutListener {
//            val r = Rect()
//            svPersonalInfo.getWindowVisibleDisplayFrame(r)
//            val heightDiff = svPersonalInfo.rootView.height - (r.bottom - r.top)
//
//            if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
//                //ok now we know the keyboard is up...
//                mCreateProfileActivity.txtNEXTProfile.visibility = View.INVISIBLE
//            } else {
//                //ok now we know the keyboard is down...
//                mCreateProfileActivity.txtNEXTProfile.visibility = View.VISIBLE
//            }
//        }

        edUserName.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->

            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER ) {
                if (edUserName.text.toString().trim().isNotEmpty()) {
                    if (mCreateProfileActivity.connectedToInternet()) {
                        mCreateProfileActivity.hitVerifyUsernameAPI(edUserName.text.toString().trim(), txtUsernameStatus)
                    } else {
                        mCreateProfileActivity.showAlert(llCoachGender, mCreateProfileActivity.mErrorInternet)
                    }
                }
                return@OnKeyListener true
            }
            false
        })
        edUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (mCreateProfileActivity.userName == char.toString().trim()) {
                    mCreateProfileActivity.isUserNameAvailable = true
                    txtUsernameStatus.visibility = View.VISIBLE
                } else {
                    mCreateProfileActivity.isUserNameAvailable = false
                    txtUsernameStatus.visibility = View.INVISIBLE
                }
            }
        })

        edUserName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (edUserName.text.toString().trim().isNotEmpty()) {
                    if (!mCreateProfileActivity.isUserNameAvailable) {
                        if (mCreateProfileActivity.connectedToInternet()) {
                            mCreateProfileActivity.hitVerifyUsernameAPI(edUserName.text.toString().trim(),
                                    txtUsernameStatus)
                        } else {
                            mCreateProfileActivity.showAlert(llCoachGender, mCreateProfileActivity.mErrorInternet)
                        }
                    }
                }
            }
        }
    }

    override fun initListener() {
        imgAddEditProfile.setOnClickListener(this)
//        txtCountryCode.setOnClickListener(this)
        llCoachGender.setOnClickListener(this)
        rlContract.setOnClickListener(this)
        imgInfo.setOnClickListener(this)
        txtDocumentExtension.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            txtDocumentExtension -> {
                if (mDocumentPath.isNotEmpty())
                    showDocumentOptions()
                else
                    openFileChooser()
            }
            imgInfo -> {
                val location = IntArray(2)
                imgInfo.getLocationOnScreen(location)
                onButtonShowPopupWindowClick(imgInfo, location[0], location[1])
            }
//            txtCountryCode -> {
//                val inSelectCountry = Intent(activity, CountryCodeActivity::class.java)
//                startActivityForResult(inSelectCountry, COUNTRY_CODE)
//                activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
//            }
            imgAddEditProfile -> {
                val inProfile = Intent(activity, OptionPhotoSelection::class.java)
                if (!TextUtils.isEmpty(mPath)) {
                    inProfile.putExtra("visible", "yes")
                    inProfile.putExtra("path", mPath)
                }
                startActivityForResult(inProfile, PIC)
                activity!!.overridePendingTransition(0, 0)
            }
            llCoachGender -> {
                showOptions(Constants.genderData(), txtGender.text.toString(), getString(R.string.select_gender))
            }
            rlContract -> {

            }
        }
    }

    private fun openFileViewer() {
        val file = File(mDocumentPath)
        val fileUri = Uri.fromFile(file)
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            if (mDocumentPath.contains(".pdf")) {
                // PDF file
                intent.setDataAndType(fileUri, "application/pdf")
            } else if (mDocumentPath.contains(".txt")) {
                // Text file
                intent.setDataAndType(fileUri, "text/plain")
            } else if (mDocumentPath.contains(".doc")) {
                // doc file
                intent.setDataAndType(fileUri, "application/msword")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity!!.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show()
        }

    }

    private fun openFileChooser() {
        val selectedPhotos = java.util.ArrayList<String>()
        FilePickerBuilder.getInstance()
                .setMaxCount(1)
                .setSelectedFiles(selectedPhotos)
                .enableDocSupport(true)
                .setActivityTheme(R.style.FilePickerTheme)
                .pickFile(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK)
            when (requestCode) {
//                COUNTRY_CODE -> {
//                    txtCountryCode.text = data!!.getStringExtra("Country_code")
//                    mCreateProfileActivity.phoneCountryCode = txtCountryCode.text.toString()
//                }
                PIC -> {
                    mPath = data!!.getStringExtra("filePath")
                    mCreateProfileActivity.mFileImage = File(mPath)

                    val options = RequestOptions().centerCrop().placeholder(R.mipmap.ic_profile_avatar)
                            .circleCrop()
                            .error(R.mipmap.ic_add_pic)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)

                    Glide.with(activity!!).load(mCreateProfileActivity.mFileImage.absolutePath).apply(options).into(imgPlaceHolder)
                    imgAddEditProfile.setImageResource(R.mipmap.ic_edit_pic)
                }
            }
        super.onActivityResult(requestCode, resultCode, data)
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
        txtGender.text = selectedOption
        mCreateProfileActivity.gender = id
    }

//    internal fun getCountryCode() {
//        val tm = activity!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        val countryCode = tm.networkCountryIso.toUpperCase()
//        val rl = resources.getStringArray(R.array.CountryCodes)
//
//        for (i in rl.indices) {
//            val g = rl[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            if (g[1].trim { it <= ' ' } == countryCode.trim { it <= ' ' }) {
//                txtCountryCode.setText("+" + g[0])
//                break
//            }
//        }
//    }

    fun documentSelected(documentPath: String) {
        mDocumentPath = documentPath
        val documentExtension = documentPath.substring(documentPath.length - 3, documentPath.length)
        txtDocumentExtension.text = documentExtension
        txtTapToAdd.visibility = View.GONE

        imgDoc.setImageResource(R.mipmap.ic_doc_file_big)
        rlContract.setBackgroundResource(0)
    }

    fun onButtonShowPopupWindowClick(view: View, X: Int, Y: Int) {
        // inflate the layout of the popup window

        val inflater = activity!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val popupView = inflater.inflate(R.layout.popup_window, null)
        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)
        popupWindow.showAtLocation(view, Gravity.TOP, X, Y - (Constants.dpToPx(80)));
//        popupWindow.showAsDropDown(views)

        popupView.setOnTouchListener(View.OnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        })
    }

    private fun showDocumentOptions() {
        val documentDialog = BottomSheetDialog(activity!!)
        documentDialog.setContentView(R.layout.dialog_document)

        val dialogParms = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT)
        dialogParms.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        val bottomSheet = documentDialog.window.findViewById(android.support.design.R.id.design_bottom_sheet) as FrameLayout
        bottomSheet.setBackgroundResource(R.drawable.white_default)
        bottomSheet.layoutParams = dialogParms
        bottomSheet.viewTreeObserver
                .addOnGlobalLayoutListener(OnViewGlobalLayoutListener(bottomSheet, Constants.dpToPx(300)))

        val txtViewDocument = documentDialog.txtViewDocument
        val txtAddDocument = documentDialog.txtAddDocument

        txtViewDocument.setOnClickListener {
            documentDialog.dismiss()
            openFileViewer()
        }

        txtAddDocument.setOnClickListener {
            documentDialog.dismiss()
            openFileChooser()
        }

        documentDialog.show()
    }


}