package com.example.testapp.common

import android.content.Context
import android.content.SharedPreferences
import com.example.testapp.common.Constants.MY_TOKEN
import com.example.testapp.common.Constants.USER_ADDRESS
import com.example.testapp.common.Constants.USER_BIRTHDAY
import com.example.testapp.common.Constants.USER_GENDER
import com.example.testapp.common.Constants.USER_NAME
import com.example.testapp.common.Constants.USER_PHONE

object MySettings {
    private var appContext: Context? = null
    private var instance: MySettings? = null

    fun initInstance(con: Context?) {
        appContext = con
        if (instance == null) instance = MySettings
    }
    fun setContext(context:Context){
        this.appContext = context
    }
   fun getInstance(): MySettings? {
        if (instance == null) instance = MySettings
        return instance
    }

    fun getToken(): String? {
        val preferences: SharedPreferences =
            appContext!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return preferences.getString(MY_TOKEN, "")
    }
    fun setToken(token: String?) {
        val preferences: SharedPreferences =
            appContext!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(MY_TOKEN, token)
        editor.apply()
    }
    fun getPhone(): String? {
        val preferences: SharedPreferences =
            appContext!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return preferences.getString(USER_PHONE, "")
    }
    fun setPhone(phone: String?) {
        val preferences: SharedPreferences =
            appContext!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(USER_PHONE, phone)
        editor.apply()
    }
    fun getName(): String? {
        val preferences: SharedPreferences =
            appContext!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return preferences.getString(USER_NAME, "")
    }
    fun setName(userName: String?) {
        val preferences: SharedPreferences =
            appContext!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(USER_NAME, userName)
        editor.apply()
    }
    /*fun setImage(userImage : String?){
        val preferences : SharedPreferences =
            appContext!!.getSharedPreferences("settings",Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(USER_IMAGE,userImage)
        editor.apply()
    }
    fun getImage(): String?{
        val preferences : SharedPreferences =
            appContext!!.getSharedPreferences("settings",Context.MODE_PRIVATE)
        return preferences.getString(USER_IMAGE,"")
    }*/
    fun getUserGender():String?{
        val preferences : SharedPreferences =
            appContext!!.getSharedPreferences("settings",Context.MODE_PRIVATE)
        return preferences.getString(USER_GENDER,"")
    }
    fun setUserGender(userGender : String?){
        val preferences : SharedPreferences =
            appContext!!.getSharedPreferences("settings",Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(USER_GENDER,userGender)
        editor.apply()
    }
    fun getUserAddress():String?{
        val preferences : SharedPreferences =
            appContext!!.getSharedPreferences("settings",Context.MODE_PRIVATE)
        return preferences.getString(USER_ADDRESS,"")
    }
    fun setUserAddress(userAddress:String?){
        val preferences : SharedPreferences =
            appContext!!.getSharedPreferences("settings",Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(USER_ADDRESS,userAddress)
        editor.apply()
    }
    fun getUserBirthday():String?{
        val preferences : SharedPreferences =
            appContext!!.getSharedPreferences("settings",Context.MODE_PRIVATE)
        return preferences.getString(USER_BIRTHDAY,"")
    }
    fun setUserBirthday(userBirthday : String?){
        val preferences : SharedPreferences =
            appContext!!.getSharedPreferences("settings",Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(USER_BIRTHDAY,userBirthday)
        editor.apply()
    }
}