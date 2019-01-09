package com.myreevuuCoach.activities

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import com.ipaulpro.afilechooser.utils.FileUtils
import com.myreevuuCoach.BuildConfig
import com.myreevuuCoach.R
import com.myreevuuCoach.utils.Constants
import com.soundcloud.android.crop.Crop
import kotlinx.android.synthetic.main.activity_option_photo.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class OptionPhotoSelection : BaseKotlinActivity() {


    private val WRITE_EXTERNAL_ID = 1
    private val READ_EXTERNAL_ID = 2
    private val CAMERA_INTENT = 1
    private val GALLERY_INTENT = 2
    private val MULTIPLE_PERMISSIONS = 3

    private var mSystemTime: Long = 0

    private var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    override fun getContentView(): Int {
        this.window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorTransperent));
        return R.layout.activity_option_photo
    }

    override fun initUI() {

        if (intent.hasExtra("visible")) {
            txtOptionVp.visibility = View.VISIBLE
            viewFull.visibility = View.VISIBLE
        } else {
            txtOptionVp.visibility = View.GONE
            viewFull.visibility = View.GONE
        }

        translateView(mHeight.toFloat(), 0f, false)
    }

    override fun onCreateStuff() {
    }

    override fun initListener() {
        txtOptionGallery.setOnClickListener(this)
        txtOptionVp.setOnClickListener(this)
        txtTakePhoto.setOnClickListener(this)
        txtCancel.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        when (view) {
            txtOptionGallery -> {
                onGallery()
            }
            txtCancel -> {
                moveBack()
            }
            txtTakePhoto -> {
                onCamera()
            }
            txtOptionVp -> {
                onFullView()
            }
        }
    }

    private fun translateView(fromY: Float, toY: Float, finishActivity: Boolean) {
        val translateAnimation = TranslateAnimation(0f, 0f, fromY, toY)
        translateAnimation.fillAfter = true
        translateAnimation.duration = 300
        llOptionPhoto.startAnimation(translateAnimation)

        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if (finishActivity) {
                    moveBack()
                }
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })
    }

    private fun showGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, GALLERY_INTENT)
    }

    private fun onFullView() {
        val picArray = ArrayList<String>()
        picArray.add(intent.getStringExtra("path"))

        val intent = Intent(this@OptionPhotoSelection, FullViewImageActivity::class.java)
        intent.putStringArrayListExtra("images", picArray)
        startActivity(intent)
        finish()
    }

    internal fun onGallery() {
        if (ContextCompat.checkSelfPermission(this@OptionPhotoSelection, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@OptionPhotoSelection, arrayOf(READ_EXTERNAL_STORAGE), READ_EXTERNAL_ID)
        } else {
            showGallery()
        }
    }

    internal fun onCamera() {
        if (checkPermissions()) {
            startCameraActivity()
        }
    }

    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded = ArrayList<String>()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this@OptionPhotoSelection, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }

    internal fun startCameraActivity() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        mSystemTime = System.currentTimeMillis()
        val f = File(Environment.getExternalStorageDirectory(), "${getString(R.string.app_name)}$mSystemTime.png")
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            val photoURI = FileProvider.getUriForFile(this@OptionPhotoSelection,
                    BuildConfig.APPLICATION_ID + ".provider", f)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        } else {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
        }
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        startActivityForResult(cameraIntent, CAMERA_INTENT)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXTERNAL_ID -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCameraActivity()
            READ_EXTERNAL_ID -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                showGallery()
            MULTIPLE_PERMISSIONS -> {
                if (grantResults.size == 2) {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults.size > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        startCameraActivity()
                    }
                } else if (grantResults.size == 1) {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        startCameraActivity()
                    }
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == CAMERA_INTENT && resultCode == Activity.RESULT_OK) {
                val dir = Environment.getExternalStorageDirectory()
                val f = File(dir, "${getString(R.string.app_name)}$mSystemTime.png")
                Log.e("camera photo", "is " + f.absolutePath)
                val rr = Uri.fromFile(File(f.absolutePath))
                beginCrop(rr)
            } else if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
                val selectedImage = data!!.data
                beginCrop(selectedImage)
            } else if (requestCode == Crop.REQUEST_CROP) {
                Log.e("crop photo", "is $data")
                handleCrop(resultCode, data!!)
            }

        } catch (e: Exception) {
            Log.e("exc photo", "is $e")
            e.printStackTrace()
        }

    }

    private fun handleCrop(resultCode: Int, result: Intent) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                val ur = Crop.getOutput(result)
                val picturePath = FileUtils.getPath(this, ur)
                val ii = Intent()
                ii.putExtra("path", picturePath)
                ii.putExtra("filePath", getRealPathFromURI(getImageUri(this@OptionPhotoSelection, getBitmap(picturePath))))
                setResult(Activity.RESULT_OK, ii)
                finish()
            } else if (resultCode == Crop.RESULT_ERROR) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            e.printStackTrace()
        }

    }

    fun getBitmap(path: String?): Bitmap {
        val rotation = getImageOrientation(path)
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())

        val options = BitmapFactory.Options()
        options.inSampleSize = 1
        val sourceBitmap = BitmapFactory.decodeFile(path, options)

        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.width, sourceBitmap.height, matrix,
                true)
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

//    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
//        val values =  ContentValues();
//		values.put(MediaStore.Images.Media.TITLE, "");
//		values.put(MediaStore.Images.Media.DISPLAY_NAME, "");
//		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//		// Add the date meta data to ensure the image is added at the front of the gallery
//		values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
//		values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
//        return inContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//    }


    fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }


    fun getImageOrientation(imagePath: String?): Int {
        var rotate = 0
        try {
            val imageFile = File(imagePath!!)
            val exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return rotate
    }


    private fun beginCrop(source: Uri?) {
        val destination = Uri.fromFile(File(cacheDir, "cropped"))
        Crop.of(source, destination).asSquare().start(this)
    }


    override fun onBackPressed() {
        Constants.closeKeyboard(mContext, llOuterOptionPhoto)
        translateView(0f, mHeight.toFloat(), true)
    }

    private fun moveBack() {
        finish()
        overridePendingTransition(0, 0)
    }


}