package com.zzzh.akhalteke.ui.waybill

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.BitmapCallback
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.UpdataReciptAdapter
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.SelectPath
import com.zzzh.akhalteke.mvp.contract.UpLoadReceiptContract
import com.zzzh.akhalteke.mvp.model.UpLoadReceiptModel
import com.zzzh.akhalteke.mvp.presenter.UpLoadReceiptPresenter
import com.zzzh.akhalteke.ui.ImageActivity
import com.zzzh.akhalteke.ui.SelectorImageActivity
import com.zzzh.akhalteke.utils.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_updata_recipt.*
import okhttp3.*
import top.zibin.luban.Luban
import java.io.File
import java.lang.Exception

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.waybill
 * @Package com.zzzh.akhalteke.ui.waybill
 * @Email : yufeilong92@163.com
 * @Time :2019/3/18 15:23
 * @Purpose :上传回单
 */
class UpdataReciptActvity : SelectorImageActivity(), UpLoadReceiptContract.View {


    private var mSelectList: MutableList<SelectPath>? = null

    companion object {
        var showImgar: String = "1000"
        var ORDER_ID: String = "order_id"
        var TYPE: String = "type"
        var NO_TYPE: String = "no_type"
        var LOOK_TYPE: String = "looktype"
    }

    private var mOrder_id: String? = null
    private var mPictureAdapter: UpdataReciptAdapter? = null
    var isDelete: Boolean = false//是否选择选择三个了
    private var mPresenter: UpLoadReceiptPresenter? = null
    private var mType: String = NO_TYPE
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_updata_recipt)
        if (intent != null) {
            mOrder_id = intent.getStringExtra(ORDER_ID)
            mType = intent.getStringExtra(TYPE)
        }
        initMutabtList()
        initRequest()
        initEvent()
        if (mType != NO_TYPE) {
            btn_recipt_upload.visibility = View.GONE
        } else {
            btn_recipt_upload.visibility = View.VISIBLE
        }
    }

    fun initRequest() {
        mPresenter = UpLoadReceiptPresenter()
        mPresenter!!.initMvp(this, model = UpLoadReceiptModel())
        mPresenter!!.requestRecepit(mContext, mOrder_id!!)

    }

    fun initEvent() {
        btn_recipt_upload.setOnClickListener {
            DialogUtil.showDialogTitle(mContext, "提示", "确定上传吗？上传前请仔细检查回单是否正确。",
                    getString(R.string.sure), "检查一下", true, object : DialogUtil.TitleInterface {
                override fun cancleOnClick() {
                }

                override fun sureOnClick() {
                    if (mSelectList == null || mSelectList!!.isEmpty()) {
                        showToast(getString(R.string.please_updata_receipt))
                        return
                    }
                    showProgress()
                    initBaidu()
                    mPresenter!!.submitUpLoadRecepit(mContext, mOrder_id!!, mSelectList!!)
                }

            })

        }
    }

    fun initMutabtList() {
        if (mSelectList == null) {
            mSelectList = mutableListOf()
        }
        if (mType == NO_TYPE) {
            val data: SelectPath = SelectPath()
            data.path = showImgar
            mSelectList!!.add(data)
        }
        setMangager(rlv_updata_recipt, 4, GridLayoutManager.VERTICAL)
        mPictureAdapter = UpdataReciptAdapter(mContext, mSelectList!!, mType == NO_TYPE)
        rlv_updata_recipt.adapter = mPictureAdapter
        mPictureAdapter!!.setOnItemDeleteListener(object : UpdataReciptAdapter.OnItemDeleteListener {
            override fun onItemAddListener(postion: Int, str: SelectPath) {
                toShowDialog(postion)
            }

            override fun onItemShowListener(postion: Int, str: SelectPath) {
                val bunder = Bundle()
                val mData = arrayListOf<SelectPath>()
                for (item in mSelectList!!) {
                    val selectPath = SelectPath()
                    selectPath.type = if (item.type == "1") ImageActivity.BENDI_TYPE else ImageActivity.NET_TYPE;
                    selectPath.path = if (item.type == "1") item.path else DataMessageVo.HTTP_HEAR + item.path;
                    mData.add(selectPath)
                }
                if (mData == null || mData.isEmpty()) {
                    showToast("没有找到要查看图片")
                    return
                }
                bunder.putSerializable(ImageActivity.IMAGEPATH, mData)
                bunder.putInt(ImageActivity.INDEX, postion)
                jumpTo(ImageActivity::class.java, bunder, getString(R.string.receipt_picture))
            }

            override fun onItemDeleteListener(postion: Int, str: SelectPath) {
                mSelectList!!.remove(str)
                hineAdd()
            }

        })

    }

    override fun onBaseNext(base: String, id: Int, img: String) {
        val data = SelectPath()
        data.path = img
        data.updata = base
        data.type = "1"
        mSelectList!!.add(0, data)
        hineAdd()
    }


    fun hineAdd() {
        if (mSelectList != null && !mSelectList!!.isEmpty() && mSelectList!!.size == 4) {
            mSelectList!!.removeAt(3)
            isDelete = true
        } else {
            if (isDelete) {
                if (mType == NO_TYPE) {
                    val data: SelectPath = SelectPath()
                    data.path = showImgar
                    mSelectList!!.add(data)
                }
                isDelete = false
            }
        }
        mPictureAdapter!!.notifyDataSetChanged()

    }


    override fun UpLoadRecepitSuccess(t: Any?) {
        showToast(getString(R.string.subimt_success))
        finishBase()

    }

    override fun UpLoadRecepitError(ex: Throwable) {
        this.onError(ex)
    }

    override fun UpLoadRecepitCompter() {
        this.onComplise()
    }

    override fun LookRecepitSuccess(t: Any?) {
        if (t == null) return
        val data: String = t as String
        val list = Utils.getUserSelectToList(data)
        mSelectList!!.clear()
        isDelete = list.size == 3
        if (list.size < 3) {
            if (mType == NO_TYPE) {
                val data: SelectPath = SelectPath()
                data.path = showImgar
                mSelectList!!.add(data)
            }
        }
        for (index in list.indices) {
            Thread(Runnable {
                down(list[index], index)
            }).start()
        }
    }

    override fun LookRecepitError(ex: Throwable) {
        this.onError(ex)
    }

    override fun LookRecepitCompter() {
        this.onComplise()
    }

    val handle = object : Handler() {
        override fun handleMessage(msg: Message?) {
            msg.let {
                var response = msg!!.obj as Bitmap
                var index = msg!!.arg1
                val save = ImagerUtil.compressAndSave(mContext, response!!)
                picture(save, index)
            }
        }
    }

    fun down(str: String, index: Int) {
        OkHttpUtils.get()
                .url(DataMessageVo.HTTP_HEAR + str)
                .tag(this)
                .build().execute(object : BitmapCallback() {
                    override fun onResponse(response: Bitmap?, id: Int) {
                        val msg = Message.obtain()
                        if (response != null) {
                            msg.obj = response
                            msg.arg1 = index
                            handle.sendMessage(msg)
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?, id: Int) {
                    }
                })

    }

}
