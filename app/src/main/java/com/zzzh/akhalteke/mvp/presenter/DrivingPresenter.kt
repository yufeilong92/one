package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.DrivingContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import java.io.File

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/11 11:11
 * @Purpose :驾驶员信息认证
 */

class DrivingPresenter : DrivingContract.Presenter {

    var view: DrivingContract.View? = null;
    var model: DrivingContract.Model? = null;


    override fun initMvp(v: DrivingContract.View, m: DrivingContract.Model) {
        this.view=v
        this.model=m
    }
    override fun submitDivingInfom(mont: Context,  map: HashMap<Int, String>, role: String) {
        model!!.submitDivingInfom(mont,map,role,object :RequestResultInterface{
             override fun <T> Success(t: T) {
                 view!!.SubmitSuccess(t)
             }

             override fun onError(ex: Throwable) {
                 view!!.SubmitError(ex)
             }

             override fun onComplise() {
                 view!!.SubmitComplise()
             }

         })
    }
    override fun submitDriverLicense(m: Context, file: File) {
        model!!.submitDriverLicense(m,file,object :RequestResultInterface{
            override fun onError(ex: Throwable) {
                view!!.DriverLicenseError(ex)
            }

            override fun onComplise() {
                view!!.DriverLicenseComplise()
            }

            override fun <T> Success(t: T) {
                view!!.DriverLicenseSuccess(t)
            }
        })
    }


}