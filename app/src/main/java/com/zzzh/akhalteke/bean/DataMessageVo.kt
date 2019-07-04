package com.zzzh.akhalteke.bean

import com.zzzh.akhalteke.bean.vo.WeatherVo
import com.zzzh.akhalteke.utils.Utils

class DataMessageVo {
    companion object {
        var PHONETYPE: String = "1";
        var PASSWORD: ByteArray = "akmd".toByteArray();
        val BRAND: String = Utils.getBrand();
        val MODEL: String = Utils.getModel();
//        val HTTP_HEAR: String = "http://zzzh56.com:8080/"
        val HTTP_HEAR: String = "http://zzzh56.com:8082/"//外网
//        val HTTP_HEAR: String = "http://192.168.100.12:8080/"//本地
//        val HTTP_HEAR: String = "http://http://2y50t76103.zicp.vip:41782/"
//        val HTTP_HEAR: String = "http://39.105.197.48:8080/"
        //身份正面(行驶证)
        val CarIdZ: Int = 0
        //反面（货车行驶证,营业执照）
        val CarIdF: Int = 1
        //头像
        val Hear: Int = 2

        val BULE: Int = 1;
        val YELLOW: Int = 2;
        val GREEN: Int = 3;
        val HUN: Int = 4;
        val CINT_PANGE_SIZE = 10;
        val ORDER_STATUS_One = "1";//运输中
        val ORDER_STATUS_tWO = "2";//已完成
        val ORDER_STATUS_Three = "3";//已取消，

        val SURE_ARGREEMENT = "1";//确定，
        val CANCLE_ARGREEMENT = "2";//取消，
        val REFRESH_STARTPAGE = 0;//刷新其实页，
        val KEFU_PHONE: String = "4001-567-168";//客服电话

        val ZONE = 0
        val ONE = 1
        val TWO = 2
        val mUpdateUrl: String = DataMessageVo.HTTP_HEAR + "androidDriverVersion.json"
        //        val JGRQUEST = Utils.getRandom4()
        val JGRQUEST = 1001
        var CityCode: String = "101010100"
        var mWeatherData: WeatherVo? = null
            val ETC_HTTP=" https://loan.cywetc.com/ls-h5-common/index.html"
    }

}