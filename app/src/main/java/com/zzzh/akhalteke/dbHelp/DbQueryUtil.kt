package com.zzzh.akhalteke.dbHelp
import com.tencent.wcdb.Cursor

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/8 14:11
 * @Purpose :数据库帮助类
 */

  class DbQueryUtil {

     var mQuery: Cursor? = null

    fun initCursor(query: Cursor) {
        this.mQuery = query
    }

    fun queryInt(key: String): Int ?{
        val columnIndex = mQuery!!.getColumnIndex(key)
        return mQuery!!.getInt(columnIndex)
    }


    fun queryString(key: String): String? {
        val columnIndex = mQuery!!.getColumnIndex(key)
        return mQuery!!.getString(columnIndex)
    }

    fun queryBLOB(key: String): ByteArray? {
        val columnIndex = mQuery!!.getColumnIndex(key)
        return mQuery!!.getBlob(columnIndex)
    }

    fun querydouble(key: String): Double?{
        val columnIndex = mQuery!!.getColumnIndex(key)
        return mQuery!!.getDouble(columnIndex)
    }

    fun queryFloat(key: String): Float? {
        val columnIndex = mQuery!!.getColumnIndex(key)
        return mQuery!!.getFloat(columnIndex)
    }

    fun queryLong(key: String): Long? {
        val columnIndex = mQuery!!.getColumnIndex(key)
        return mQuery!!.getLong(columnIndex)
    }

    fun queryShort(key: String): Short? {
        val columnIndex = mQuery!!.getColumnIndex(key)
        return mQuery!!.getShort(columnIndex)
    }

}