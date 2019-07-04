package com.zzzh.akhalteke.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import com.zzzh.akhalteke.bean.DataMessageVo


/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/3/16 10:52
 * @Purpose :时间工具类
 */
class TimeUtil {
    val CINT_TIME_SECOND = 1000
    val CINT_TIME_MINUTE = 60 * 1000
    val CINT_TIME_HOUR = 3600 * 1000
    val CINT_TIME_DAY = 24 * 3600 * 1000

    companion object {//被companion object包裹的语句都是private的

        private var singletonInstance: TimeUtil? = null

        @Synchronized
        fun getInstance(): TimeUtil? {
            if (singletonInstance == null) {
                singletonInstance = TimeUtil()
            }
            return singletonInstance
        }
    }

    /**
     * 返回yyyy-MM-DD hh:mm:ss
     *
     * @param datestr 处理：2015-12-22 08:49:21.0
     * @return
     */
    fun getCommonDateStr(datestr: String): String {
        if (StringUtil.isEmpty(datestr) || datestr.length <= 19)
            return datestr
        val tmpStr = datestr.substring(0, 19)
        val date = strToDate(tmpStr, null) ?: return tmpStr
        return getChatTime(date.time)
    }

    fun getTime(): Long {
        return Date().time
    }

    /**
     * 字符串转日期
     *
     * @param str 字符串
     * @param def 默认时间，如果转换失败则返回默认时间
     */
    fun strToDate(str: String, def: Date?): Date? {
        return strToDate(str, "yyyy-MM-dd HH:mm:ss", def)
    }

    /**
     * 字符串转日期
     *
     * @param str 字符串
     * @param def 默认时间，如果转换失败则返回默认时间
     */
    fun strToDate(str: String, formatstr: String, def: Date?): Date? {
        if (StringUtil.isEmpty(str))
            return def
        try {
            val sdf = SimpleDateFormat(formatstr)
            return sdf.parse(str)
        } catch (e: Exception) {
            return def
        }

    }

    /**
     * 计算当前时间-提供的时间间隔
     *
     * @param str
     * @return
     */
    fun intervalNow(str: String): Long {
        return intervalNow(strToDate(str, null))
    }

    /**
     * 是否超过一天
     *
     * @param str
     * @return
     */
    fun isMore(str: String): Boolean {
        val l1 = intervalNow(strToDate(str, null))
        val l = l1 - 86400000
        return l > 0
    }

    /**
     * 是否超过一天
     *
     * @param str
     * @return
     */
    fun isMoreHour(str: String): Boolean {
        val l1 = intervalNow(strToDate(str, null))
        val l = l1 - 3600000
        return l > 0
    }

    /**
     * 计算当前时间-提供的时间间隔
     *
     * @param date
     * @return
     */
    fun intervalNow(date: Date?): Long {
        return if (date == null) Date().time else Date().time - date.time
    }

    /**
     * 返回两个时间的间隔(取绝对值)，单位ms
     *
     * @param date1
     * @param date2
     * @return
     */
    fun interval(date1: Date?, date2: Date?): Long {
        if (date1 == null && date2 == null)
            return 0
        if (date1 == null)
            return date2!!.time
        return if (date2 == null) date1.time else Math.abs(date1.time - date2.time)
    }

    /**
     * 日期转为字符串
     *
     * @param date 如果为空，返回当前时间
     * @return
     */
    fun dateToString(date: Date?): String {
        var date = date
        if (date == null)
            date = Date()
        return dateToString(date, "yyyy-MM-dd HH:mm:ss")
    }

    /**
     * 日期转为字符串
     *
     * @param date 如果为空，返回当前时间
     * @return
     */
    fun dateToStringOne(date: Date?): String {
        var date = date
        if (date == null)
            date = Date()
        return dateToString(date, "yyyy/MM/dd HH:mm:ss")
    }

    /**
     * 日期转为字符串
     *
     * @param date 如果为空，返回当前时间
     * @return
     */
    fun dateToStringYMD(date: Date?): String {
        var date = date
        if (date == null)
            date = Date()
        return dateToString(date, "yyyy-MM-dd")
    }

    /**
     * 日期转为字符串
     *
     * @param date         如果为空，返回当前时间
     * @param formatstring 如果为空，则默认格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun dateToString(date: Date?, formatstring: String?): String {
        var date = date
        var formatstring = formatstring
        if (formatstring == null || formatstring == "")
            formatstring = "yyyy-MM-dd HH:mm:ss"
        if (date == null)
            date = Date()
        try {
            val sdf = SimpleDateFormat(formatstring)
            return sdf.format(date)
        } catch (e: Exception) {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return sdf.format(date)
        }

    }

    fun longToStr(date: Long, formatstr: String): String {
        return dateToString(Date(date), formatstr)
    }


    fun getTime(time: Long): String {
        val format = SimpleDateFormat("MM月dd日 HH:mm")
        return format.format(Date(time))
    }
    fun getYMDTime(time: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(Date(time))
    }
    fun getYMDTimeWithOutms(time: Long): String {
        val format = SimpleDateFormat("yyyy年MM月dd日 HH点")
        return format.format(Date(time))
    }
    fun getYMDWithOuthmsTime(time: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(Date(time))
    }
    fun getMDhmTime(time: Long): String {
        val format = SimpleDateFormat("MM-dd HH:mm")
        return format.format(Date(time))
    }
    fun getHourAndMin(time: Long): String {
        val format = SimpleDateFormat("HH:mm")
        return format.format(Date(time))
    }

    fun getChatTime(timesamp: Long): String {
        var result = ""
        val sdf = SimpleDateFormat("dd")
        val today = Date(System.currentTimeMillis())
        val otherDay = Date(timesamp)
        val temp = Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay))

        when (temp) {
            0 -> result = getHourAndMin(timesamp)
            1 -> result = "昨天 " + getHourAndMin(timesamp)
            2 -> result = "前天 " + getHourAndMin(timesamp)

            else ->
                // result = temp + "天前 ";
                result = getTime(timesamp)
        }

        return result
    }

    //截取年月日
    fun getYMDT(str: String): String {
        var time = ""
        if (!StringUtil.isEmpty(str)) {
            val x = str.indexOf(" ")
            if (x == -1)
                return str
            time = str.substring(0, x)
        }
        return time
    }

    //截取年
    fun getYearTime(str: String): Int {
        var time = 0
        if (!StringUtil.isEmpty(str)) {
            val x = str.indexOf("-")
            try {
                time = Integer.parseInt(str.substring(0, x))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return time
    }

    fun getCurrentYear(): String {
        val sdf = SimpleDateFormat("yyyy")
        val date = Date()
        return sdf.format(date)
    }

    /**
     * 字符传转换成long
     * @param time
     * @return
     */
    fun getTimeWString(time: String): Long {
        if (StringUtil.isEmpty(time)) {
            return 0
        }
        val date = strToDate(time, null) ?: return 0
        return date.time
    }

    fun getHaoMiaoToTime(time: Long): String {
        val dateFormat =  SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
// time为转换格式后的字符串
        return dateFormat.format( Date (time));
    }
    /**
     * 是否晚上十点到早上六点之间
     * @param planDate
     * @return
     */
    fun  isNight(planDate:Date):Boolean {
        //如果计划发布时间为空，就当发布时间为白天
        if(null == planDate) {
            return false;
        }
        //设置当前时间
        val date = Calendar.getInstance();
        date.time = planDate;

        //处于开始时间之后，和结束时间之前的判断
        return date.get(Calendar.HOUR_OF_DAY) >= 18 || date.get(Calendar.HOUR_OF_DAY) < 6
    }

}