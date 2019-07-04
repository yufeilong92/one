package com.zzzh.akhalteke.utils
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.TagAliasCallback
import cn.jpush.android.f.a.b.showToast
import cn.jpush.android.api.JPushMessage
import com.zhy.http.okhttp.utils.Utils.isConnected
import android.util.SparseArray
import java.util.*


/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.utils
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/4/26 10:33
 * @Purpose :激光推送设置别名
 */
class JgPushUtil(var mContext: Context) {
    companion object {
        //被companion object包裹的语句都是private的
        private var singletonInstance: JgPushUtil? = null
        private val TAG = "JIGUANG-TagAliasHelper"
        var sequence = 1
        /**增加 */
        val ACTION_ADD = 1
        /**覆盖 */
        val ACTION_SET = 2
        /**删除部分 */
        val ACTION_DELETE = 3
        /**删除所有 */
        val ACTION_CLEAN = 4
        /**查询 */
        val ACTION_GET = 5

        val ACTION_CHECK = 6

        val DELAY_SEND_ACTION = 1

        val DELAY_SET_MOBILE_NUMBER_ACTION = 2
        @Synchronized
        fun getInstance(m: Context): JgPushUtil? {
            if (singletonInstance == null) {
                singletonInstance = JgPushUtil(m)
            }
            return singletonInstance
        }
    }


    private val setActionCache = SparseArray<Any>()

    operator fun get(sequence: Int): Any {
        return setActionCache.get(sequence)
    }

    fun remove(sequence: Int): Any {
        return setActionCache.get(sequence)
    }

    fun put(sequence: Int, tagAliasBean: Any) {
        setActionCache.put(sequence, tagAliasBean)
    }

    private val delaySendHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                DELAY_SEND_ACTION -> if (msg.obj != null && msg.obj is TagAliasBean) {
                    Log.i(TAG, "on delay time")
                    sequence++
                    val tagAliasBean = msg.obj as TagAliasBean
                    setActionCache.put(sequence, tagAliasBean)
                    if (mContext != null) {
                        handleAction(mContext, sequence, tagAliasBean)
                    } else {
                        Log.e(TAG, "#unexcepted - context was null")
                    }
                } else {
                    Log.w(TAG, "#unexcepted - msg obj was incorrect")
                }
                DELAY_SET_MOBILE_NUMBER_ACTION -> if (msg.obj != null && msg.obj is String) {
                    Log.i(TAG, "retry set mobile number")
                    sequence++
                    val mobileNumber = msg.obj as String
                    setActionCache.put(sequence, mobileNumber)
                    if (mContext != null) {
                        handleAction(mContext, sequence, mobileNumber)
                    } else {
                        Log.e(TAG, "#unexcepted - context was null")
                    }
                } else {
                    Log.w(TAG, "#unexcepted - msg obj was incorrect")
                }
            }
        }
    }

    fun handleAction(mContext: Context?, sequence: Int, mobileNumber: String) {
        put(sequence, mobileNumber)
        Log.d(TAG, "sequence:$sequence,mobileNumber:$mobileNumber")
        JPushInterface.setMobileNumber(mContext!!, sequence, mobileNumber)
    }

    /**
     * 处理设置tag
     */
    fun handleAction(context: Context?, sequence: Int, tagAliasBean: TagAliasBean?) {
        init(context)
        if (tagAliasBean == null) {
            Log.w(TAG, "tagAliasBean was null")
            return
        }
        put(sequence, tagAliasBean)
        if (tagAliasBean.isAliasAction) {
            when (tagAliasBean.action) {
                ACTION_GET -> JPushInterface.getAlias(context!!, sequence)
                ACTION_DELETE -> JPushInterface.deleteAlias(context!!, sequence)
                ACTION_SET -> JPushInterface.setAlias(context!!, sequence, tagAliasBean.alias)
                else -> {
                    Log.w(TAG, "unsupport alias action type")
                    return
                }
            }
        } else {
            when (tagAliasBean.action) {
                ACTION_ADD -> JPushInterface.addTags(context!!, sequence, tagAliasBean.tags)
                ACTION_SET -> JPushInterface.setTags(context!!, sequence, tagAliasBean.tags)
                ACTION_DELETE -> JPushInterface.deleteTags(context!!, sequence, tagAliasBean.tags)
                ACTION_CHECK -> {
                    //一次只能check一个tag
                    JPushInterface.checkTagBindState(context!!, sequence, tagAliasBean.tags!!.toTypedArray()[0])
                }
                ACTION_GET -> JPushInterface.getAllTags(context!!, sequence)
                ACTION_CLEAN -> JPushInterface.cleanTags(context!!, sequence)
                else -> {
                    Log.w(TAG, "unsupport tag action type")
                    return
                }
            }
        }
    }

    private fun RetryActionIfNeeded(errorCode: Int, tagAliasBean: TagAliasBean?): Boolean {
        if (!ExampleUtil.isConnected(mContext)) {
            Log.w(TAG, "no network")
            return false
        }
        //返回的错误码为6002 超时,6014 服务器繁忙,都建议延迟重试
        if (errorCode == 6002 || errorCode == 6014) {
            Log.d(TAG, "need retry")
            if (tagAliasBean != null) {
                val message = Message()
                message.what = DELAY_SEND_ACTION
                message.obj = tagAliasBean
                delaySendHandler.sendMessageDelayed(message, (1000 * 60).toLong())
                val logs = getRetryStr(tagAliasBean.isAliasAction, tagAliasBean.action, errorCode)
                ExampleUtil.showToast(logs, mContext)
                return true
            }
        }
        return false
    }

    private fun RetrySetMObileNumberActionIfNeeded(errorCode: Int, mobileNumber: String): Boolean {
        if (!ExampleUtil.isConnected(mContext)) {
            Log.w(TAG, "no network")
            return false
        }
        //返回的错误码为6002 超时,6024 服务器内部错误,建议稍后重试
        if (errorCode == 6002 || errorCode == 6024) {
            Log.d(TAG, "need retry")
            val message = Message()
            message.what = DELAY_SET_MOBILE_NUMBER_ACTION
            message.obj = mobileNumber
            delaySendHandler.sendMessageDelayed(message, (1000 * 60).toLong())
            var str = "Failed to set mobile number due to %s. Try again after 60s."
            str = String.format(Locale.ENGLISH, str, if (errorCode == 6002) "timeout" else "server internal error”")
            ExampleUtil.showToast(str, mContext)
            return true
        }
        return false

    }

    private fun getRetryStr(isAliasAction: Boolean, actionType: Int, errorCode: Int): String {
        var str = "Failed to %s %s due to %s. Try again after 60s."
        str = String.format(Locale.ENGLISH, str, getActionStr(actionType), if (isAliasAction) "alias" else " tags", if (errorCode == 6002) "timeout" else "server too busy")
        return str
    }

    private fun getActionStr(actionType: Int): String {
        when (actionType) {
            ACTION_ADD -> return "add"
            ACTION_SET -> return "set"
            ACTION_DELETE -> return "delete"
            ACTION_GET -> return "get"
            ACTION_CLEAN -> return "clean"
            ACTION_CHECK -> return "check"
        }
        return "unkonw operation"
    }

    fun onTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        Log.i(TAG, "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.tags)
        Log.i(TAG, "tags size:" + jPushMessage.tags.size)
        init(context)
        //根据sequence从之前操作缓存中获取缓存记录
        val tagAliasBean = setActionCache.get(sequence) as TagAliasBean
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context)
            return
        }
        if (jPushMessage.errorCode == 0) {
            Log.i(TAG, "action - modify tag Success,sequence:$sequence")
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " tags success"
            Log.i(TAG, logs)
            ExampleUtil.showToast(logs, context)
        } else {
            var logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags"
            if (jPushMessage.errorCode == 6018) {
                //tag数量超过限制,需要先清除一部分再add
                logs += ", tags is exceed limit need to clean"
            }
            logs += ", errorCode:" + jPushMessage.errorCode
            Log.e(TAG, logs)
            if (!RetryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ExampleUtil.showToast(logs, context)
            }
        }
    }

    fun onCheckTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        Log.i(TAG, "action - onCheckTagOperatorResult, sequence:" + sequence + ",checktag:" + jPushMessage.checkTag)
        init(context)
        //根据sequence从之前操作缓存中获取缓存记录
        val tagAliasBean = setActionCache.get(sequence) as TagAliasBean
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context)
            return
        }
        if (jPushMessage.errorCode == 0) {
            Log.i(TAG, "tagBean:$tagAliasBean")
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " tag " + jPushMessage.checkTag + " bind state success,state:" + jPushMessage.tagCheckStateResult
            Log.i(TAG, logs)
            ExampleUtil.showToast(logs, context)
        } else {
            val logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags, errorCode:" + jPushMessage.errorCode
            Log.e(TAG, logs)
            if (!RetryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ExampleUtil.showToast(logs, context)
            }
        }
    }

    fun onAliasOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        Log.i(TAG, "action - onAliasOperatorResult, sequence:" + sequence + ",alias:" + jPushMessage.alias)
        init(context)
        //根据sequence从之前操作缓存中获取缓存记录
        val tagAliasBean = setActionCache.get(sequence) as TagAliasBean
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context)
            return
        }
        if (jPushMessage.errorCode == 0) {
            Log.i(TAG, "action - modify alias Success,sequence:$sequence")
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " alias success"
            Log.i(TAG, logs)
            ExampleUtil.showToast(logs, context)
        } else {
            val logs = "Failed to " + getActionStr(tagAliasBean.action) + " alias, errorCode:" + jPushMessage.errorCode
            Log.e(TAG, logs)
            if (!RetryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ExampleUtil.showToast(logs, context)
            }
        }
    }

    fun init(context: Context?) {
        if (context != null) {
            this.mContext = context.applicationContext
        }
    }
    //设置手机号码回调
    fun onMobileNumberOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        Log.i(TAG, "action - onMobileNumberOperatorResult, sequence:" + sequence + ",mobileNumber:" + jPushMessage.mobileNumber)
        init(context)
        if (jPushMessage.errorCode == 0) {
            Log.i(TAG, "action - set mobile number Success,sequence:$sequence")
            setActionCache.remove(sequence)
        } else {
            val logs = "Failed to set mobile number, errorCode:" + jPushMessage.errorCode
            Log.e(TAG, logs)
            if (!RetrySetMObileNumberActionIfNeeded(jPushMessage.errorCode, jPushMessage.mobileNumber)) {
                ExampleUtil.showToast(logs, mContext)
            }
        }
    }

    class TagAliasBean {
        internal var action: Int = 0
        internal var tags: Set<String>? = null
        internal var alias: String? = null
        internal var isAliasAction: Boolean = false

        override fun toString(): String {
            return "TagAliasBean{" +
                    "action=" + action +
                    ", tags=" + tags +
                    ", alias='" + alias + '\''.toString() +
                    ", isAliasAction=" + isAliasAction +
                    '}'.toString()
        }
    }
}