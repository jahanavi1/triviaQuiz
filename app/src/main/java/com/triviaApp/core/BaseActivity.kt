package com.triviaApp.core

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Region
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.triviaApp.R
import com.triviaApp.constant.AppConst
import com.triviaApp.utils.AppUtils
import java.io.*
import java.lang.reflect.Type
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


open class BaseActivity : AppCompatActivity() {
    private var progressDialog: Dialog? = null
    private val iv: ImageView? = null
    var gson: Gson? = null
    lateinit var gsonExpose: Gson
    var VIDEO_URI: String = ""

    var mGoogleApiClient: GoogleApiClient? = null

    val visibleFragment: Fragment?
        get() {
            val fragmentManager = supportFragmentManager
            val fragments = fragmentManager.fragments
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible)
                    return fragment
            }
            return null
        }

    val activity: Activity
        get() = this
    val CUSTOM_REQUEST_CODE = 1002
    internal var mCurrentPhotoPath: String? = ""

    private var allPermissionsListener: MultiplePermissionsListener? = null


    public override fun onStart() {
        super.onStart()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Fabric.with(this, Crashlytics());
        AppUtils.setActivtiy(this)
        gson = Gson()
        gsonExpose = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }


    fun getDecimal(value: Double): Double {
        val formatter = DecimalFormat("#0.000000")
        return java.lang.Double.parseDouble(formatter.format(value))

    }

    fun buttonDisable(button: View) {
        button.alpha = 0.5f
        button.isEnabled = false
    }

    fun buttonEnable(button: View) {
        button.alpha = 1.0f
        button.isEnabled = true
    }


    fun replaceFragment(fragment: Fragment, name: String?, addtToBackStack: Boolean) {

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.container, fragment, name)
        if (addtToBackStack)
            ft.addToBackStack(name)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }


    fun launch(activityTosrat: Class<*>) {
        startActivity(Intent(this, activityTosrat))
    }

    fun launchActivityWithViewAnimation(
        options: ActivityOptionsCompat,
        activityTosrat: Class<*>,
        bundle: Bundle
    ) {
        val intent = Intent(this, activityTosrat)
        intent.putExtra(AppConst.BUNDLE, bundle)
        (activity).startActivity(intent, options.toBundle())
    }

    fun launchActivityWithViewAnimation(
        view: View,
        transitionName: String,
        activityTosrat: Class<*>
    ) {
        val intent = Intent(this, activityTosrat)

        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, transitionName)
        (activity).startActivity(intent, options.toBundle())
    }

    fun launchNewFirstActivity(activityTosrat: Class<*>) {
        val intent = Intent(applicationContext, activityTosrat)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        AppUtils.setActivtiy(this)
    }


    fun currentActivity(): String {
        val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo = am.getRunningTasks(1)
        Log.d("topActivity", "CURRENT Activity ::" + taskInfo[0].topActivity?.className)
        //        ComponentName componentInfo = ;
        return taskInfo[0].topActivity?.className.toString()
    }

    fun launch(activity: Activity, activityTostart: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, activityTostart)
        intent.putExtra(AppConst.BUNDLE, bundle)
        activity.startActivity(intent)
    }

    fun launchBringFront(activityTostart: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, activityTostart)
        intent.putExtra(AppConst.BUNDLE, bundle)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        activity.startActivity(intent)
    }

    fun launch(activityTostart: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, activityTostart)
        intent.putExtra(AppConst.BUNDLE, bundle)
        activity.startActivity(intent)
    }


    fun launchNewFirstActivity(activityTosrat: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, activityTosrat)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(AppConst.BUNDLE, bundle)
        startActivity(intent)
        finish()
    }


    fun launchActivityForResult(activityTosrat: Class<*>, bundle: Bundle?, requestCode: Int) {
        activity.startActivityForResult(
            Intent(activity, activityTosrat).putExtra(AppConst.BUNDLE, bundle),
            requestCode
        )

    }

    fun showErrorAboveKeypadSnackBar(view: View?, s: String) {
        try {
//                hideKeyboard()
            val snack = Snackbar.make(view!!, s, Snackbar.LENGTH_LONG)
            val sbview = snack.view
            sbview.setBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    android.R.color.holo_red_dark
                )
            )
            val textView = sbview.findViewById<View>(R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(activity, android.R.color.white))
            snack.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    inline fun <reified T> parseArray(json: String, typeToken: Type): T {

        val gson = GsonBuilder().create()
        return gson.fromJson<T>(json, typeToken)
    }

    fun getCurrentFragment(): Fragment? {
        val currentFragment = supportFragmentManager
            .findFragmentById(R.id.container)
        return currentFragment
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }


    var isDialogRunningForvideo: Boolean = false

    fun adapter(provinceList: MutableList<*>): ArrayAdapter<Any> {

        val adapter =
            ArrayAdapter<Any>(this, android.R.layout.simple_spinner_dropdown_item, provinceList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter

    }

    fun adapter(stringArray: Array<String>): ArrayAdapter<Any> {
        val adapter =
            ArrayAdapter<Any>(this, android.R.layout.simple_spinner_dropdown_item, stringArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            imageFileName,
            null
        )
        return Uri.parse(path)
    }


    private fun requestForCallPermission(redirectToCall: RedirectToCall) {
        Dexter.withContext(this)
            .withPermission(
                Manifest.permission.CALL_PHONE
            ).withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    redirectToCall.callRedirection()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                }
            }).check()
    }

    interface RedirectToCall {
        fun callRedirection()
    }

    //
//
    fun showProgressDialog(show: Boolean) {
        try {
            if (isDialogRunningForvideo) {
                return
            }
            //Show Progress bar here
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun showProgressDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = Dialog(this)
            } else {
                return
            }
            val view = LayoutInflater.from(this).inflate(R.layout.app_loading_dialog, null, false)

//        val imageView1 = view.findViewById<View>(R.id.imageView2) as ImageView
//        val a1 = AnimationUtils.loadAnimation(this, R.anim.progress_anim)
//        a1.duration = 1500
//        imageView1.startAnimation(a1)


            progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog!!.setContentView(view)
            val window = progressDialog!!.window
            window?.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this,
                    android.R.color.transparent
                )
            )
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            if (!progressDialog!!.isShowing && !activity.isFinishing)
                progressDialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }


    fun onBackPress(view: View) {
        onBackPressed()
    }

    object ActivityHandler {
        private var screenStack: HashMap<String, Activity>? = null

        // Add activity
        fun addActivities(actName: String, _activity: Activity?) {
            if (screenStack == null) {
                screenStack = HashMap()
            }

            if (_activity != null && !screenStack!!.containsKey(actName))
                screenStack!![actName] = _activity
        }

        // Remove Activity
        fun removeActivity(key: String) {
            if (screenStack != null && screenStack!!.size > 0) {
                val _activity = screenStack!![key]
                if (_activity != null) {
                    _activity.finish()
                    screenStack!!.remove(key)
                }

            }
        }
    }


    companion object {


        fun launchActivityForResult(
            activity: Activity,
            activityTosrat: Class<*>,
            requestCode: Int
        ) {
            activity.startActivityForResult(Intent(activity, activityTosrat), requestCode)
        }

        const val REQUEST_VIDEO_CAPTURE = 1
        const val ACTION_TAKE_VIDEO_RESULT_CODE = 121
    }


    //Kaushal

    protected open fun bindView(layout: Int): ViewDataBinding? {
        return DataBindingUtil.setContentView(this, layout)
    }
    //region Methods Toolbar
//    open fun onInitToolbar(toolBar: Toolbar?): Unit {
//        onInitToolbar(toolBar, getString(R.string.clear), -1, false)
//    }

    open fun onInitToolbar(toolBar: Toolbar?, title: String?) {
        onInitToolbar(toolBar, title, -1, false)
    }

    open fun onInitToolbar(toolBar: Toolbar?, title: Int) {
        onInitToolbar(toolBar, title, -1, false)
    }

    open fun onInitToolbar(toolBar: Toolbar?, title: Int, icon: Int) {
        onInitToolbar(toolBar, getString(title), icon, true)
    }

    open fun onInitToolbar(
        toolBar: Toolbar?,
        title: String?,
        displayHome: Boolean
    ) {
        onInitToolbar(toolBar, title, -1, displayHome)
    }

    open fun onInitToolbar(
        toolBar: Toolbar?,
        title: Int,
        displayHome: Boolean
    ) {
        onInitToolbar(toolBar, title, -1, displayHome)
    }

    open fun onInitToolbar(
        toolBar: Toolbar?,
        title: Int,
        icon: Int,
        displayHome: Boolean
    ) {
        onInitToolbar(toolBar, getString(title), icon, displayHome)
    }

    open fun onInitToolbar(
        toolBar: Toolbar?,
        title: String?,
        icon: Int,
        displayHome: Boolean
    ) {
        if (toolBar != null) {
            setSupportActionBar(toolBar)
            val actionBar: ActionBar? = supportActionBar
            if (actionBar != null) {
                actionBar.title = title
                actionBar.setDisplayShowHomeEnabled(displayHome)
                actionBar.setDisplayHomeAsUpEnabled(displayHome)
                if (icon != -1 && displayHome) {
                    toolBar.navigationIcon = ContextCompat.getDrawable(this, icon)
                }
            }
        }
    }


    private fun getFileNameOnly(fileName: String): String {
        return fileName.substring(0, fileName.lastIndexOf(".") - 1)
    }

    private fun getFileExtensionOnly(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length)
    }

}