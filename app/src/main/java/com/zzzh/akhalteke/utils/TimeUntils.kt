package com.zzzh.akhalteke.utils

import android.util.Log
import com.lipo.utils.Arith
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUntils {

    /**
     * 间隔一个月
     */
    fun toNextMonth(): String {
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.MONTH, 1)
        return dateToString(nextYear.time)
    }

    /**
     * 间隔一个季度
     */
    fun toNext3Month(): String {
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.MONTH, 3)
        return dateToString(nextYear.time)
    }

    /**
     * 间隔一个年
     */
    fun toNextYear(): String {
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)
        return dateToString(nextYear.time)
    }

    /**
     * 增加n天的日期
     */
    fun toSpaceTimer(n: Int): String {
        val nextTime = Calendar.getInstance()
        nextTime.add(Calendar.DATE, n)
        return dateToString(nextTime.time)
    }

    /**
     * 增加n天的日期
     */
    fun toSpaceTimer(n: Int,endTime: String): String {
        val nextTime = Calendar.getInstance()
        nextTime.time = stringToDate(endTime)
        nextTime.add(Calendar.DATE, n)
        return dateToString(nextTime.time)
    }

    /**
     * 时间转换
     */
    fun reserveTime(tData: String): String {
        val tDInt = ToolUtils.stringToIntM(tData)
        if (tDInt > 10) {
            return "${tDInt}分钟"
        } else {
            return "${tDInt}小时"
        }
    }

    // 字符串转时间戳
    fun getTime(timeString: String?): Long {
        return stringToDate(timeString).time
    }

    fun stringToDate(timeString: String?):Date{
        var timeString = timeString
        if (timeString == null || "" == timeString) {
            return Date()
        }

        val lent = timeString.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size
        if (lent == 1) {
            timeString += " 00:00:00"
        } else if (lent == 2) {
            timeString += ":00"
        }

        var l: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var d: Date = Date()
        try {
            d = sdf.parse(timeString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return d
    }

    // 时间戳转字符串
    fun getStrTime(timeStamp: String): String? {
        var timeString: String? = null
        val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        val l = java.lang.Long.valueOf(timeStamp)
        timeString = sdf.format(Date(l))// 单位秒
        return timeString
    }

    /**
     * data 转换成 年月日
     */
    fun dateToString(date: Date): String {
        val sdf = SimpleDateFormat("yyyy年MM月dd日")
        return sdf.format(date)
    }

    /**
     * 有时间戳 计算相隔时间
     */
    fun getTimeSpace(timeString: String): Long {
        var day: Long = 0

        var space = getTime(timeString) - System.currentTimeMillis()
        if (space > 0) {
            space += (1000 * 3600 * 24).toLong()
        }

        day = Arith.div(space.toDouble(), (1000 * 3600 * 24).toDouble()).toLong()
        return day
    }

    /**
     * 前后两个时间戳，计算相隔时间
     */
    fun getSpaceTime(atEnd: String, atBegin: String): Long {
        var day: Long = 0

        val space = getTime(atEnd) - getTime(atBegin)

        day = Arith.div(space.toDouble(), (1000 * 3600 * 24).toDouble()).toLong()

        return day
    }

    /**
     * 有时间间隔，获取时间字符串
     */
    fun longToTimeStr(current: Long): String {
        var timeStr = ""
        val hour1 = current / 3600
        val hour2 = current % 3600

        val muite1 = hour2 / 60
        val muite2 = hour2 % 60

        if (hour1 == 0L) {
            timeStr = "00"
        } else if (hour1 < 10) {
            timeStr = "0$hour1"
        } else {
            timeStr = hour1.toString()
        }

        timeStr += ":"

        if (muite1 == 0L) {
            timeStr += "00"
        } else if (muite1 < 10) {
            timeStr += "0$muite1"
        } else {
            timeStr += muite1.toString()
        }

        timeStr += ":"

        if (muite2 == 0L) {
            timeStr += "00"
        } else if (muite2 < 10) {
            timeStr += "0$muite2"
        } else {
            timeStr += muite2.toString()
        }

        return timeStr
    }

    fun longSpace(endTime: String, startTime: String): Long {
        return ToolUtils.stringToLong(endTime) - ToolUtils.stringToLong(startTime)
    }

    fun toSpaceRenew(endTime: String): Int {
        var sTemp = 0
        val spaceLong = System.currentTimeMillis() - getTime(endTime)
        if (spaceLong < 0) {
            return -1
        }
        sTemp = (spaceLong / (1000 * 3600 * 24)).toInt()
//        if ((spaceLong % (1000 * 3600 * 24)) != 0L) {
//            sTemp
//        }
        return sTemp
    }

}