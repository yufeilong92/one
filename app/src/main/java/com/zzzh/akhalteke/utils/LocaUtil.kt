package com.zzzh.akhalteke.utils

import android.content.Context
import java.io.File



/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 15:54
 * @Purpose :缓存工具
 */
object LocaUtil {

    /**
     * 计算缓存的大小,
     *
     * @return
     */
    fun getCacheSize(context: Context): String {
        var fileSize: Long = 0
        var cacheSize = "0KB"
        val filesDir = context.getApplicationContext().getFilesDir()// /data/data/package_name/files
        val cacheDir = context.getCacheDir()// /data/data/package_name/cache
        fileSize += getDirSize(filesDir)
        fileSize += getDirSize(cacheDir)
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            val externalCacheDir = getExternalCacheDir(context)// "<sdcard>/Android/data/<package_name>/cache/"
            fileSize += getDirSize(externalCacheDir)
        }
        if (fileSize > 0)
            cacheSize = formatFileSize(fileSize)
        return cacheSize
    }

    /**
     * 清除app缓存
     */
    fun clearAppCache(context: Context) {
        // 清除数据缓存
        clearCacheFolder(context.getFilesDir(), System.currentTimeMillis())
        clearCacheFolder(context.getCacheDir(), System.currentTimeMillis())
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            clearCacheFolder(
                getExternalCacheDir(context),
                System.currentTimeMillis()
            )
        }
    }

    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    private fun clearCacheFolder(dir: File?, curTime: Long): Int {
        var deletedFiles = 0
        if (dir != null && dir!!.isDirectory()) {
            try {
                for (child in dir!!.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime)
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return deletedFiles
    }


    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    fun getDirSize(dir: File?): Long {
        if (dir == null) {
            return 0
        }
        if (!dir!!.isDirectory()) {
            return 0
        }
        var dirSize: Long = 0
        val files = dir!!.listFiles()
        for (file in files) {
            if (file.isFile()) {
                dirSize += file.length()
            } else if (file.isDirectory()) {
                dirSize += file.length()
                dirSize += getDirSize(file) // 递归调用继续统计
            }
        }
        return dirSize
    }

    /**
     * 将二进制长度转换成文件大小
     *
     * @param length
     * @return
     */
    fun formatFileSize(length: Long): String {
        var result: String = "0"
        var sub_string = 0
        if (length >= 1073741824) {
            sub_string = (length.toFloat() / 1073741824).toString().indexOf(
                "."
            )
            result = ((length.toFloat() / 1073741824).toString() + "000").substring(
                0,
                sub_string + 3
            ) + "GB"
        } else if (length >= 1048576) {
            sub_string = (length.toFloat() / 1048576).toString().indexOf(".")
            result = ((length.toFloat() / 1048576).toString() + "000").substring(
                0,
                sub_string + 3
            ) + "MB"
        } else if (length >= 1024) {
            sub_string = (length.toFloat() / 1024).toString().indexOf(".")
            result = ((length.toFloat() / 1024).toString() + "000").substring(
                0,
                sub_string + 3
            ) + "KB"
        } else if (length < 1024)
            result = java.lang.Long.toString(length) + "B"
        return result
    }


    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    fun isMethodsCompat(VersionCode: Int): Boolean {
        val currentVersion = android.os.Build.VERSION.SDK_INT
        return currentVersion >= VersionCode
    }

    fun getExternalCacheDir(context: Context): File {
        // return context.getExternalCacheDir(); API level 8
        // e.g. "<sdcard>/Android/data/<package_name>/cache/"
        return context.getExternalCacheDir()
    }
}