package com.zzzh.akhalteke.ui.verify.newverify

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.DriverResultsVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.mvp.contract.DrivingContract
import com.zzzh.akhalteke.mvp.model.DrivingModel
import com.zzzh.akhalteke.mvp.presenter.DrivingPresenter
import com.zzzh.akhalteke.ui.SelectorImageActivity

import com.zzzh.akhalteke.ui.verify.DrivingMemberActivity
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_driving_new_member.*
import kotlinx.android.synthetic.main.activity_drivingmenber.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*
import java.io.File

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.verify.newverify
 * @Package com.zzzh.akhalteke.ui.verify.newverify
 * @Email : yufeilong92@163.com
 * @Time :2019/6/3 13:54
 * @Purpose :驾驶员信息认证
 */
class DrivingNewMemberActivity : SelectorImageActivity(), DrivingContract.View {
    var mUserType: Int? = 1
    var mType: String? = ""
    var map: HashMap<Int, String>? = null
    var mPresenter: DrivingPresenter? = null;

    companion object {
        val TYPE = "type"
        val TYPE_Finish = "finish"
        val TYPE_NO_FINISH = "no_finish"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driving_new_member)
        if (intent != null) {
            mType = intent.getStringExtra(TYPE)
        }
        tv_activity_title.text = getString(R.string.car_diving)
        initRquest()
        initEvent()
    }


    fun initEvent() {
        map = hashMapOf()
        //主驾
        rl_driving_main_driver.setOnClickListener {
            if (mUserType != DataMessageVo.CarIdF)
                clearImageView()
            mUserType = DataMessageVo.CarIdF;
            setZF(true, false)
        }
        //副驾
        rl_driving_fu_driver.setOnClickListener {
            if (mUserType != DataMessageVo.Hear)
                clearImageView()
            mUserType = DataMessageVo.Hear;
            setZF(false, true)
        }
        /*      radio_grop.setOnCheckedChangeListener { group, checkedId ->
                  if (checkedId == R.id.radbtn_one) {//主驾
                      if (mUserType != DataMessageVo.CarIdF)
                          clearImageView()
                      mUserType = DataMessageVo.CarIdF;
                  } else if (checkedId == R.id.radbtn_two) {//副驾
                      if (mUserType != DataMessageVo.Hear)
                          clearImageView()
                      mUserType = DataMessageVo.Hear;
                  }
              }*/
        iv_driving_f.setOnClickListener {
            //行驶证
            toShowDialog(DataMessageVo.CarIdZ)
        }
        iv_driving_huoche.setOnClickListener {
            //货车行驶证件
            toShowDialog(DataMessageVo.CarIdF)
        }
        tv_driving_submit.setOnClickListener {
            submitDriving()
        }
    }

    fun setZF(z: Boolean, f: Boolean) {
        iv_car_select_main.setImageResource(if (z) R.drawable.ic_oval_n else R.drawable.ic_oval_s)
        iv_car_select_fu.setImageResource(if (f) R.drawable.ic_oval_n else R.drawable.ic_oval_s)
    }

    fun initRquest() {
        mPresenter = DrivingPresenter()
        mPresenter!!.initMvp(this, m = DrivingModel())
    }

    fun clearImageView() {
        map!!.clear()
        iv_driving_f.setImageResource(R.mipmap.jiashizheng)
        iv_driving_huoche.setImageResource(R.mipmap.jiashizheng)
    }

    override fun onBaseNext(base: String, id: Int, img: String) {
        if (id == -1) return
        map!!.put(id, base)
        var url = Uri.parse(img)
        if (id == DataMessageVo.CarIdZ) {
            iv_driving_f.setImageURI(url)
//            ll_driver_result.visibility = View.GONE
            tv_driving_name.text = ""
            tv_driving_certificate.text = ""
            showProgress()
            mPresenter!!.submitDriverLicense(mContext, File(img))
        } else if (id == DataMessageVo.CarIdF) {
            iv_driving_huoche.setImageURI(url)
//            showProgress()
//            mPresenter!!.submitDriverQualification(mContext, File(img))
        }
    }

    fun submitDriving() {
        if (map == null || map!!.isEmpty()) {
            showToast(getString(R.string.please_up_diving))
            return
        }
        val name = Utils.getObjToStr(tv_driving_name)
        val diverCertificatie = Utils.getObjToStr(tv_driving_certificate)
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(diverCertificatie)) {
            showToast("驾驶证识别失败，请重新上传")
            return
        }

        if (map!!.get(DataMessageVo.CarIdZ) == null) {
            showToast(getString(R.string.please_up_diving))
            return
        }
//        if(map!![DataMessageVo.CarIdF]!=null){
//            val quesi = Utils.getObjToStr(tv_driver_cetificate_quesi)
//            val vaild = Utils.getObjToStr(tv_driver_cetificate_valid_until)
//            if (StringUtil.isEmpty(quesi)||StringUtil.isEmpty(vaild)){
//                showToast("货车驾驶资格证识别失败，请重新上传")
//                return
//            }
//        }

        showProgress()
        mPresenter!!.submitDivingInfom(mContext, map!!, mUserType.toString())

    }

    override fun SubmitSuccess(t: Any?) {
        val role: String = t as String
//        showToast(getString(R.string.submit_success))
        val help = UserDbHelp.get_Instance(mContext)
        help!!.upDriver("1", role)
        if (mType == TYPE_Finish) {
            finishBase()
            return
        }
        val bundle = Bundle()
        bundle.putString(CarNewActivity.TYPE, "")
        jumpTo(CarNewActivity::class.java, bundle)
        finishBase()
    }

    override fun SubmitError(ex: Throwable) {
        this.onError(ex)
    }

    override fun SubmitComplise() {
        this.onComplise()
    }

    override fun DriverLicenseSuccess(t: Any?) {
        if (t == null) {
//            ll_driver_result.visibility = View.GONE
            return
        }

        val data: DriverResultsVo = t as DriverResultsVo
        map!!.put(DataMessageVo.CarIdZ, data.imagePath!!)
//        ll_driver_result.visibility = View.VISIBLE
        tv_driving_name.text = data.name
        tv_driving_certificate.text = data.number


    }

    override fun DriverLicenseError(ex: Throwable) {
        this.onError(ex)
    }

    override fun DriverLicenseComplise() {
        this.onComplise()
    }

}