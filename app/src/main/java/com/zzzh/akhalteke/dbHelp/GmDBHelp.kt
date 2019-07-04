package com.zzzh.akhalteke.dbHelp

import android.content.Context
import com.tencent.wcdb.database.SQLiteDatabase
import com.tencent.wcdb.database.SQLiteOpenHelper
import com.zzzh.akhalteke.bean.DataMessageVo

class GmDBHelp(
    context: Context?
) : SQLiteOpenHelper(context, DEFAULT_NAME, DataMessageVo.PASSWORD, null, null, DEFAULT_VERSION,
    null) {

    companion object {
        val DEFAULT_NAME: String = "skmd.db"
        val TABLE_NAME: String = "carinfom"
        var DEFAULT_VERSION: Int = 1;
        private var singletonInstance:GmDBHelp ?= null
        @Synchronized fun getInstance(m:Context):GmDBHelp?{
            if (singletonInstance == null){
                singletonInstance = GmDBHelp(m)
            }
            return singletonInstance
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var carInfom = "create table " + TABLE_NAME + " (" +
                "id integer primary key autoincrement," +
                "token text," +
                "userId text," +
                "phone text," +
                "ifReal text," +//实名认证 1/已经2未
                "ifDriver text," +//驾驶员认证
                "ifCar text," +//车辆认证
                "name text," +//姓名
                "idnumber text," +//身份证号码
                "hear text," +//头像
                "plateNumber text," +//车牌号
                "role text," +//1为正驾2为副驾
                "guider text," +//是否查看过启动界面
                "carPlateColourId text," +//车牌颜色，蓝色-1，黄色-2，绿色-3，黄绿色-4
                "extend text," +//809 1 是 2 否
                "extend_one text," +
                "extend_two text," +
                "extend_three text," +
                "extend_four text" +
                ");"
        db!!.execSQL(carInfom)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}