package com.zzzh.akhalteke.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.zzzh.akhalteke.R

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/4/1 16:46
 * @Purpose :车牌选择器
 */
class CarColorSelextUtil(var mContext: Context) {
    companion object {
        //被companion object包裹的语句都是private的
        private var singletonInstance: CarColorSelextUtil? = null

        @Synchronized
        fun getInstance(m: Context): CarColorSelextUtil? {
            if (singletonInstance == null) {
                singletonInstance = CarColorSelextUtil(m)
            }
            return singletonInstance
        }
    }

    enum class CarColor {
        BLUE, YELLOW, Green, Hun
    }

    var mCarColor: Drawable? = mContext.resources.getDrawable(R.mipmap.lansechepai);
    fun setSelectCar(statas: CarColor) {
        when (statas) {
            CarColor.BLUE -> {
                mCarColor = getDrawable(mContext, R.mipmap.lansechepai)
            }
            CarColor.YELLOW -> {
                mCarColor = getDrawable(mContext, R.mipmap.huangsechepai)
            }
            CarColor.Green -> {
                mCarColor = getDrawable(mContext, R.mipmap.lvsechepai)
            }
            CarColor.Hun -> {
                mCarColor = getDrawable(mContext, R.mipmap.huanglvsechepai)
            }
        }
    }

    fun getDrawable(m: Context, id: Int): Drawable? {
        return m.resources.getDrawable(id)
    }


}