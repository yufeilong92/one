package com.zzzh.akhalteke.utils

import android.annotation.SuppressLint
import android.content.Context
import android.R.id.edit
import android.content.SharedPreferences
import java.io.IOException
import java.io.StreamCorruptedException


class SaveUUidUtil @SuppressLint("WrongConstant")
constructor(context: Context) {
    val sharedPref: SharedPreferences
    private var s_User: String? = null
    //获取序列化的数据
    val userId: String
        @Synchronized get() {
            if (s_User == null) {
                s_User = String()
                val str = sharedPref.getString(SaveUUidUtil.KEY_NAME, "")
                try {
                    val obj = SerializableUtil.str2Obj(str)
                    if (obj != null) {
                        s_User = obj as String?
                    }
                } catch (e: StreamCorruptedException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return s_User as String
        }

    init {
        sharedPref = context.getSharedPreferences(
            "data",
            Context.MODE_PRIVATE or Context.MODE_APPEND
        )
    }

    @Synchronized
    fun putUUID(vo: String) {
        val editor = sharedPref.edit()
        var str = ""
        try {
            str = SerializableUtil.obj2Str(vo)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        editor.putString(KEY_NAME, str)
        editor.commit()
        s_User = vo
    }

    @Synchronized
    fun delectUUid() {
        val editor = sharedPref.edit()
        editor.putString(KEY_NAME, "")
        editor.commit()
        s_User = null
    }

    companion object {
        // 用户名key
        val KEY_NAME = "useruuid"
        @get:Synchronized
        var instance: SaveUUidUtil? = null
            private set

        @Synchronized
        fun initSharedPreference(context: Context) {
            if (instance == null) {
                instance = SaveUUidUtil(context)
            }
        }
    }
}