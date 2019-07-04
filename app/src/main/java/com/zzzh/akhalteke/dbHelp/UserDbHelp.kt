package com.zzzh.akhalteke.dbHelp


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.tencent.wcdb.database.SQLiteDatabase
import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo
import com.zzzh.akhalteke.utils.StringUtil
import java.lang.Exception

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/8 14:31
 * @Purpose :用户信息帮助类、
 */

@SuppressLint("StaticFieldLeak")
object UserDbHelp {

    @Volatile
    private var _singleton: UserDbHelp? = null
    var mContext: Context? = null
    var mSqLiteDatabase: SQLiteDatabase? = null
    var mDbQueryUtil: DbQueryUtil? = null;
    fun UserDbHelp(context: Context): UserDbHelp? {
        this.mContext = context
        mSqLiteDatabase = createtable();
        mDbQueryUtil = DbQueryUtil()
        return this
    }

    fun get_Instance(context: Context): UserDbHelp? {
        if (_singleton == null) {
            synchronized(UserDbHelp::class.java) {
                if (_singleton == null) {
                    _singleton = UserDbHelp(context)
                }
            }
        }
        return _singleton
    }

    fun createtable(): SQLiteDatabase {
        var userHelp = GmDBHelp.getInstance(mContext!!)
        return userHelp!!.writableDatabase
    }

    //保存用户信息
    fun addUseInfom(
            id: String?, token: String?, phone: String?,
            ifReal: String?, ifDriver: String?,
            ifcar: String?, name: String?, number: String?, hear: String?,
            plateNumber: String?, role: String?, carPlateColourId: String?
    ) {
        isRead()
        mSqLiteDatabase!!.delete(GmDBHelp.TABLE_NAME, null, null)
        mSqLiteDatabase!!.beginTransaction()
        try {
            val conVal = ContentValues()
            conVal.put("userId", id)
            conVal.put("token", token)
            conVal.put("phone", phone)
            conVal.put("ifReal", ifReal)
            conVal.put("ifDriver", ifDriver)
            conVal.put("ifcar", ifcar)
            conVal.put("name", name)
            conVal.put("idnumber", number)
            conVal.put("hear", hear)
            conVal.put("plateNumber", plateNumber)
            conVal.put("role", role)
            conVal.put("carPlateColourId", carPlateColourId)
            mSqLiteDatabase!!.insert(GmDBHelp.TABLE_NAME, null, conVal)
            mSqLiteDatabase!!.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mSqLiteDatabase!!.endTransaction()
        }

    }

    /**
     * 更新头像
     */
    fun upHearInfom(hear: String?){
        isRead()
        val userInfom = getUserInfom()
        if (userInfom==null||StringUtil.isEmpty(userInfom.token)){
            return
        }
        mSqLiteDatabase!!.beginTransaction()
        try {
            val conVal = ContentValues()
            conVal.put("hear", hear)
            mSqLiteDatabase!!.update(GmDBHelp.TABLE_NAME, conVal, "userId=?",
                    arrayOf(userInfom.userId))
            mSqLiteDatabase!!.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mSqLiteDatabase!!.endTransaction()
        }
    }
    //保存用户信息
    fun updateUserInfom(
        id: String?, phone: String?,
        ifReal: String?, ifDriver: String?,
        ifcar: String?, name: String?, number: String?, hear: String?,
        plateNumber: String?, role: String?, carPlateColourId: String?, ifVert: String?
    ) {
        isRead()
        val userInfom = getUserInfom()
        if (userInfom == null || StringUtil.isEmpty(userInfom.token)) {
            return
        }
        mSqLiteDatabase!!.beginTransaction()
        try {
            val conVal = ContentValues()
            conVal.put("userId", id)
            conVal.put("phone", phone)
            conVal.put("ifReal", ifReal)
            conVal.put("ifDriver", ifDriver)
            conVal.put("ifcar", ifcar)
            conVal.put("name", name)
            conVal.put("idnumber", number)
            conVal.put("hear", hear)
            conVal.put("plateNumber", plateNumber)
            conVal.put("role", role)
            conVal.put("extend", ifVert)
            conVal.put("carPlateColourId", carPlateColourId)
            mSqLiteDatabase!!.update(GmDBHelp.TABLE_NAME, conVal, "userId=?",
                    arrayOf(userInfom.userId))
            mSqLiteDatabase!!.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mSqLiteDatabase!!.endTransaction()
        }

    }

    //更新实名认证状态
    fun upReal(str: String, name: String?, phone: String?, number: String?, portrait: String?) {
        isRead()
        val userInfom = getUserInfom()
        if (userInfom == null || StringUtil.isEmpty(userInfom.token)) {
            return
        }
        mSqLiteDatabase!!.beginTransaction()
        try {
            val contvalue = ContentValues()
            contvalue.put("ifReal", str)
            contvalue.put("name", name)
            contvalue.put("phone", phone)
            contvalue.put("idnumber", number)
            contvalue.put("hear", portrait)
            mSqLiteDatabase!!.update(
                    GmDBHelp.TABLE_NAME, contvalue, "userId=?"
                    , arrayOf(userInfom.userId)
            )
            mSqLiteDatabase!!.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mSqLiteDatabase!!.endTransaction()

        }

//        val newInfom = UserDbVo()
//        newInfom.ifReal = str
//        mSqLiteDatabase!!.delete(GmDBHelp.TABLE_NAME, null, null)
//        val copyVo = copyVo(userInfom, newInfom)
//        mSqLiteDatabase!!.insert(GmDBHelp.TABLE_NAME, null, copyVo)
    }

    //更新驾驶员证状态
    fun upDriver(str: String, role: String) {
        isRead()
        val userInfom = getUserInfom()
        if (userInfom == null || StringUtil.isEmpty(userInfom.token)) {
            return
        }
        mSqLiteDatabase!!.beginTransaction()
        try {
            val contvalue = ContentValues()
            contvalue.put("ifDriver", str)
            contvalue.put("role", role)
            mSqLiteDatabase!!.update(
                    GmDBHelp.TABLE_NAME, contvalue, "userId=?"
                    , arrayOf(userInfom.userId)
            )
            mSqLiteDatabase!!.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mSqLiteDatabase!!.endTransaction()
        }


    }

    //更新车辆任证状态
    fun upCar(str: String, plateNumber: String, carPlateColourId: String) {
        isRead()
        val userInfom = getUserInfom()
        if (userInfom == null || StringUtil.isEmpty(userInfom.token)) {
            return
        }
        mSqLiteDatabase!!.beginTransaction()
        try {
            val contvalue = ContentValues()
            contvalue.put("ifCar", str)
            contvalue.put("plateNumber", plateNumber)
            contvalue.put("carPlateColourId", carPlateColourId)
            mSqLiteDatabase!!.update(
                    GmDBHelp.TABLE_NAME, contvalue, "userId=?"
                    , arrayOf(userInfom.userId)
            )
            mSqLiteDatabase!!.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mSqLiteDatabase!!.endTransaction()
        }

//        val newInfom = UserDbVo()
//        newInfom.ifCar = str
//        mSqLiteDatabase!!.delete(GmDBHelp.TABLE_NAME, null, null)
//        val copyVo = copyVo(userInfom, newInfom)
//        mSqLiteDatabase!!.insert(GmDBHelp.TABLE_NAME, null, copyVo)
    }

    fun clear() {
        isRead()
        mSqLiteDatabase!!.delete(GmDBHelp.TABLE_NAME, null, null)
    }


    fun getUserInfom(): UserDbVo? {
        isRead()
        val cursor = mSqLiteDatabase!!.query(GmDBHelp.TABLE_NAME, null, null, null, null, null, null)
        mDbQueryUtil!!.initCursor(cursor)

        while (cursor.moveToNext()) {
            val vo = getContentValue(mDbQueryUtil!!)
            cursor.close()
            return vo;
        }
        cursor.close()
        return null;

    }

    fun upDateGuiderStatus(look: String) {
        isRead()
        val content = ContentValues()
        content.put("guider", look)
        val userInfom = getUserInfom()
        if (userInfom == null || StringUtil.isEmpty(userInfom.userId))
            mSqLiteDatabase!!.insert(GmDBHelp.TABLE_NAME, null, content)
        else {
            mSqLiteDatabase!!.update(GmDBHelp.TABLE_NAME, content, "userId=?",
                    arrayOf(userInfom.userId))
        }
    }

    private fun isRead() {
        if (mSqLiteDatabase == null || mSqLiteDatabase!!.isReadOnly) {
            return
        }
    }

    private fun copyVo(old: UserDbVo, new: UserDbVo): ContentValues {
        val values: ContentValues = ContentValues()
        if (StringUtil.isEmpty(new.token)) {
            values.put("token", old.token)
        } else {
            values.put("token", new.token)
        }
        if (StringUtil.isEmpty(new.userId)) {
            values.put("userId", old.userId)
        } else {
            values.put("userId", new.userId)
        }
        if (StringUtil.isEmpty(new.phone)) {
            values.put("phone", old.phone)
        } else {
            values.put("phone", new.phone)
        }
        if (StringUtil.isEmpty(new.ifReal)) {
            values.put("ifReal", old.ifReal)
        } else {
            values.put("ifReal", new.ifReal)
        }
        if (StringUtil.isEmpty(new.ifDriver)) {
            values.put("ifDriver", old.ifDriver)
        } else {
            values.put("ifDriver", new.ifDriver)
        }
        if (StringUtil.isEmpty(new.ifCar)) {
            values.put("ifCar", old.ifCar)
        } else {
            values.put("ifCar", new.token)
        }
        if (StringUtil.isEmpty(new.extend)) {
            values.put("extend", old.extend)
        } else {
            values.put("extend", new.extend)
        }
        if (StringUtil.isEmpty(new.extend_one)) {
            values.put("extend_one", old.extend_one)
        } else {
            values.put("extend_one", new.extend_one)
        }
        if (StringUtil.isEmpty(new.extend_two)) {
            values.put("extend_two", old.extend_two)
        } else {
            values.put("extend_two", new.extend_two)
        }
        if (StringUtil.isEmpty(new.extend_three)) {
            values.put("extend_three", old.extend_three)
        } else {
            values.put("extend_three", new.extend_three)
        }
        if (StringUtil.isEmpty(new.extend_four)) {
            values.put("extend_four", old.extend_four)
        } else {
            values.put("extend_four", new.extend_four)
        }
        return values;

    }

    private fun getContentValue(mDbQueryUtil: DbQueryUtil): UserDbVo {
        val vo: UserDbVo = UserDbVo()
        val id = mDbQueryUtil.queryInt("id")
        val token = mDbQueryUtil.queryString("token")
        val userId = mDbQueryUtil.queryString("userId")
        val phone = mDbQueryUtil.queryString("phone")
        val ifReal = mDbQueryUtil.queryString("ifReal")
        val ifDriver = mDbQueryUtil.queryString("ifDriver")
        val ifCar = mDbQueryUtil.queryString("ifCar")
        val name = mDbQueryUtil.queryString("name")
        val idnumber = mDbQueryUtil.queryString("idnumber")
        val hear = mDbQueryUtil.queryString("hear")
        val plateNumber = mDbQueryUtil.queryString("plateNumber")
        val role = mDbQueryUtil.queryString("role")
        val extend = mDbQueryUtil.queryString("extend")
        val extent_one = mDbQueryUtil.queryString("extend_one")
        val extend_two = mDbQueryUtil.queryString("extend_two")
        val extend_three = mDbQueryUtil.queryString("extend_three")
        val extend_four = mDbQueryUtil.queryString("extend_four")
        val guider = mDbQueryUtil.queryString("guider")
        val carPlateColourId = mDbQueryUtil.queryString("carPlateColourId")
        vo.id = id ?: 0
        vo.guider = guider ?: "0"
        vo.carPlateColourId = carPlateColourId ?: "0"
        vo.token = token ?: ""
        vo.userId = userId ?: ""
        vo.phone = phone ?: ""
        vo.ifCar = ifCar ?: ""
        vo.ifDriver = ifDriver ?: ""
        vo.ifReal = ifReal ?: ""
        vo.extend = extend ?: ""
        vo.extend_one = extent_one ?: ""
        vo.extend_two = extend_two ?: ""
        vo.extend_three = extend_three ?: ""
        vo.extend_four = extend_four ?: ""
        vo.name = name ?: ""
        vo.hear = hear ?: ""
        vo.idnumber = idnumber ?: ""
        vo.plateNumber = plateNumber ?: ""
        vo.role = role ?: ""
        return vo;
    }


}