package com.zzzh.akhalteke.ui.video

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/5/9 9:26
 * @Purpose :视频工具类
 */
object VideoUtil {
    /**
     * 裁剪视频本地目录路径
     */
    fun getTrimmedVideoDir(context: Context, dirName: String): String {
        var dirPath = ""
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            dirPath = (context.externalCacheDir.toString() + File.separator
                    + dirName) // /mnt/sdcard/Android/data/<package name>/files/...
        } else {
            dirPath = (context.cacheDir.toString() + File.separator
                    + dirName) // /data/data/<package name>/files/...
        }
        val file = File(dirPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return dirPath
    }
}