package com.zzzh.akhalteke.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.R


class RouterTo(var mContext: Context) {

    @SuppressLint("MissingPermission")
    fun callPhone(phoneNum: String) {
        PermissionUtils.showPermission(mContext as Activity, "打电话需要开通打电话权限，请允许", arrayOf(Permission.CALL_PHONE)) {
            //        Intent intent = new Intent(Intent.ACTION_DIAL);
            val intent = Intent(Intent.ACTION_CALL)
            val data = Uri.parse("tel:$phoneNum")
            intent.data = data
            mContext.startActivity(intent)
            (mContext as Activity).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
        }
    }

    fun jumpTo(clazz: Class<*>) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        mContext.startActivity(intentB)
        (mContext as Activity).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpTo(clazz: Class<*>, bundle: Bundle) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        mContext.startActivity(intentB)
        (mContext as Activity).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpToFoResult(clazz: Class<*>, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        (mContext as Activity).startActivityForResult(intentB, resultCode)
        (mContext as Activity).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpToFoResult(clazz: Class<*>, bundle: Bundle, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        (mContext as Activity).startActivityForResult(intentB, resultCode)
        (mContext as Activity).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpToBU(clazz: Class<*>) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        mContext.startActivity(intentB)
        (mContext as Activity).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToBU(clazz: Class<*>, bundle: Bundle) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        mContext.startActivity(intentB)
        (mContext as Activity).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResulBU(clazz: Class<*>, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        (mContext as Activity).startActivityForResult(intentB, resultCode)
        (mContext as Activity).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResultBU(clazz: Class<*>, bundle: Bundle, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        (mContext as Activity).startActivityForResult(intentB, resultCode)
        (mContext as Activity).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun finishBase() {
        (mContext as Activity).finish()
        (mContext as Activity).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    }

}