package com.zzzh.akhalteke.service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract
import com.zzzh.akhalteke.mvp.model.CheckInfomModel
import com.zzzh.akhalteke.mvp.presenter.CheckInfomPresenter

private const val ACTION_FOO = "com.zzzh.akhalteke.service.action.FOO"

private const val EXTRA_PARAM1 = "com.zzzh.akhalteke.service.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.zzzh.akhalteke.service.extra.PARAM2"

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.service
 * @Package com.zzzh.akhalteke.service
 * @Email : yufeilong92@163.com
 * @Time :2019/4/9 10:20
 * @Purpose :校验用户信息
 */
class CheckInfomService : IntentService("CheckInfomService"), CheckInfomContract.View {
    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FOO -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo(param1, param2)
            }
        }
    }


    private fun handleActionFoo(param1: String, param2: String) {

    }

    companion object {

        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, CheckInfomService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }
    }

    override fun CheckSuccess(t: Any?) {



    }

    override fun CheckError(ex: Throwable) {
    }

    override fun CheckComplise() {
    }

}
