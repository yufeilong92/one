package com.zzzh.akhalteke.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.SelectPath
import com.zzzh.akhalteke.ui.waybill.UpdataReciptActvity
import com.zzzh.akhalteke.utils.ImageLoadingUtils
import com.zzzh.akhalteke.utils.Utils

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/18 15:51
 * @Purpose :回执单适配器
 */
class UpdataReciptAdapter(var context: Context,  var infoList: MutableList<SelectPath>,var show:Boolean) :
        BaseQuickAdapter<SelectPath, BaseViewHolder>(R.layout.item_updata_recipt, infoList) {
    override fun convert(helper: BaseViewHolder?, item: SelectPath?) {
        val position = helper!!.layoutPosition
        val imgDelete = helper!!.getView<ImageView>(R.id.iv_recipt_img_delete)
        val imgReciptImgShow = helper!!.getView<SimpleDraweeView>(R.id.iv_recipt_img_show)
        val imgReciptadd = helper!!.getView<ImageView>(R.id.iv_recipt_img_add)
        val rlReciptShow = helper!!.getView<RelativeLayout>(R.id.rl_recipt_show)
        if (item!!.path.equals(UpdataReciptActvity.showImgar)) {
            rlReciptShow.visibility = View.GONE
            imgReciptadd.visibility = View.VISIBLE
        } else {
            rlReciptShow.visibility = View.VISIBLE
            if (show){
                imgDelete.visibility=View.VISIBLE
            }else{
                imgDelete.visibility=View.GONE
            }
            imgReciptadd.visibility = View.GONE
//            val url = Uri.parse(item.path)
//            imgReciptImgShow.set(url)
            if (item.type == "1") {
                ImageLoadingUtils.loadLocalImage(imgReciptImgShow, item.path!!)
            } else {
                Utils.showNetImager(imgReciptImgShow, item.path)
            }
        }

        imgDelete.setOnClickListener {
            if (ItemDeleteListener != null) {
                ItemDeleteListener!!.onItemDeleteListener(position, item)
            }
        }
        imgReciptadd.setOnClickListener {
            if (ItemDeleteListener != null) {
                ItemDeleteListener!!.onItemAddListener(position, item)
            }
        }
        imgReciptImgShow.setOnClickListener {
            if (ItemDeleteListener != null) {
                ItemDeleteListener!!.onItemShowListener(position, item)
            }
        }
    }

    interface OnItemDeleteListener {
        fun onItemDeleteListener(postion: Int, str: SelectPath)
        fun onItemAddListener(postion: Int, str: SelectPath)
        fun onItemShowListener(postion: Int, str: SelectPath)
    }

    var ItemDeleteListener: OnItemDeleteListener? = null
    fun setOnItemDeleteListener(delete: OnItemDeleteListener) {
        this.ItemDeleteListener = delete
    }


}