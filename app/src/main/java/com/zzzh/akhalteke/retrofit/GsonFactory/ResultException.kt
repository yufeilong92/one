package com.zzzh.akhalteke.retrofit.gsonFactory

import java.io.IOException

class ResultException(
        var msg: String = "",
        var status: String = "",
        var data: String = "") : IOException() {

}