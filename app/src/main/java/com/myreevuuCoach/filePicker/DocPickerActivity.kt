package filePicker

import android.content.Intent
import android.view.View
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.BaseKotlinActivity
import com.myreevuuCoach.filePicker.DocFragment
import com.myreevuuCoach.filePicker.DocPickerFragment
import com.myreevuuCoach.filePicker.FragmentUtil
import com.myreevuuCoach.filePicker.PickerManager
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.activity_doc_picker.*
import java.util.*

/**
 * Created by dev on 10/8/18.
 */
class DocPickerActivity : BaseKotlinActivity(), DocPickerFragment.DocPickerFragmentListener, DocFragment.DocFragmentListener {

    private var type: Int = 0

    override fun getContentView() = R.layout.activity_doc_picker

    override fun initUI() {
        type = intent.getIntExtra(Constants.EXTRA_PICKER_TYPE, Constants.MEDIA_PICKER)
        var selectedPaths = ArrayList<String>()
        openSpecificFragment(type, selectedPaths)
    }

    override fun onCreateStuff() {

    }

    override fun initListener() {
        imgBack.setOnClickListener(this)
        txtDone.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(v: View?) {
        when (v) {
            imgBack -> {
                moveBack()
            }
            txtDone -> {

            }
        }
    }

    override fun onBackPressed() {
        moveBack()
    }

    private fun moveBack() {
        PickerManager.getInstance().reset()
        setResult(RESULT_CANCELED)
        finish()
        overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out)
    }

    override fun onItemSelected() {
        val currentCount = PickerManager.getInstance().currentCount

        if (PickerManager.getInstance().maxCount == 1 && currentCount == 1) {
            returnData(
                    if (type == Constants.MEDIA_PICKER)
                        PickerManager.getInstance().selectedPhotos
                    else
                        PickerManager.getInstance().selectedFiles)
        }
    }

    private fun openSpecificFragment(type: Int, selectedPaths: ArrayList<String>?) {
        if (PickerManager.getInstance().isDocSupport) PickerManager.getInstance().addDocTypes()

        val photoFragment = DocPickerFragment.newInstance()
        FragmentUtil.addFragment(this, R.id.container, photoFragment)
    }

    private fun returnData(paths: ArrayList<String>) {
        val intent = Intent()
        if (type == Constants.MEDIA_PICKER) {
            intent.putStringArrayListExtra(Constants.KEY_SELECTED_MEDIA, paths)
        } else {
            intent.putStringArrayListExtra(Constants.KEY_SELECTED_DOCS, paths)
        }
        setResult(RESULT_OK, intent)
        finish()
        overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out)
    }

}