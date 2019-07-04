package com.lipo.utils

import android.text.format.Time

class MyTime {

    var year: Int = 0

    var month: Int = 0

    var day: Int = 0

    var minute: Int = 0

    var hour: Int = 0

    var second: Int = 0

    var WeekOfYear: Int = 0

    companion object {

        val timeNow: MyTime
            get() {
                val time = MyTime()
                val t = Time()
                t.setToNow()
                return time
            }
    }

}
