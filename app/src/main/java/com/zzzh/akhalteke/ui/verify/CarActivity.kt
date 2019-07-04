package com.zzzh.akhalteke.ui.verify

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.zzzh.akhalteke.MainActivity
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.CarVo
import com.zzzh.akhalteke.bean.vo.DriveringResultsVo
import com.zzzh.akhalteke.mvp.contract.CarContract
import com.zzzh.akhalteke.mvp.model.CarModel
import com.zzzh.akhalteke.mvp.presenter.CarPresenter
import com.zzzh.akhalteke.ui.SelectorImageActivity
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import com.zzzh.akhalteke.view.dialog.SelectCarColorDialog
import kotlinx.android.synthetic.main.activity_car.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*
import java.io.File

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/6 12:02
 * @Purpose : 车辆认证
 */

class CarActivity : SelectorImageActivity(), CarContract.View {

    var mPaths: HashMap<Int, String>? = null
    var mCarTypeId: String = "";
    var mCarColor: Int = 0
    var mPresenter: CarPresenter? = null
    var mCarLengtId: String = ""
    var carLengtName: String = ""

    companion object {
        val CARTYPENUMBER: Int = 1003;
        val CARLENGTHNUMBER: Int = 1004;
        val CARLENGTH_RESULT: Int = 1005;
        val CARTYPE_ID: String = "id"
        val CARTYPE_NAME: String = "name"
        val TYPE: String = "type"//类型
        val ME_INFOM_TYPE: String = "me_type"//我的界面进
    }

    private var mType: String? = ""

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_car)
        if (intent != null) {
            mType = intent.getStringExtra(TYPE)
        }
        tv_activity_title.text=getString(R.string.car_authentication)
        initRequest();
        initView();
    }

    fun initRequest() {
        mPresenter = CarPresenter()
        mPresenter!!.initMvp(this, model = CarModel())

    }

    fun initView() {
        mPaths = hashMapOf()
        iv_user_xingshi.setOnClickListener {
            //行驶证
            toShowDialog(DataMessageVo.CarIdZ)
        }
        iv_jing_ying.setOnClickListener {
            //经营证件照
            toShowDialog(DataMessageVo.CarIdF)
        }
        tv_car_color.setOnClickListener {
            //车牌颜色
            val cars = Utils.getArrayString(mContext, R.array.car_paizhao)
            val selectCarColorDialog =
                    SelectCarColorDialog(this, object : SelectCarColorDialog.SelectCarColorInterface {
                        override fun onItemClickListener(id: Int) {
                            tv_car_color.text = cars[id - 1]
                            //todo处理用户选中牌照
                            if (id == DataMessageVo.BULE) {
                                mCarColor = DataMessageVo.BULE
                            } else if (id == DataMessageVo.YELLOW) {
                                mCarColor = DataMessageVo.YELLOW
                            } else if (id == DataMessageVo.GREEN) {
                                mCarColor = DataMessageVo.GREEN
                            } else if (id == DataMessageVo.HUN) {
                                mCarColor = DataMessageVo.HUN
                            }
                        }
                    })
            selectCarColorDialog.show()

        }

        tv_car_number.setOnClickListener {
            jumpToFoResulBU(
                    CarNumberActivity::class.java,
                    CARLENGTHNUMBER
            )
        }
        tv_car_type.setOnClickListener {
            //车型
            jumpToFoResulBU(
                    CarTypeActivity::class.java,
                    CARTYPENUMBER
            )
        }
        tv_car_leng.setOnClickListener {
            //车长
            jumpToFoResulBU(
                    CarLengthActivity::class.java,
                    CARLENGTH_RESULT
            )
        }
        btn_submit.setOnClickListener {
            submit()
        }
    }

    fun submit() {
        if (mPaths == null || mPaths!!.isEmpty()) {
            showToast(getString(R.string.please_car_driviing))
            return
        }
        if (mPaths!!.get(DataMessageVo.CarIdZ) == null) {
            showToast(getString(R.string.please_car_driviing))
            return
        }
        if (mCarColor == 0) {
            showToast(getString(R.string.please_car_color))
            return
        }

        val carnumber = Utils.getObjToStr(tv_car_number)
        if (StringUtil.isEmpty(carnumber)) {
            showToast(getString(R.string.please_car_number))
            return
        }
        if (StringUtil.isEmpty(mCarTypeId)) {
            showToast(getString(R.string.please_car_type))
            return
        }
        val length = Utils.getObjToStr(tv_car_leng)
        if (StringUtil.isEmpty(length)) {
            showToast(getString(R.string.please_car_length))
            return
        }
        val kg = Utils.getObjToStr(et_car_kg)
        if (StringUtil.isEmpty(kg)) {
            showToast(getString(R.string.pleas_car_ke))
            return
        }
        showProgress()
        mPresenter!!.submitCarInfom(
                mContext, carnumber, mCarColor.toString(),
                mCarTypeId, mCarLengtId, kg, mPaths!!
        )

    }

    override fun onBaseNext(base: String, id: Int, img: String) {
        if (id == -1) return
        var url = Uri.parse(img)
        mPaths!!.put(id, base)
        if (id == DataMessageVo.CarIdZ) {
            iv_user_xingshi.setImageURI(url)
            ll_driver_jiashi.visibility= View.GONE
            tv_car_number.text=""
            et_belongs_who.setText("")
            showProgress()
            mPresenter!!.submitDrivingLicense(mContext, File(img))
        } else if (id == DataMessageVo.CarIdF) {
            iv_jing_ying.setImageURI(url)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (resultCode == CARTYPENUMBER) {//车型
            val extra = data.getStringExtra(
                    CARTYPE_NAME
            )
            mCarTypeId = data.getStringExtra(CARTYPE_ID)
            tv_car_type.text = extra
        } else if (resultCode == CARLENGTHNUMBER) {//车牌
            val number = data.getStringExtra(CarNumberActivity.CARNUMBERKEY)
            val replace = number.replace("•", "")
            tv_car_number.text = replace
        } else if (resultCode == CARLENGTH_RESULT) {
            mCarLengtId = data.getStringExtra(CARTYPE_ID)
            carLengtName = data.getStringExtra(CARTYPE_NAME)
            if (!StringUtil.isEmpty(carLengtName)) {
                tv_car_leng.text = carLengtName + "米"
            }
        }
    }

    override fun SubmitSuccess(t: Any?) {
        val data: CarVo = t as CarVo
        val help = UserDbHelp.get_Instance(mContext)
        help!!.upCar("1", data.plateNumber!!, data.carPlateColourId!!)
//        showToast(getString(R.string.submit_success))
        if (mType == ME_INFOM_TYPE) {
            finishBase()
            return
        }
        jumpTo(MainActivity::class.java)
        finish()
    }

    override fun SubmitError(ex: Throwable) {
        this.onError(ex)
    }

    override fun SubmitComplise() {
        this.onComplise()
    }
    override fun DrivingLicenseSuccess(t: Any?) {
        if(t==null){
            ll_driver_jiashi.visibility= View.GONE
            return
        }
        val data:DriveringResultsVo=t as DriveringResultsVo
        mPaths!!.put(DataMessageVo.CarIdZ, data.imagePath!!)
        ll_driver_jiashi.visibility= View.VISIBLE
        tv_car_number.text=data.plateNumber
        et_belongs_who.setText(data.owner)
    }

    override fun DrivingLicenseError(ex: Throwable) {
     this.onError(ex)
    }

    override fun DrivingLicenseComplise() {
        this.onComplise()
    }

}