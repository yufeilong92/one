package com.zzzh.akhalteke.utils

import com.zzzh.akhalteke.bean.vo.GetParamVo

class SortUtils{
    fun getSortString(datas:Array<GetParamVo>):String{
        var mStringbuffer:StringBuffer
        for (datas in datas.iterator()){}
        return  ""
    }

    /**
     * 升序排序
     *
     * @param str
     * @return
     */
    fun sort(str: String): String {
        val s1 = str.toCharArray()
        println(s1)
        for (i in s1.indices) {
            for (j in 0 until i) {
                if (s1[i] < s1[j]) {
                    val temp = s1[i]
                    s1[i] = s1[j]
                    s1[j] = temp
                }
            }
        }
        return String(s1)
    }
}