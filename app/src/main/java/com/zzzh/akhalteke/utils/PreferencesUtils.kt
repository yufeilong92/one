package com.zzzh.akhalteke.utils

import android.content.Context
import com.zzzh.akhalteke.BaseApplication
import org.json.JSONException
import org.json.JSONObject


class PreferencesUtils {

    val preferences = BaseApplication.toInstance().getSharedPreferences("lipo_zzzh_diver", Context.MODE_PRIVATE)

//
//    /**
//     * 注册登录时保存userInfo
//     */
//    fun shareUserInfo(userInfo: UserInfo?) {
//        Constant.userInfo = userInfo
//        val editor = preferences.edit()
//        if (userInfo == null) {
//            editor.putString("lipo_tnuser", "")
//        } else {
//            editor.putString("lipo_tnuser", JSON.toJSONString(userInfo))
//        }
//        editor.apply()
//    }
//
//    /**
//     * 更新user信息
//     */
//    fun updateUserInfo() {
//        val editor = preferences.edit()
//        editor.putString("lipo_tnuser", JSON.toJSONString(Constant.userInfo))
//        editor.apply()
//    }
//
//    /**
//     * 初始化的时候获取userInfo信息
//     */
//    fun toGetUserInfo(): UserInfo? {
//        val userString = preferences.getString("lipo_tnuser", "")
//        return if (userString == null || userString.isEmpty()) {
//            null
//        } else {
//            Gson().fromJson<UserInfo>(userString, UserInfo::class.java).also {
//                Constant.userInfo = it
//            }
//        }
//    }

    fun isFristApp(): Boolean {
        return preferences.getBoolean("tn_isFristApp", true)
    }

    fun toSetNoFristApp() {
        Constant.isFristApp = false
        val editor = preferences.edit()
        editor.putBoolean("tn_isFristApp", false)
        editor.apply()
    }

    /**
     * 保存启屏活动id
     */
    fun toSaveBehaveId(id:String){
        val editor = preferences.edit()
        editor.putString("tn_behave_id",id)
        editor.apply()
    }

    /**
     * 获取启屏活动id
     */
    fun toGetBehaveId():String{
        return preferences.getString("tn_behave_id","")
    }

    /**
     * 保存用户名和密码
     */
    fun toSaveNamePsd(name:String,password:String){
        if(name == null||password == null){
            return
        }
        val json = JSONObject()
        json.put("name",name)
        json.put("password",password)
        val editor = preferences.edit()
        editor.putString("name_password",json.toString())
        editor.apply()
    }

    /**
     * 获取用户名和密码
     */
    fun toGetNamePsd():JSONObject?{
        val jsonStr = preferences.getString("name_password","")
        if(ToolUtils.isEmpty(jsonStr)){
            return null
        }
        val json:JSONObject
        try {
            json = JSONObject(jsonStr)
        }catch (ex: JSONException){
            return null
        }
        return json
    }


    /**
     * 保存用户名和密码
     */
    fun saveFirstStartApp(){
        val editor = preferences.edit()
        editor.putBoolean("name",true)
        editor.apply()
    }   /**
     * 保存用户名和密码
     */
    fun getFirstStartApp():Boolean{
        val boolean = preferences.getBoolean("name",false)
        return boolean
    }

}