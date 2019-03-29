package com.myreevuuCoach.fragments


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.SelectOption
import com.myreevuuCoach.utils.ConnectionDetector
import com.myreevuuCoach.utils.Utils

abstract class BaseKotlinFragment : Fragment(), View.OnClickListener, SelectOption {

    lateinit var itemView: View
    protected lateinit var mContext: Context
    private var mSnackbar: Snackbar? = null
    internal lateinit var utils: Utils
    var mGson = Gson()

    fun hideKeyboard(mContext: Activity?) {
        // Check if no views has focus:
        val view = mContext!!.currentFocus
        if (view != null) {
            val imm = mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(setContentView(), container, false)
        utils = Utils(activity)
        initUI()

        return itemView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        onActivityCreated()
        initListener()
        super.onActivityCreated(savedInstanceState)
    }

    abstract fun setContentView(): Int
    abstract fun initUI() /// Alter UI here
    abstract fun onActivityCreated() /// Initalize Variables here
    abstract fun initListener()


    fun connectedToInternet(view: View): Boolean {
        if (ConnectionDetector(context).isConnectingToInternet) {
            return true
        } else {
            showInternetAlert(view)
            return false
        }
    }

    protected fun showInternetAlert(view: View) {
        mSnackbar = Snackbar.make(view, R.string.internet, Snackbar.LENGTH_SHORT)
        mSnackbar!!.show()
    }

    protected fun showAlertSnackBar(view: View, message: String) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        mSnackbar!!.getView().setBackgroundColor(Color.RED)
        mSnackbar!!.show()
    }


}