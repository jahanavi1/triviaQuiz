package com.triviaApp.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.triviaApp.R
import com.triviaApp.constant.AppConst
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("UNREACHABLE_CODE", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("SimpleDateFormat")
public class AppUtils {
    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
            var strAdd = ""
            val geocoder = Geocoder(activity, Locale.getDefault())
            try {
//                val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                if (addresses != null) {
                    val returnedAddress = addresses.get(0)
                    val strReturnedAddress = StringBuilder("")
                    for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    }
                    strAdd = strReturnedAddress.toString()
                    Log.w("Location", strReturnedAddress.toString())
                } else {
                    Log.w("Location", "No Address returned!")
                }
            } catch (e: Exception){
                e.printStackTrace()
                Log.w("Location", "Canont get Address!")
            }
            return strAdd
        }

        fun getPostalCode(LATITUDE: Double, LONGITUDE: Double): String {
            var strAdd = ""
            val geocoder = Geocoder(activity, Locale.getDefault())
            try {
//                val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                if (addresses != null) {
                    val returnedAddress = addresses.get(0).postalCode
                    val strReturnedAddress = StringBuilder("")
//                    for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
//                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
//                    }
                    strAdd = returnedAddress.toString()
                    Log.w("Location", strReturnedAddress.toString())
                } else {
                    Log.w("Location", "No Address returned!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.w("Location", "Canont get Address!")
            }
            return strAdd
        }


        lateinit var activity: AppCompatActivity

        fun setActivtiy(activtiy: AppCompatActivity) {
            activity = activtiy
        }

        @SuppressLint("DefaultLocale")
//        fun getLatLongFromZipcode(zipData:String):LatLng? {
//            val geocoder = Geocoder(activity)
////        val zip = "380050"
//            var message :String = ""
//            var messageLatLng :LatLng? = null
//
//            try {
//                val addresses: List<Address> = geocoder.getFromLocationName(zipData, 1)
//                if (!addresses.isEmpty()) {
//                    val address: Address = addresses[0]
//                    // Use the address as needed
//                    message = java.lang.String.format(
//                        "Latitude: %f, Longitude: %f",
//                        address.getLatitude(), address.getLongitude()
//                    )
//                    messageLatLng = LatLng(address.latitude,address.longitude)
////                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//                    Log.d("Lat-Lang","Lat-Lang"+message)
//
//                } else {
//                    // Display appropriate message when Geocoder services are not available
////                    Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show()
//                    AppUtils.showToast(activity, "Unable to geocode zipcode")
//                }
//            } catch (e: IOException) {
//                // handle exception
//            }
//            return messageLatLng
//        }

        fun uTCToLocalDateOnly(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
//            2021-01-26T09:37:22.000+0000
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }


//        fun getSeperatedAddres(addressInfo: PlaceAddressInfo): Map<String, String> {
//
//            val map = HashMap<String, String>()
//            try {
//
//                if (addressInfo.result == null) {
//                    return map;
//                }
////            map[AppConstant.ADDRESS] = addressInfo.result!!.formatted_address.toString().split(",")[0]
//
//                map[AppConst.NAME] = addressInfo.result!!.name.toString()
//                map[AppConst.LOCATIONS] = addressInfo.result!!.geometry!!.location!!.toString()
//                map[AppConst.FORMETTED_ADDRESS] = addressInfo.result!!.formatted_address.toString()
//                Log.d(
//                    "Location address name",
//                    addressInfo.result!!.formatted_address.toString().split(",")[0]
//                )
//
////            map[AppConst.ADDRESS] = addressInfo.result!!.name.toString()
////            Log.d("Location address name", addressInfo.result!!.formatted_address.toString().split(",")[0])
//
//                map[AppConst.LATITUED] = addressInfo.result!!.geometry!!.location!!.lat.toString()
//                Log.d(
//                    "Location LATITUED",
//                    addressInfo.result!!.geometry!!.location!!.lat.toString()
//                )
//
//                map[AppConst.LONGITUDE] = addressInfo.result!!.geometry!!.location!!.lng.toString()
//                Log.d(
//                    "Location LONGITUDE",
//                    addressInfo.result!!.geometry!!.location!!.lng.toString()
//                )
//
//                for (item in addressInfo.result!!.address_components!!) {
//                    when {
//                        item.types!!.contains("country") -> {
//                            map[AppConst.COUNTRY] = item.long_name.toString()
//                            map[AppConst.COUNTRY_CODE] = item.short_name.toString()
//                            Log.d("Location Country", item.long_name!!)
//                        }
//
//                        item.types.contains("administrative_area_level_2") -> {
//                            Log.d("Location City", item.long_name!!)
//                            map[AppConst.CITY2] = item.long_name.toString()
//                        }
//
//                        item.types.contains("locality") -> {
//                            Log.d("Location City", item.long_name!!)
//                            map[AppConst.CITY] = item.long_name.toString()
//                        }
//
//                        item.types.contains("administrative_area_level_1") -> {
//                            Log.d("Location State", item.long_name!!)
//                            map[AppConst.STATE] = item.long_name.toString()
//                        }
//
//                        item.types.contains("postal_code") -> {
//                            Log.d("postal_code", item.long_name!!)
//                            map[AppConst.ZIP_CODE] = item.long_name.toString()
//                            Log.d("Zipcode", item.long_name.toString())
//
//                        }
//
//                        item.types.contains("sublocality_level_1") -> {
//                            map[AppConst.SUBLOCALITY] = item.long_name.toString()
//                            Log.d("Sublocality", item.long_name.toString())
//                        }
//
//                        item.types.contains("route") -> {
//                            map[AppConst.ROUTE] = item.long_name.toString()
//                            Log.d("Route", item.long_name.toString())
//                        }
//                    }
//                }
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.w("Location", "Canont get Address!")
//            }
//            return map
//        }

        fun convertTime(time: Long): String {
            val date = Date(time)
            val format: Format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(date)
        }

        /*fun convertTime(time: String): String {
            val date = Date(time)
            val format: Format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(date)
        }*/


        fun meterToMiles(meter: Double): Double {
            try {
                return meter / 1609.344
            } catch (e: Exception) {
                return 0.0
            }
        }



        fun displayMobileFormat(contact: String): String {
            try {
                var mobileFormat = "("
                var mobile = contact.substring(0, 3)
                mobileFormat = mobileFormat + mobile + ")"
                mobileFormat = mobileFormat + " " + contact.substring(3, 7)
                mobileFormat = mobileFormat + " " + contact.substring(7, 10)
                return mobileFormat
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun getZipCode(latitude: Double, longitude: Double, i: Int) : String {
            val geocoder: Geocoder
            val addresses: List<Address>
            var zipcode :String = ""
            geocoder = Geocoder(activity, Locale.getDefault())
            addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses.size > 0 && !addresses.isEmpty()) {
                zipcode  = addresses[0].postalCode

            }

            return  zipcode

        }

        fun getCityCode(latitude: Double, longitude: Double, i: Int) : String {
            var sPlace:String = ""
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(activity, Locale.getDefault())
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val result = java.lang.StringBuilder()

            if (addresses.size > 0) {
                if(!addresses.get(0).subLocality.isNullOrEmpty()) {
                    val city1 = addresses.get(0).locality
                    val city: String = addresses.get(0).subLocality

                    Log.d("Area", "CITY" + city)
                    Log.d("CITY", "CITY" + city1)

//                val splitAddress = address.split(",".toRegex()).toTypedArray()
                    sPlace = city + ", " + city1
                }
                else{
                    val city1 = addresses.get(0).locality
                    val city: String = ""

                    Log.d("CITY", "CITY" + city1)

//                val splitAddress = address.split(",".toRegex()).toTypedArray()
                    sPlace = city + city1
                }
//                sPlace = """
//                ${splitAddress[0]}
//
//                """.trimIndent()
//                if (city != null && !city.isEmpty()) {
//                    val splitCity = city.split(",".toRegex()).toTypedArray()
//                    sPlace += splitCity[0]
//                }
            }

          /*  val city = addresses[0].locality
            val city1 = addresses[0].subLocality
*/
            return sPlace


        }

        fun displayMobileDashFormat(contact: String): String {
            try {
                var mobileFormat = ""
                var mobile = contact.substring(0, 3)
                mobileFormat = mobileFormat + mobile
                mobileFormat = mobileFormat + "-" + contact.substring(3, 7)
                mobileFormat = mobileFormat + "-" + contact.substring(7, 10)
                return mobileFormat
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun displayMobile_spaceFormat(contact: String): String {
            try {
                var mobileFormat = ""
                var mobile = contact.substring(0, 3)
                mobileFormat = mobileFormat + mobile
                mobileFormat = mobileFormat + " " + contact.substring(3, 7)
                mobileFormat = mobileFormat + " " + contact.substring(7, 10)
                return mobileFormat
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun secondsToHours(seconds: Int): String {
            try {

                var hours = seconds / 3600
                var minutes = ((seconds / 60) - (hours * 60))
                return "$hours hour $minutes mins"
            } catch (e: Exception) {
                return ""
            }
        }

//        fun showErrorSnackBar(view: View?, s: String) {
//            try {
//                val snack = Snackbar.make(view!!, s, Snackbar.LENGTH_LONG)
//                val sbview = snack.view
//                sbview.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.holo_red_dark))
//                val textView = sbview.findViewById<View>(R.id.snackbar_text) as TextView
//                textView.setTextColor(ContextCompat.getColor(activity, android.R.color.white))
//                snack.show()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }

        fun showErrorSnackBar(s: String) {
            try {
                val snack =
                    Snackbar.make(activity.window.decorView.rootView, s, Snackbar.LENGTH_LONG)
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

//        fun hideKeyboard()
//        {
//            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//            if (inputMethodManager != null) {
//                inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0)
//            }
//        }


        fun showSnackBar(s: String) {
            val snack = Snackbar.make(activity.window.decorView.rootView, s, Snackbar.LENGTH_LONG)
            val sbview = snack.view
            sbview.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.black))
            val textView = sbview.findViewById<View>(R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(activity, android.R.color.white))

//            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(activity.window.decorView.rootView.getWindowToken(), 0)

            snack.show()


        }

        object ShowSnackBar {
            fun show(context: Context, message: String, view: View) {
                val imm =
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
            }
        }


        fun hasInternet(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return cm.activeNetworkInfo != null
        }

        fun hideKeyboard(ctx: Activity) {
            if (ctx.currentFocus != null) {
                val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(ctx.currentFocus!!.windowToken, 0)
            }
        }

        fun snackBarWithActivityFinish(activity: Activity, msg: String) {
//            val snack = Snackbar.make(Companion.activity.window.decorView.rootView, msg, 800)
            val sbview = activity.window.decorView.rootView
            val snackbar = Snackbar
                .make(sbview, msg, 1200)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(snackbar: Snackbar?, event: Int) {
                        activity.finish()
                    }

                    override fun onShown(snackbar: Snackbar?) {

                    }
                })
            snackbar.show()
        }


        fun convertStringToRequestBodyHashmap(params: HashMap<String, String>): HashMap<String, RequestBody> {
            val bodyHashMap = HashMap<String, RequestBody>()
            val keys = params.keys
            for (key in keys) {
                bodyHashMap[key] = getRequestBody(params[key])
            }
            return bodyHashMap
        }

        fun getRequestBody(value: String?): RequestBody {
//            return RequestBody.create(MediaType.parse("multipart/form-data"), value!!)
            return value.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        @JvmStatic
        fun getDiff(date1: String, date2: String): String {
            try {

                var secs = (Date(date1).getTime() - Date(date2).getTime()) / 1000;
                var hours = secs / 3600
                secs = secs % 3600
                var mins = secs / 60
                secs = secs % 60

                Log.i("TEst", "Test time  " + hours + ":" + mins)
                return "$hours:$mins"
            } catch (e: Exception) {
                e.printStackTrace();
            }
            return ""
        }

        @JvmStatic
        fun convertDateFromApi(dateStr: String): String {
            var date: Date? = null
            try {
                if (dateStr.isNotEmpty())
                    date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return if (date != null)
                SimpleDateFormat("MM/dd/yyyy h:mm a").format(date)
            else ""

        }

        fun parseDateTimeWithoutConvertTimeZone(time: String): String? {
            val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val outputPattern = "MM/dd/yyyy h:mm a"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str

        }


//        @JvmStatic
//        fun getDigits(): String {
//            val str = "abc d 1234567890pqr 54897"
//            val myNumbers = StringBuilder()
//            for (i in 0 until str.length) {
//                if (Character.isDigit(str[i])) {
//                    myNumbers.append(str[i])
//                    println(str[i] + " is a digit.")
//                } else {
//                    println(str[i] + " not a digit.")
//                }
//            }
//            return ("Your numbers: $myNumbers")
//        }


        private var uniqueID: String? = ""
        private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

        @Synchronized
        fun getUUID(context: Context): String {
            if (uniqueID!!.isEmpty()) {
                val sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE
                )
                uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
                if (uniqueID == null) {
                    uniqueID = UUID.randomUUID().toString()
                    val editor = sharedPrefs.edit()
                    editor.putString(PREF_UNIQUE_ID, uniqueID)
                    editor.apply()
                }
            }
            return uniqueID as String
//            return "0Xylasw";
        }




//        fun convertTime(time: Long): String {
//            val date = Date(time)
//            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            return format.format(date)
//        }
//
//
//        fun parseDate(serverDate: Long): String? {
//            val server_date = convertTime(serverDate)
//            val df = SimpleDateFormat("yyyy MM dd hh:mm:ss", Locale.ENGLISH)
//            df.timeZone = TimeZone.getTimeZone("UTC")
//            val date: Date?
//            return try {
//                date = df.parse(server_date)
//                df.timeZone = TimeZone.getDefault()
//                val formattedDate = df.format(date)
//                var spf = SimpleDateFormat("yyyy MM dd hh:mm:ss")
//                val newDate = spf.parse(formattedDate)
//                spf = SimpleDateFormat("dd MMM,yyyy HH:mm a")
//                spf.format(newDate)
//            } catch (e: ParseException) {
//                e.printStackTrace()
//                null
//            }
//
//        }


        fun hoursDifference(date1: Date, date2: Date): Long {

            var MILLI_TO_HOUR = 1000 * 60 * 60;
            return (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
        }

        fun longToStringDate(timestamp: Long): String {

            var date = Date(timestamp)
            var df2 = SimpleDateFormat("MM/dd/yyyy");
            var dateText = df2.format(date);
            return dateText
        }

        fun longDate(timestamp: Long): String {

            var df2 = SimpleDateFormat("MM/dd/yyyy HH:mm")
            var date = Date(timestamp)

            var dateText = df2.format(date);
            return dateText
        }


        fun parseDateFromAPIWithoutConvertTimeZone(time: String): String {
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = "MM/dd/yyyy"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String = ""

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str

        }


        fun parseOnlyTime(time: String): String? {
            val inputPattern = "HH:mm"
            val outputPattern = "hh:mm a"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str

        }


        fun convertToDateOnly(dateStr: String): String? {
            var date: Date
            try {
                date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ").parse(dateStr)

                return SimpleDateFormat("dd MMM, yyyy").format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }

        fun get24To12HrsFormat(time: String): String {
            var time1: String
            try {
                val sdf = SimpleDateFormat("HH:mm")
                val dateObj = sdf.parse(time)
                time1 = SimpleDateFormat("hh:mm aa").format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            return time1
        }

        fun getTimeAgo(date: String): String {

            val dayOfDate: Long
            val suffix = "ago"
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ")

            var past: Date? = null
            try {
                past = sdf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val calendar = Calendar.getInstance()
            calendar.time
            var time = past!!.time
            if (time < 1000000000000L) {
                time *= 1000
            }

            val now = currentDate().time
            if (time > now || time <= 0) {
                return "Just Now"
            }

            val diff = now - time
            val second = TimeUnit.MILLISECONDS.toSeconds(diff)
//    val minute = TimeUnit.MILLISECONDS.toMinutes(diff)
//    val hour = TimeUnit.MILLISECONDS.toHours(diff)
            val day = TimeUnit.MILLISECONDS.toDays(diff)

            if (second < 60) {
                return "$second seconds $suffix"
            }
            if (diff < MINUTE_MILLIS) {
                return "Just Now"
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "A minute ago"
            } else if (diff < 60 * MINUTE_MILLIS) {
                return (diff / MINUTE_MILLIS).toString() + " minutes ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                return (diff / HOUR_MILLIS).toString() + " hours ago"
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday"
            } else if (day >= 7) {
                if (day > 360) {
                    dayOfDate = day / 360
                    if (dayOfDate == 1L)
                        return (day / 360).toString() + " year " + suffix
                    else
                        return (day / 360).toString() + " years " + suffix

                } else if (day > 30) {
                    dayOfDate = day / 30
                    if (dayOfDate == 1L)
                        return (day / 30).toString() + " month " + suffix
                    else
                        return (day / 30).toString() + " months " + suffix

                } else {
                    dayOfDate = day / 7
                    if (dayOfDate == 1L)
                        return (day / 7).toString() + " week " + suffix
                    else
                        return (day / 7).toString() + " weeks " + suffix
                }
            } else if (day < 7) {
                return "$day Days $suffix"
            } else {
                return ""
            }

            return ""
        }

        fun getTimeORDate(datesToConvert: String):String{
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val dateformat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
//            dateformat.timeZone = TimeZone.getTimeZone("UTC")
            val datetimeFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US)
//            datetimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
//            timeFormat.timeZone = TimeZone.getTimeZone("UTC")

            try {
                val gmt = sdf.parse(datesToConvert)!!
                val gmtText = dateformat.format(gmt)
                val today = Calendar.getInstance().time
                val todayText = dateformat.format(today)
                if(gmtText == todayText)
                    dateToReturn = timeFormat.format(gmt)
                else
                    dateToReturn = datetimeFormat.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return dateToReturn
        }
        fun uTCToLocal(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US)
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }




        fun getDeciamlToTwo(value: Double): Double {
            val precision = DecimalFormat("0.00")
            precision.roundingMode = RoundingMode.UP
            return java.lang.Double.parseDouble(precision.format(value))

        }

        fun formatTwoDecimalPlace(value: Double): String {
            return String.format("%.2f", value)
        }


        fun StringFormat(value: Float): Int {
            val data: Int = value.toInt()
            return data
        }


//
//
//        fun getTimeFromString(dateStr: String): String {
//            var date: Date? = null
//            try {
//                date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateStr)
//            } catch (e: ParseException) {
//                e.printStackTrace()
//            }
//
//            return SimpleDateFormat("Hh:mm").format(date)
//
//        }


        fun apiDateFormat(dateStr: String): String? {
            val date: Date?
            try {
                date = SimpleDateFormat("MM/dd/yyyy").parse(dateStr)

                return SimpleDateFormat("MMMM dd,yyyy").format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }

        fun isValidDateRange(
            strStartDate: String, strEndDate: String,
            equalOK: Boolean
        ): Boolean {
            // false if either value is null
            val startDate = stringToDate(strStartDate)
            val endDate = stringToDate(strEndDate)
            if (startDate == null || endDate == null) {
                return false
            }

            if (equalOK) {
                // true if they are equal
                if (startDate == endDate) {
                    return true
                }
            }

            if (startDate.after(endDate)) return false

            // true if endDate after startDate
            return if (endDate.after(startDate)) {
                true
            } else false

        }

        fun stringToDate(dateStr: String): Date? {

            val formatter = SimpleDateFormat("yyyy-MM-dd")
            var date: Date? = null
            try {
                date = formatter.parse(dateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }

        @SuppressLint("NewApi")
        @RequiresApi(Build.VERSION_CODES.N)
        fun convertStringTodate(dateStr: String): Date? {

            val formatter = SimpleDateFormat("MM/DD/YYYY")
            var date: Date? = null
            try {
                date = formatter.parse(dateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }

        private val TAG = "AppUtils"


        fun cancelNotification(context: Context) {
            val notifManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifManager.cancelAll()
        }

        private val SECOND_MILLIS = 1000
        private val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private val HOUR_MILLIS = 60 * MINUTE_MILLIS

        private fun currentDate(): Date {
            val calendar = Calendar.getInstance()
            return calendar.time
        }

        fun getTrimedMobile(countryCode: String, mNumber: String): String {
            var mobileNumber = mNumber
            if (mobileNumber.contains(countryCode)) {
                mobileNumber = mobileNumber.substring(countryCode.length)
            }
            println(">>>$mobileNumber")
            return mobileNumber
        }

        fun notificationTime(date: Long, createdDate: String): String {
            val smsTime = Calendar.getInstance()
            smsTime.timeInMillis = date

            val now = Calendar.getInstance()
            return when {
                now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) -> {
                    "Today "
                }
                now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1 -> {
                    "Yesterday "
                }
                else -> {
                    AppUtils.uTCToLocal(createdDate)


                }
            }
        }

        fun isYesterday(d: Date): Boolean {
            return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
        }

        fun getLocationFromLatLong(latitude: Double, longitude: Double): String {
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(activity, Locale.getDefault())

            try {
                addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                if (addresses.size > 0) {
                    val city = addresses[0].locality
                    val state = addresses[0].adminArea
                    val country = addresses[0].countryCode
                    val featureName = addresses[0].featureName
                    val subLocality = addresses[0].subLocality

                    return if (city != null) {
                        if (featureName != null) {
                            if (subLocality != null)
                                "$featureName, $subLocality, $city, $state, $country"
                            else
                                "$featureName, $city, $state, $country"
                        } else {
                            if (subLocality != null)
                                "$subLocality, $city, $state, $country"
                            else
                                "$city, $state, $country"
                        }

                    } else {
                        "$featureName, $subLocality, $state, $country"
                    }
                } else {
                    return ""
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }


        }

        fun isRunning(activityClass: Class<*>, context: Context): Boolean {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val tasks = activityManager.getRunningTasks(Integer.MAX_VALUE)

            for (task in tasks) {
                if (activityClass.canonicalName!!.equals(
                        task.baseActivity?.className,
                        ignoreCase = true
                    )
                )
                    return true
            }

            return false
        }

        fun parseTime(time: String): String {
            try {
                val _24HourSDF = SimpleDateFormat("HH:mm")
                val _12HourSDF = SimpleDateFormat("hh:mm a")
                val _24HourDt = _24HourSDF.parse(time)
                return _12HourSDF.format(_24HourDt)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }


        fun getDayOfMonthSuffix(n: Int): String? {

            return if (n in 11..13) {
                "th"
            } else when (n % 10) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
        }

        /**
         * phone : 10 digit phone nunmber
         * xxx-xxx-xxxx
         */
        fun formatPhoneWithDash(phone: String): String {
            if (phone.length != 10) {
                return phone
            } else {
                val part1 = phone.substring(0, 3)
                val part2 = phone.substring(3, 6)
                val part3 = phone.substring(6, phone.length)
                return "$part1-$part2-$part3"
            }
        }

        fun formatPhoneWithDashAndBracket(phone: String): String {
            return "(${formatPhoneWithDash(phone)})"
        }

        fun setDrawableSelector(
            context: Context,
            normal: Int,
            selected: Int
        ): Drawable? {
            val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
            val state_normal =
                ContextCompat.getDrawable(context, normal)
            val state_pressed =
                ContextCompat.getDrawable(context, selected)
            val state_normal_bitmap: Bitmap
            try {
                state_normal_bitmap = Bitmap.createBitmap(
                    state_normal!!.intrinsicWidth,
                    state_normal.intrinsicHeight,
                    BITMAP_CONFIG
                )
                val canvas = Canvas(state_normal_bitmap)
                state_normal.setBounds(0, 0, canvas.width, canvas.height)
                state_normal.draw(canvas)
            } catch (e: OutOfMemoryError) {
                // Handle the error
                return null
            }


            // Setting alpha directly just didn't work, so we draw a new bitmap!
            val disabledBitmap = Bitmap.createBitmap(
                state_normal.intrinsicWidth,
                state_normal.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(disabledBitmap)
            val paint = Paint()
            paint.alpha = 126
            canvas.drawBitmap(state_normal_bitmap, 0f, 0f, paint)
            val state_normal_drawable =
                BitmapDrawable(context.resources, disabledBitmap)
            val drawable =
                StateListDrawable()
            drawable.addState(
                intArrayOf(android.R.attr.state_selected),
                state_pressed
            )
            drawable.addState(
                intArrayOf(android.R.attr.state_enabled),
                state_normal_drawable
            )
            return drawable
        }
    }
}
