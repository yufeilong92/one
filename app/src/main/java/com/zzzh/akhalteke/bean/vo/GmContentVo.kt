package com.zzzh.akhalteke.bean.vo

import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo

object GmContentVo {
    var mUserInfom: UserDbVo? = null;
    fun setUserInfom(vo: UserDbVo) {
        mUserInfom = vo;
    }

    fun getUserInfom(): UserDbVo? {
        if (mUserInfom == null) {
            return null
        }
        return mUserInfom
    }

    var mWeatherVo: WeatherVo? = null

    fun setWeatherVo(vo: WeatherVo) {
        this.mWeatherVo = vo
    }

    fun getWeatherVo(): WeatherVo? {
        if (mWeatherVo == null)
            return null
        return mWeatherVo
    }
}