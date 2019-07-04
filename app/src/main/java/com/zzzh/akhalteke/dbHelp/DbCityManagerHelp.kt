package com.zzzh.akhalteke.dbHelp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import kotlin.Exception

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.dbHelp
 * @Email : yufeilong92@163.com
 * @Time :2019/5/10 12:05
 * @Purpose :打开db文件
 */
class DbCityManagerHelp(val mContext: Context) {
    private val DB_NAME = "weather_city.db"

    companion object {
        //被companion object包裹的语句都是private的
        private var singletonInstance: DbCityManagerHelp? = null

        @Synchronized
        fun getInstance(m: Context): DbCityManagerHelp? {
            if (singletonInstance == null) {
                singletonInstance = DbCityManagerHelp(m)
            }
            return singletonInstance
        }
    }

    fun openDb(): SQLiteDatabase {
        var path = mContext.externalCacheDir.absolutePath + File.separator + "/" + DB_NAME
        if (!File(path).exists()) {
            try {
                val file = FileOutputStream(path)
                val inputStream = mContext.assets.open("")
                var btye: ByteArray = byteArrayOf()
                var readBytes = 0
                while ((inputStream.read(btye).also { readBytes = it }) != -1) {
                    file.write(btye, 0, readBytes)
                }
                inputStream.close()
                file.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return SQLiteDatabase.openOrCreateDatabase(path, null)
    }

    fun quertCityCode(sqLiteDatabase: SQLiteDatabase, city: String): String {
        try {
            val cursor = sqLiteDatabase.query("city", null, "cityZh=?", arrayOf("$city"), null, null, null)
            if (cursor.moveToNext()) {
                val index = cursor.getColumnIndex("cityZh")
                val string = cursor.getString(index)
                return string
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}


