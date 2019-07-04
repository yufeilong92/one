package com.zzzh.akhalteke.utils

import android.content.Context
import java.io.*
import java.lang.Exception

object FileUtil {

    /**
     * @param path 打开路径文件
     */
    fun fileUtil(context: Context, path: String): String {
        val open = context.resources.assets.open(path)
        val buffer = BufferedReader(InputStreamReader(open, "GBK"))
        var sb = StringBuffer()
        var str: String? = null
        while ((buffer.readLine().also { str = it }) != null) {
            sb.append(str)
        }
        buffer.close()
        return sb.toString()
    }

    fun saveText(context: Context, mStr: String) {
        val name: String = "${System.currentTimeMillis()}.txt"
        val filePath = context.externalCacheDir.absolutePath + File.separator +name
        try {
            val file = File(filePath)
            if (!file.exists()) {
                file.createNewFile()
            }
            val outStream=FileOutputStream(file)
            L.e(file.name)
            outStream.write(mStr.toByteArray())
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}