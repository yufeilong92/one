package com.zzzh.akhalteke.jgpush

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.JPushInterface
import android.os.Bundle
import com.zzzh.akhalteke.utils.StringUtil
import org.json.JSONException
import org.json.JSONObject
import android.text.TextUtils
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.me.MyNewMoneyActivity
import com.zzzh.akhalteke.ui.waybill.FreightNewAgreementActivity
import com.zzzh.akhalteke.ui.waybill.OrderInfomNoActivity
import com.zzzh.akhalteke.ui.waybill.OrderNewInfomActivity
import com.zzzh.akhalteke.utils.AudioUtils
import com.zzzh.akhalteke.xunfei.MyIntentService


class GmJgPushService : BroadcastReceiver() {
    private val TAG = "JIGUANG-Example"
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val bundle = intent!!.getExtras()
//            Log.d(TAG, "[MyReceiver] onReceive - " + intent!!.getAction() + ", extras: " + printBundle(context, bundle))
            printBundles(context, bundle)
            if (JPushInterface.ACTION_REGISTRATION_ID == intent!!.getAction()) {
                val regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId!!)
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.getAction()) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE)!!)
                processCustomMessage(context!!, bundle)

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.getAction()) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知")
                val notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: $notifactionId")

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == intent!!.getAction()) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知")

                //打开自定义的Activity
//                val i = Intent(context, JgTestActivity::class.java)
//                i.putExtras(bundle)
//                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                context!!.startActivity(i)
                printBundle(context, bundle)
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK == intent.getAction()) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA)!!)
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE == intent.getAction()) {
                val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected)
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction())
            }
        } catch (e: Exception) {

        }

    }

    // 打印所有的 intent extra 数据
    private fun printBundle(context: Context?, bundle: Bundle): String {
        val sb = StringBuilder()
        for (key in bundle.keySet()) {
            if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
            } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
            } else if (key == JPushInterface.EXTRA_EXTRA) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data")
                    continue
                }

                try {
                    val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                    val it = json.keys()

                    //todo 处理激光推送键值对
                    while (it.hasNext()) {
                        val myKey = it.next()
                        val value = json.optString(myKey)
//                        sb.append(
//                            "\nkey:" + key + ", value: [" +
//                                    myKey + " - " + json.optString(myKey) + "]"
//                        )
//                        L.e(
//                            "key===" + myKey + "\n" +
//                                    "value===" + json.optString(myKey)
//                        )
                        when (myKey) {
                            "orderInfo" -> {//订单详情
                                val start = orderInfomActivity()
                                startManger(context, start, value)
                            }
                            "agreementInfo" -> {//协议详情
                                val start = agreementInfomActivity()
                                startManger(context, start, value)
                            }
                            "accountInfo" -> {//我的钱包
                                val start = accountInfomActivity()
                                startManger(context, start, value)
                            }
//                            "alarm" -> {//语音
//                                val start = alarmActivity()
//                                startManger(context, start, value)
//                            }
                        }


                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Get message extra JSON error!")
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key))
            }
        }
        return sb.toString()
    }

    // 打印所有的 intent extra 数据
    private fun printBundles(context: Context?, bundle: Bundle): String {
        val sb = StringBuilder()
        for (key in bundle.keySet()) {
            if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
            } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
            } else if (key == JPushInterface.EXTRA_EXTRA) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data")
                    continue
                }
                try {
                    val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                    val it = json.keys()
                    //todo 处理激光推送键值对
                    while (it.hasNext()) {
                        val myKey = it.next()
                        val value = json.optString(myKey)
//                        sb.append(
//                            "\nkey:" + key + ", value: [" +
//                                    myKey + " - " + json.optString(myKey) + "]"
//                        )
//                        L.e(
//                            "key===" + myKey + "\n" +
//                                    "value===" + json.optString(myKey)
//                        )
                        when (myKey) {
//                            "orderInfo" -> {//订单详情
//                                val start = orderInfomActivity()
//                                startManger(context,start, value)
//                            }
//                            "agreementInfo" -> {//协议详情
//                                val start = agreementInfomActivity()
//                                startManger(context,start, value)
//                            }
//                            "accountInfo" -> {//我的钱包
//                                val start = accountInfomActivity()
//                                startManger(context,start, value)
//                            }
                            "alarm" -> {//语音
                                val start = alarmActivity()
                                startManger(context, start, value)
                            }
                        }


                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Get message extra JSON error!")
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key))
            }
        }
        return sb.toString()
    }

    //send msg to MainActivity
    private fun processCustomMessage(context: Context, bundle: Bundle) {
        val message = bundle.getString(JPushInterface.EXTRA_MESSAGE)
        val extras = bundle.getString(JPushInterface.EXTRA_EXTRA)
//            val msgIntent = Intent(MainActivity.MESSAGE_RECEIVED_ACTION)
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message)
        if (!StringUtil.isEmpty(extras)) {
            try {
                val extraJson = JSONObject(extras)
                if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras)
                }
            } catch (e: JSONException) {
            }
        }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent)
    }

    interface StartIntent {
        fun startIntent(context: Context?, id: String);
    }

    private fun startManger(context: Context?, start: StartIntent, id: String) {
        start.startIntent(context, id)
    }

    //订单详情
    private inner class orderInfomActivity : StartIntent {
        override fun startIntent(context: Context?, id: String) {
            val intent = Intent(context!!, OrderNewInfomActivity::class.java)
            val bundle = Bundle()
            bundle.putString(BaseActivity.CNT_PARAMETE_TITLE, "订单详情")
            bundle.putString(OrderNewInfomActivity.ORDER_ID, id)
            bundle.putString(OrderNewInfomActivity.TYPE, OrderInfomNoActivity.TYPE_ONE)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtras(bundle)
            context!!.startActivity(intent)

        }
    }

    //协议详情
    private inner class agreementInfomActivity : StartIntent {
        override fun startIntent(context: Context?, id: String) {
            val intent = Intent(context!!, OrderNewInfomActivity::class.java)
            val bundle = Bundle()
            bundle.putString(BaseActivity.CNT_PARAMETE_TITLE, "订单详情")
            bundle.putString(OrderNewInfomActivity.ORDER_ID, id)
            bundle.putString(OrderNewInfomActivity.TYPE, OrderInfomNoActivity.TYPE_ONE)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context!!.startActivity(intent)
        }
    }

    //我的钱包
    private inner class accountInfomActivity : StartIntent {
        override fun startIntent(context: Context?, id: String) {
            val intent = Intent(context!!, MyNewMoneyActivity::class.java)
            intent.putExtra(BaseActivity.CNT_PARAMETE_TITLE, "我的钱包")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context!!.startActivity(intent)
        }
    }

    //提示
    private inner class alarmActivity : StartIntent {
        override fun startIntent(context: Context?, id: String) {
//            MyIntentService.startActionFoo(context!!, id, "")
        AudioUtils.getInstance(context!!)!!.startVideo(id)
        }
    }


}
