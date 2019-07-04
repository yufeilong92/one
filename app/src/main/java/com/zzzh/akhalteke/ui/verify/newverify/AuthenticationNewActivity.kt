package com.zzzh.akhalteke.ui.verify.newverify

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.IDCardFResultsVo
import com.zzzh.akhalteke.bean.vo.IDCardZResultsVo
import com.zzzh.akhalteke.bean.vo.RealNameVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.mvp.contract.AuthenticationContract
import com.zzzh.akhalteke.mvp.model.AuthenticationModel
import com.zzzh.akhalteke.mvp.presenter.AuthenticationPresenter
import com.zzzh.akhalteke.ui.SelectorImageActivity
import com.zzzh.akhalteke.ui.verify.AuthenticationActivity
import com.zzzh.akhalteke.ui.verify.DrivingMemberActivity
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.activity_authentication_new.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*
import java.io.File

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.verify.newverify
 * @Package com.zzzh.akhalteke.ui.verify.newverify
 * @Email : yufeilong92@163.com
 * @Time :2019/6/3 11:44
 * @Purpose :实名认证
 */
class AuthenticationNewActivity : SelectorImageActivity(), AuthenticationContract.View {

    //储存图片地址的base64
    lateinit var mSelectPicture: HashMap<Int, String>
    var mPresenter: AuthenticationPresenter? = null
    var mPhone: String? = null;


    companion object {
        val USERPHONE: String = "phone"
        val TYPE = "TYPE"
        val TYPE_FINISH = "finish"
        val TYPE_NO_FINISH = "Nofinish"
    }

    private var mType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_new)
        if (intent != null) {
            mPhone = intent.getStringExtra(AuthenticationNewActivity.USERPHONE)
            mType = intent.getStringExtra(AuthenticationNewActivity.TYPE)

        }
        tv_activity_title.text = getString(R.string.authentication)
        tv_gm_right_title.text = getString(R.string.contact_service)
        tv_gm_right_title.setOnClickListener {
            Utils.playPhone(mContext, DataMessageVo.KEFU_PHONE)
        }
        initRequest()
        initEvent()
    }


    fun initEvent() {
        mSelectPicture = hashMapOf()
        iv_authen_id_z.setOnClickListener {

            toShowDialog(DataMessageVo.CarIdZ)
        }
        iv_authen_id_f.setOnClickListener {
            //证件反面
            toShowDialog(DataMessageVo.CarIdF)
        }
        iv_authen_hear.setOnClickListener {
            //头像
            toShowDialog(DataMessageVo.Hear)
        }
        btn_authen_authentication.setOnClickListener {
            subimtInfom()
        }
    }

    fun initRequest() {
        mPresenter = AuthenticationPresenter()
        mPresenter!!.initMvp(this, m = AuthenticationModel())
    }

    override fun onBaseNext(base: String, id: Int, img: String) {
        if (id == -1) return
        val url = Uri.parse(img)
        mSelectPicture.put(id, base)
        if (id == DataMessageVo.CarIdZ) {
//            证明正面
//            ll_sfz_z.visibility = View.GONE
            tv_authen_name.text=""
            tv_authen_idnumber.text=""
            iv_authen_id_z.setImageURI(url)
            showProgress()
            mPresenter!!.submitUserIdCarFace(mContext, File(img))
        } else if (id == DataMessageVo.CarIdF) {
//            ll_sfz_f.visibility = View.GONE
            tv_authen_visa.text=""
            tv_authen_validity.text=""
            iv_authen_id_f.setImageURI(url)
            showProgress()
            mPresenter!!.submitUserIdCarBack(mContext, File(img))
        } else if (id == DataMessageVo.Hear) {
            iv_authen_hear.setImageURI(url)
        }

    }


    fun subimtInfom() {
        //提交加密的集合
        if (mSelectPicture == null || mSelectPicture.isEmpty()) {
            showToast(getString(R.string.please_up_id))
            return
        }
        val name = Utils.getObjToStr(tv_authen_name)
        val idnumber = Utils.getObjToStr(tv_authen_idnumber)
        if (StringUtil.isEmpty(name)|| StringUtil.isEmpty(idnumber)){
            showToast("身份证正面识别失败,请重新上传")
            return
        }

        if (mSelectPicture.get(DataMessageVo.CarIdZ) == null) {
            showToast(getString(R.string.please_up_id))
            return
        }
        if (mSelectPicture.get(DataMessageVo.CarIdF) == null) {
            showToast(getString(R.string.please_up_id_f))
            return
        }
        val visa = Utils.getObjToStr(tv_authen_visa)
        val indate = Utils.getObjToStr(tv_authen_validity)
        if (StringUtil.isEmpty(visa)|| StringUtil.isEmpty(indate)){
            showToast("身份证反面识别失败,请重新上传")
            return
        }

        if (mSelectPicture.get(DataMessageVo.Hear) == null) {
            showToast(getString(R.string.please_up_me))
            return
        }
        showProgress()
        mPresenter!!.submitUserInfom(
                mContext, idnumber, mSelectPicture, name
        )

    }


    override fun SubmitSuccess(t: Any?) {
        val data: RealNameVo = t as RealNameVo
        val help = UserDbHelp.get_Instance(mContext)
        help!!.upReal("1", data.name, data.phone, data.number, data.portrait)
//        jumpTo(MainActivity::class.java)
        if (mType == AuthenticationNewActivity.TYPE_FINISH) {
            clear()
            finish()
            return
        }
        val bundle = Bundle()
        bundle.putString(DrivingNewMemberActivity.TYPE, DrivingNewMemberActivity.TYPE_NO_FINISH)
        jumpTo(DrivingNewMemberActivity::class.java, bundle)
        clear()
        finish()
    }

    override fun SubmitError(ex: Throwable) {
        this.onError(ex)
    }

    override fun SubmitCompter() {
        this.onComplise()
    }

    fun clear() {
        mSelectPicture.clear()
    }

    override fun FileFaceSuccess(t: Any?) {
        if (t == null) {
//            ll_sfz_z.visibility = View.GONE
            return
        }
//        ll_sfz_z.visibility = View.VISIBLE
        val data: IDCardZResultsVo =t as IDCardZResultsVo
        mSelectPicture.put(DataMessageVo.CarIdZ, data.savePath!!)
        tv_authen_name.text=data.name
        tv_authen_idnumber.text=data.idnumber


    }

    override fun FileFaceError(ex: Throwable) {
        this.onError(ex)
    }

    override fun FileFaceCompter() {
        this.onComplise()
    }

    override fun FileBackSuccess(t: Any?) {
        if (t == null) {
//            ll_sfz_f.visibility = View.GONE
            return
        }
//        ll_sfz_f.visibility = View.VISIBLE
        val data: IDCardFResultsVo =t as IDCardFResultsVo
        mSelectPicture.put(DataMessageVo.CarIdF, data.savePath!!)
        tv_authen_visa.text=data.issue
        tv_authen_validity.text=data.effectiveTime
    }

    override fun FileBackError(ex: Throwable) {
        this.onError(ex)
    }

    override fun FileBackCompter() {
        this.onComplise()
    }
}