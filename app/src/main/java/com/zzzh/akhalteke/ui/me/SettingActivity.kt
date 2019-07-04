package com.zzzh.akhalteke.ui.me

import android.os.Bundle
import com.google.gson.Gson
import com.vector.appupdatedemo.ext.cancelProgressDialog
import com.vector.appupdatedemo.ext.showProgressDialog
import com.vector.appupdatedemo.ext.toast
import com.vector.update_app.UpdateAppBean
import com.vector.update_app_kotlin.check
import com.vector.update_app_kotlin.updateApp
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.UpDataVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.net.updateapp.UpdateAppHttpUtil
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.video.OpenVideoActivity
import com.zzzh.akhalteke.utils.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.json.JSONObject
import java.io.File

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/3/16 10:24
 * @Purpose :设置界面
 */
class SettingActivity : BaseActivity() {
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_setting)
        initEvent()
        bindCacheView()
    }

    fun bindCacheView() {
        tv_setting_version.text = Utils.getVersionName(mContext)
    }

    fun initEvent() {
        ll_setting_clear_cache.setOnClickListener {
            //清除缓存
            LocaUtil.clearAppCache(mContext)
            showToast(getString(R.string.cleat_cache))
            bindCacheView()
        }
        ll_setting_version.setOnClickListener {
            //版本信息
            updateDiy1()
//            showTeathcer()
        }
        ll_settting_feedbook.setOnClickListener {
            //意见反馈
            jumpTo(FeedbackActivity::class.java, getString(R.string.feedback))
        }
        btn_setting_out.setOnClickListener {
            DialogUtil.showDialogTitle(mContext, getString(R.string.sure_out_user), "", getString(R.string.sure), getString(R.string.cancle),
                    true, object : DialogUtil.TitleInterface {
                override fun sureOnClick() {
                    //推出账号
                    val dbHelp = UserDbHelp.get_Instance(mContext)
                    finishBase()
                    val jgPush = JgPushUtil.getInstance(mContext)
                    val infom = dbHelp!!.getUserInfom()
                    val bean = JgPushUtil.TagAliasBean()
                    bean.isAliasAction = true
                    bean.action = JgPushUtil.ACTION_DELETE
                    bean.alias = infom!!.userId
                    L.e("random4==setting=", "" + DataMessageVo.JGRQUEST)
                    jgPush!!.handleAction(mContext, DataMessageVo.JGRQUEST, bean)
                    dbHelp!!.clear()
                    BaseApplication.toInstance().startActivty(mContext)
                }

                override fun cancleOnClick() {
                }
            })

        }
    }

    fun showTeathcer() {
        jumpTo(OpenVideoActivity::class.java, "天气预报")
    }

    fun updateDiy1() {
        //下载路径
        val path = mContext.externalCacheDir.absolutePath + File.separator
        //自定义参数
//        val params = HashMap<String, String>()
//        params.put("appVersion", AppUpdateUtils.getVersionName(this))
        updateApp(DataMessageVo.mUpdateUrl, UpdateAppHttpUtil())
        //自定义配置
        {
            //以下设置，都是可选
            //设置请求方式，默认get
            isPost = false
            //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
//            setParams(params)
            //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
            hideDialogOnDownloading()
            //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
            topPic = R.mipmap.top_8
            //为按钮，进度条设置颜色。
            themeColor = 0xffffac5d.toInt()
//            themeColor = 0xf1e4655d.toInt()
            //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
            targetPath = path

            //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
//                setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")

        }
                .check {
                    onBefore { showProgressDialog(this@SettingActivity) }
                    //自定义解析
                    parseJson {
                        val data = Gson().fromJson<UpDataVo>(it, UpDataVo::class.java)
                        var isUpdate = false
                        if (data.type == 1) {
                            isUpdate = true
                        }
                        val code = Utils.getAppVersionCode(mContext)
                        var isUpdataApp = "No"
                        if (code < data.versionCode!!) {
                            isUpdataApp = "Yes"
                        }
                        val jsonObject = JSONObject(it)
                        UpdateAppBean()
                                //（必须）是否更新Yes,No
                                .setUpdate(isUpdataApp)
                                //（必须）新版本号，
                                .setNewVersion(data.version)
                                //（必须）下载地址
                                .setApkFileUrl(data.path)
                                //（必须）更新内容
                                .setUpdateLog(data.updateLog)
                                //大小，不设置不显示大小，可以不设置
                                .setTargetSize(data.appSize)
                                //是否强制更新，可以不设置
                                .setConstraint(isUpdate)
                        //设置md5，可以不设置
//                                .setNewMd5(jsonObject.optString("new_md5"))

                    }
                    noNewApp { toast("当前已经是最新版本") }
                    onAfter { cancelProgressDialog(this@SettingActivity) }
                }
    }
}
