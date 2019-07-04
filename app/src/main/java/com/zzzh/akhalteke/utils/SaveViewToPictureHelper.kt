package com.zzzh.akhalteke.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import com.lipo.views.ToastView
import com.yanzhenjie.permission.Permission
import java.io.*

/**
 * 把view保存成图片
 */
object SaveViewToPictureHelper{

    /**
     * 把view保存成图片
     */
     fun save(mContext: Context, saveView: View) {
        PermissionUtils.showPermission(mContext as Activity, "保存海报需要读写内存卡的权限，请开启", arrayOf(Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE)) {
            saveView.isDrawingCacheEnabled = true
            saveView.buildDrawingCache()
            Handler().postDelayed(Runnable {
                val bmp = saveView.drawingCache // 获取图片
                savePicture(mContext,bmp, "pic" + System.currentTimeMillis() / 1000 + ".jpg")// 保存图片
                saveView.destroyDrawingCache() // 保存过后释放资源
            }, 300)
        }
    }

    /**
     * 把Bitmap保存成本地图片
     */
    fun savePicture(mContext: Context,bm: Bitmap?, fileName: String) {

        if (bm == null) {
            return
        }
        val foder = File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera")

        if (!foder.exists()) {
            foder.mkdirs()
        }
        val myCaptureFile = File(foder, fileName)
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile()
            }
            val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //        showToast("海报已成功保存在" + foder.getPath());
        ToastView.setToasd(mContext,"海报保存成功")

        try {
            MediaStore.Images.Media.insertImage(mContext.contentResolver, myCaptureFile.absolutePath, myCaptureFile.name, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()

        }
        // 发送广播，通知刷新图库的显示
        mContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myCaptureFile)))
    }

}