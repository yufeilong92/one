package com.lipo.utils

import android.content.Context
import java.lang.NumberFormatException

import java.text.DecimalFormat
import java.util.regex.Pattern

object MyPublic {

    fun getPixels(mContext:Context) {
        val dm2 = mContext.resources.displayMetrics
    }

    /**
     * 正则匹配 是否是数字
     *
     * @param str
     * @return
     */
    fun isNumber(str: String?): Boolean {
        if (str == null || "" == str) {
            return false
        }
        val pattern = Pattern.compile("[0-9]*")
        val matcher = pattern.matcher(str)
        return matcher.matches()
    }

    // 判断email格式是否正确
    fun isEmail(email: String): Boolean {
        val str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        val p = Pattern.compile(str)
        val m = p.matcher(email)

        return m.matches()
    }

    fun stringToDouble(str: String?): Double {
        var doubleValue = 0.0
        if (str == null || "" == str) {
            return 0.0
        }

        try {
            doubleValue = str.toDouble()
        } catch (e: NumberFormatException) {
            doubleValue = 0.0
        } finally {

        }

        return doubleValue
    }

    fun stringToInt(str: String?): Int {
        return if (MyPublic.isNumber(str)) {
            var intValue = 0
            try {
                intValue = str!!.toInt()
            } catch (e: NumberFormatException) {
                intValue = 0
            }

            intValue
        } else 0
    }

    fun convertTwo(value: String): String {
        val format = DecimalFormat("######0.00")
        return format.format(stringToDouble(value))
    }

    fun convertTwo(value: Double): Double {
        val format = DecimalFormat("######0.00")
        return stringToDouble(format.format(value))
    }

    fun mapClearEmpty(params: Map<String, String>) {
        //		Set<String> set = params.keySet();
        //		Iterator<String> its = set.iterator();
        //		while (its.hasNext()) {
        //			String key = its.next();
        //			String value = params.get(key);
        //			if (!value.isEmpty()) {
        //				params.remove(key);
        //			}
        //		}
    }

}
