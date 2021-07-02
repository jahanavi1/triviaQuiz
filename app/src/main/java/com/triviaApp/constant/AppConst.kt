package com.triviaApp.constant

import android.content.Context
import com.triviaApp.BuildConfig
import com.triviaApp.utils.Prefs


object AppConst {



    var LAT = "LAT"
    var LANG = "LANG"
    val DISPLAY_LOCATION = "display_location"
    val CITY2 = "city2"
    val ZIP_CODE = "zip_code"
    val FORMETTED_ADDRESS = "formatted_address"
    val LATITUED = "latitude"
    val LONGITUDE = "longitude"
    val COUNTRY = "country"
    val CITY = "city"
    var NAME = "name"

    val BUNDLE = "DATABUNDLE"



    var MIN_PRICE = ""
    var MAX_PRICE = ""
    var CURRENCY = ""




    // sort by constants
    val SORTBY_PRICE = "price"
    val SORTBY_UPDATED_DATE = "updatedDate"
    val SORTBY_ID = "id"

    val PRODUCT_DELIVERED = "PRODUCT_DELIVERED"
    //    val BANK_ACCOUNT_VERIFICATION_PENDING ="BANK_ACCOUNT_VERIFICATION_PENDING"
    val STATUS_COMPLETED = "COMPLETED"
    val STATUS_CANCELLED = "CANCELLED"
    val BANK_ACCOUNT_VERIFICATION_PENDING = "BANK_ACCOUNT_VERIFICATION_PENDING"
    val SENT_CANCEL ="Product Buy Request Cancel"
    val LAYPAY_DETAILS_CANCEL ="Product Purchase Cancel"
    val ONGOING ="ONGOING"
    val MY_LISTING = "MY_LISTING"
    val MY_LAYPAYMENT = "MY_LAYPAYMENT"



    var GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/"
//    val GOOGLE_KEY = "AIzaSyDja_vK-nstYOLKGh2tg2wWOpkW95C1g10"
    val GOOGLE_KEY = "AIzaSyDefsNgMWKEDpoQyY38E5GBN8s_i6RRbwc"

    fun getAddressInfo(place_id: String): String {
        return "place/details/json?place_id=$place_id&fields=name,geometry,formatted_address,address_components&key=$GOOGLE_KEY"
    }

}