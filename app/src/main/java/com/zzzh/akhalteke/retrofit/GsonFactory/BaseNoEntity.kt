package com.zzzh.akhalteke.retrofit.gsonFactory

import com.google.gson.annotations.SerializedName

class BaseNoEntity{

    @SerializedName("status")
    var status:Int = 0

    @SerializedName("msg")
    var msg:String = ""

}