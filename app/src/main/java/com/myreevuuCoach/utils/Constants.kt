package com.myreevuuCoach.utils

import android.Manifest
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.myreevuuCoach.R
import com.myreevuuCoach.models.OptionsModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Constants {

    companion object {
        val EMPTY: String = ""
        val EMAIL_VERIFY = "email_verify"
        val NEW_MESSAGE_FROM_ADMIN = "NEW_MESSAGE_FROM_ADMIN"
        val NEW_MESSAGE = "NEW_MESSAGE"

        val ERROR_CODE = 311


        val KEY_SELECTED_MEDIA = "SELECTED_PHOTOS"
        val KEY_SELECTED_DOCS = "SELECTED_DOCS"
        val EXTRA_PICKER_TYPE = "EXTRA_PICKER_TYPE"
        val DOC_PICKER = 0x12
        val PERMISSIONS_FILE_PICKER = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val SUCCESS = "Success"
        val FAILURE = "Failure"

        enum class FILE_TYPE {
            PDF, WORD, EXCEL, PPT, TXT, UNKNOWN
        }

        fun contains(types: Array<String>, path: String): Boolean {
            for (string in types) {
                if (path.toLowerCase().endsWith(string)) return true
            }
            return false
        }

        val PDF = "PDF"
        val DOC = "DOC"
        val TXT = "TXT"
        val REQUEST_CODE_DOC = 234
        val DEFAULT_MAX_COUNT = -1
        val FILE_TYPE_MEDIA = 1
        val FILE_TYPE_DOCUMENT = 2
        val MEDIA_PICKER = 0x11

        fun closeKeyboard(mContext: Context, view: View) {
            val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        @Throws(ParseException::class)
        fun displayDateTime(endDate: String): String {

            val utcFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
            val date_gmt = SimpleDateFormat("Z", Locale.US)
            val gmt_text = date_gmt.format(calendar.time)

            val utc_date = utcFormat.parse(endDate)
            val utc_create = Calendar.getInstance()
            utc_create.time = utc_date

            val hh: Int

            val mm: Int
            if (gmt_text.trim { it <= ' ' }.length == 3) {

            } else {
                hh = Integer.parseInt(gmt_text.substring(1, 3))
                mm = Integer.parseInt(gmt_text.substring(3, 5))

                if (gmt_text.substring(0, 1) == "+") {
                    utc_create.add(Calendar.HOUR_OF_DAY, hh)
                    utc_create.add(Calendar.MINUTE, mm)
                } else if (gmt_text.substring(0, 1) == "-") {
                    utc_create.add(Calendar.HOUR_OF_DAY, -hh)
                    utc_create.add(Calendar.MINUTE, -mm)
                }
            }
            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm aa", Locale.US)
            return dateFormat.format(utc_create.time)
        }

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }
        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()
        }
        fun sportsData(): ArrayList<String> {
            val sportsData = ArrayList<String>()
            sportsData.add("Tennis")
            sportsData.add("Wrestling")
            return sportsData
        }



        fun experienceData(): ArrayList<OptionsModel> {
            val experienceData = ArrayList<OptionsModel>()
            experienceData.add(OptionsModel(0, "< 1 Year", 0, false))
            experienceData.add(OptionsModel(1, "1 Year", 0, false))
            for (year in 2..20) {
                experienceData.add(OptionsModel(year, "$year Years", 0, false))
            }
            return experienceData
        }

        fun reportData(): ArrayList<OptionsModel> {
            val sportsData = ArrayList<OptionsModel>()
            sportsData.add(OptionsModel(1, "This video is irrelevant", 0, false))
            sportsData.add(OptionsModel(2, "This video contains inappropriate content", 0, false))
            sportsData.add(OptionsModel(3, "Video doesn't belong to any sportsArray", 0, false))
            sportsData.add(OptionsModel(4, "Others", 0, false))
            return sportsData
        }

        fun sportsPlayedData(): ArrayList<OptionsModel> {
            val sportsData = ArrayList<OptionsModel>()
            sportsData.add(OptionsModel(1, "Yes", 0, false))
            sportsData.add(OptionsModel(0, "No", 0, false))
            return sportsData
        }

        fun certificatesData(): ArrayList<String> {
            val sportsData = ArrayList<String>()
            sportsData.add("Lorem Ipsum")
            sportsData.add("Certificate")
            sportsData.add("National Level")
            sportsData.add("Certificate 4")
            return sportsData
        }

        fun genderData(): ArrayList<OptionsModel> {
            val genderData = ArrayList<OptionsModel>()
            genderData.add(OptionsModel(1, "Male", 0, false))
            genderData.add(OptionsModel(0, "Female", 0, false))
            genderData.add(OptionsModel(2, "Other", 0, false))
            return genderData
        }


        fun getGradient():ArrayList<Int> {
            val arrayList = ArrayList<Int>()
            arrayList.add(R.drawable.gradient_first)
            arrayList.add(R.drawable.gradient_second)
            arrayList.add(R.drawable.gradient_third)
            arrayList.add(R.drawable.gradient_fourth)
            arrayList.add(R.drawable.gradient_fifth)
            arrayList.add(R.drawable.gradient_sixth)
            arrayList.add(R.drawable.gradient_fourth)

            return arrayList
        }



    }
}