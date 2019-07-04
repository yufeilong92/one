package com.zzzh.akhalteke.utils

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo


/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/6 16:14
 * @Purpose :对话框工具类
 */

object DialogUtil {
/*
    companion object {
        //被companion object包裹的语句都是private的
        private var singletonInstance: DialogUtil? = null

        @Synchronized
        fun getInstance(): DialogUtil? {
            if (singletonInstance == null) {
                singletonInstance = DialogUtil()
            }
            return singletonInstance
        }
    }
*/

    interface SelectCarColorInterface {
        fun onItemClickListener(id: Int);
    }

    fun showSelectCarColorDialog(mContext: Activity, selectListener: SelectCarColorInterface) {
        var mShowCarDialog = AlertDialog.Builder(mContext, R.style.mydialogNobg)
        var view = LayoutInflater.from(mContext).inflate(R.layout.dialog_show_car_color, null)
        mShowCarDialog.setView(view);
        var close = view.findViewById<ImageView>(R.id.iv_select_color_close)
        var bule = view.findViewById<ImageView>(R.id.iv_bule_pz)
        var yellow = view.findViewById<ImageView>(R.id.iv_yellow_pz)
        var green = view.findViewById<ImageView>(R.id.iv_green_pz)
        var huan = view.findViewById<ImageView>(R.id.iv_huan_pz)
        mShowCarDialog.create();
        var dialog = mShowCarDialog.show().apply {
            close.setOnClickListener {
                dismiss()
            }
            bule.setOnClickListener {
                dismiss()
                selectListener.onItemClickListener(DataMessageVo.BULE)
            }
            yellow.setOnClickListener {
                dismiss()
                selectListener.onItemClickListener(DataMessageVo.YELLOW)
            }
            green.setOnClickListener {
                dismiss()
                selectListener.onItemClickListener(DataMessageVo.GREEN)
            }
            huan.setOnClickListener {
                dismiss()
                selectListener.onItemClickListener(DataMessageVo.HUN)
            }
        }


    }

    interface ShowPlayPhoneListener {
        fun onClickPlayListener(str: String)
    }

    //显示拨打电话
    /**
     * @param type 0 为司机  1 为货主
     * @param phone 电话
     * @param play 监听
     */
    fun showPlayPhone(context: Context, type: Int, phone: String, play: ShowPlayPhoneListener) {
        val dialogBuilder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_play_phone, null)
        val tv = view.findViewById<TextView>(R.id.tv_dialog_phone)
        val tv_Phone = view.findViewById<TextView>(R.id.tv_phone_dialog)
        val liPaly = view.findViewById<LinearLayout>(R.id.li_play_phone)

        if (type == 1) {
            tv_Phone.text = tv_Phone.text.toString().replace("司机", "货主")
        }
        tv.text = phone
        dialogBuilder.setView(view)
        dialogBuilder.create()
        val dialog = dialogBuilder.show()
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        liPaly.setOnClickListener {
            dialog.dismiss()
            if (play != null) {
                play.onClickPlayListener(phone)
            }
        }
    }

    interface addBankAgainInterface {
        fun bankAgainListener()
    }

    //显示添加成功

    fun showAddBankSuccesse(context: Context, isSuccess: Boolean, again: addBankAgainInterface) {
        val dialogBuilder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_bank_success, null)
        val img = view.findViewById<ImageView>(R.id.iv_success_colse)
        val tv = view.findViewById<TextView>(R.id.tv_success_see)
        val tv_title = view.findViewById<TextView>(R.id.tv_success_title)
        val btn = view.findViewById<Button>(R.id.btn_bank_again)
        val tv_content = view.findViewById<TextView>(R.id.tv_success_content)
        dialogBuilder.setView(view)
        dialogBuilder.create()
        if (isSuccess) {
            tv_title.text = "添加成功"
            img.setImageResource(R.mipmap.ic_add_bank_success)
            tv_content.visibility = View.GONE
        } else {
            tv_title.text = "添加失败"
            img.setImageResource(R.mipmap.ic_add_bank_error)
            tv_content.visibility = View.VISIBLE
        }
        val show = dialogBuilder.show()
        img.setOnClickListener {
            show.dismiss()
        }
        tv.setOnClickListener {
            show.dismiss()
            if (again != null) {
                again.bankAgainListener()
            }
        }
        btn.setOnClickListener {
            show.dismiss()
            if (again != null) {
                again.bankAgainListener()
            }
        }


    }

    fun showAddWaringDialog(context: Context, title: String, content: String) {
        val dialogBuilde = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_show_warning, null)
        val tv_content = view.findViewById<TextView>(R.id.tv_dialog_warning_content)
        val tv_title = view.findViewById<TextView>(R.id.tv_dialog_warning_title)
        val tv_see = view.findViewById<TextView>(R.id.tv_dialog_warning_see)
        dialogBuilde.setView(view)
        tv_content.text = content
        tv_title.text = title
        dialogBuilde.create()
        val dialog = dialogBuilde.show()
        tv_see.setOnClickListener {
            dialog.dismiss()
        }


    }

    interface TitleInterface {
        fun sureOnClick()
        fun cancleOnClick()
    }

    /**
     * @param context 上下文
     * @param title 标题
     * @param content  内容
     * @param sure  确认按钮
     * @param canle 取消按钮
     * @param canable  是否能外部点击
     * @param inter 接口
     */
    fun showDialogTitle(
        context: Context, title: String?, content: String?,
        sure: String, canle: String, canable: Boolean, inter: TitleInterface
    ) {
        val dialogBuilde = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_show_title_content, null)
        val btn_cancle = view.findViewById<Button>(R.id.btn_dialog_item_cancle)
        val ll_top = view.findViewById<LinearLayout>(R.id.li_titlte_top)
        val btn_sure = view.findViewById<Button>(R.id.btn_dialog_item_sure)
        val tv_titel = view.findViewById<TextView>(R.id.tv_dialog_item_title)
        val tv_cotnent = view.findViewById<TextView>(R.id.tv_dialog_item_content)
        ll_top.visibility = if (StringUtil.isEmpty(title)) View.GONE else View.VISIBLE
        tv_cotnent.visibility = if (StringUtil.isEmpty(content)) View.GONE else View.VISIBLE
        dialogBuilde.setView(view)
        btn_cancle.text = canle
        btn_sure.text = sure
        tv_titel.text = title
        tv_cotnent.text = content
        dialogBuilde.setCancelable(canable)
        dialogBuilde.create()
        val dialog = dialogBuilde.show()
        btn_cancle.setOnClickListener {
            dialog.dismiss()
            inter.cancleOnClick()
        }
        btn_sure.setOnClickListener {
            dialog.dismiss()
            inter.sureOnClick()
        }

    }

    /**
     * @param context 上下文
     * @param title 标题
     * @param canable  是否能外部点击
     * @param inter 接口
     */
    interface ContinueInterface {
        fun continuer()
        fun dialogDismiss(type: Int)
    }

    /**
     * @param title  标题
     * @param type   0 车辆，1 司机 2 实名认证未通过
     * @param canable  是否能取消
     */
    fun showDialogContinueAuthentication(
        context: Context,
        title: String?,
        type: Int,
        canable: Boolean,
        inter: ContinueInterface
    ) {
        val dialogBuilde = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_item_continue, null)
        val tv_title = view.findViewById<TextView>(R.id.tv_dialog_item_continue_title)
        val btn_continue = view.findViewById<Button>(R.id.btn_go_contniue_renzheng)
        val ivDialogItem = view.findViewById<ImageView>(R.id.iv_dialog_item_continue)
        if (type == 2) {
            ivDialogItem.setImageResource(R.mipmap.ic_audit_defeated)
        }
        dialogBuilde.setView(view)
        dialogBuilde.setCancelable(canable)
        dialogBuilde.create()
        val dialog = dialogBuilde.show()
        tv_title.text = title
        dialog.setOnDismissListener {
            inter.dialogDismiss(type)
        }
        btn_continue.setOnClickListener {
            if (dialog.isShowing)
                dialog.dismiss()
            inter.continuer()
        }

    }
}