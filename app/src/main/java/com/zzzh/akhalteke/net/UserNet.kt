package com.zzzh.akhalteke.net

import android.content.Context
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.bean.vo.SelectPath
import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.retrofit.RetrofitFactory
import com.zzzh.akhalteke.utils.MD5
import com.zzzh.akhalteke.utils.StringUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserNet private constructor() {
    //静态内部类单例模式
    companion object {
        var getInstance = UserNet.instance
    }

    private object UserNet {
        var instance = UserNet();
    }

    //获取验证码
    fun getCode(context: Context, phone: String, m: StringResultInterface) {
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().getCode(
                    phone = phone
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }


    }

    //验证验证码
    fun checkCode(context: Context, phone: String, code: String, m: StringResultInterface) {
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().getCheckCode(
                    phone = phone,
                    code = code,
                    phoneType = DataMessageVo.PHONETYPE,
                    brand = DataMessageVo.BRAND,
                    model = DataMessageVo.MODEL
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //实名认证
//    @SuppressLint("CheckResult")
    fun shubmitUserInfom(
            context: Context, iDnumber: String, map: HashMap<Int, String>,
            name: String, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            var file: File? = null
            if (!StringUtil.isEmpty(map[DataMessageVo.Hear])) {
                file = File(map[DataMessageVo.Hear] ?: "")
            }
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            builder.addFormDataPart("id", mUser.userId)
            builder.addFormDataPart("iDnumber", iDnumber)
            builder.addFormDataPart("iDAddress", map[DataMessageVo.CarIdZ] ?: "")
            builder.addFormDataPart("iDAddressReverse", map[DataMessageVo.CarIdF] ?: "")
            builder.addFormDataPart("name", name)
            val str = "id=${mUser.userId}" +
                    "&iDnumber=${iDnumber}&iDAddress=${map[DataMessageVo.CarIdZ]}" +
                    "&iDAddressReverse=${map[DataMessageVo.CarIdF]}&name=${name}"
            var toSortData = toSortData(str)
            toSortData += "&token=${mUser.token}"
            val handParams = MD5.getMD5String(toSortData).toUpperCase()
            builder.addFormDataPart("sign", handParams)
            if (file != null) {
                val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
                builder.addFormDataPart("portrait", file.name, imageBody)//imgfile 后台接收图片流的参数名
            }
            val parts = builder.build().parts()
            RetrofitFactory.createMainRetrofit().submitUserInfom(
//                    id = mUser.userId,
//                    iDnumber = iDnumber,
//                    iDAddress = map[DataMessageVo.CarIdZ] ?: "",
//                    iDAddressReverse = map[DataMessageVo.CarIdF] ?: "",
//                    portrait = map[DataMessageVo.Hear] ?: "",
//                    name = name
                    parts
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //驾驶员信息认证
    fun submitDrivieInfom(
            context: Context, map: HashMap<Int, String>, role: String,
            m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!

        if (RetrofitFactory.judgmentNetWork(context)) {
            var file: File? = null
            if (!StringUtil.isEmpty(map[DataMessageVo.CarIdF])) {
                file = File(map[DataMessageVo.CarIdF] ?: "")
            }
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            builder.addFormDataPart("id", mUser.userId)
            builder.addFormDataPart("driversLicense", map[DataMessageVo.CarIdZ] ?: "")
            builder.addFormDataPart("role", role)
            val str = "id=${mUser.userId}" +
                    "&driversLicense=${map[DataMessageVo.CarIdZ] ?: ""}&role=${role}"
            var toSortData = toSortData(str)
            toSortData += "&token=${mUser.token}"
            val handParams = MD5.getMD5String(toSortData).toUpperCase()
            builder.addFormDataPart("sign", handParams)
            if (file != null) {
                val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
                builder.addFormDataPart("qualificationLicense", file.name, imageBody)//imgfile 后台接收图片流的参数名
            }
            val parts = builder.build().parts()
            RetrofitFactory.createMainRetrofit().submitDriverInfom(
                    parts
//                    id = mUser.userId,
//                    driversLicense = map.get(DataMessageVo.CarIdZ) ?: "",
//                    role = role,
//                    qualificationLicense = map.get(DataMessageVo.CarIdF) ?: ""
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //车辆信息认证
    fun submitAuthCarInfom(
            context: Context, map: HashMap<Int, String>,
            plateNumber: String, carPlateColour: String, carType: String, carLength: String,
            load: String,
            m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            var file: File? = null
            if (StringUtil.isEmpty(map[DataMessageVo.CarIdF])) {
            } else
                file = File(map[DataMessageVo.CarIdF] ?: "")
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            builder.addFormDataPart("id", mUser.userId)
            builder.addFormDataPart("drivingLicense", map[DataMessageVo.CarIdZ] ?: "")
            builder.addFormDataPart("plateNumber", plateNumber)
            builder.addFormDataPart("carPlateColour", carPlateColour)
            builder.addFormDataPart("carType", carType)
            builder.addFormDataPart("carLength", carLength)
            builder.addFormDataPart("load", load)
            val str = "id=${mUser.userId}" +
                    "&drivingLicense=${map[DataMessageVo.CarIdZ] ?: ""}" +
                    "&plateNumber=${plateNumber}" +
                    "&carPlateColour=${carPlateColour}" +
                    "&carType=${carType}" +
                    "&carLength=${carLength}" +
                    "&load=${load}"
            var toSortData = toSortData(str)
            toSortData += "&token=${mUser.token}"
            val handParams = MD5.getMD5String(toSortData).toUpperCase()
            builder.addFormDataPart("sign", handParams)
            if (file != null) {
                val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
                builder.addFormDataPart("serviceLicense", file.name, imageBody)//imgfile 后台接收图片流的参数名
            }
            val parts = builder.build().parts()

            RetrofitFactory.createMainRetrofit().submitAuthCarInfom(
                    parts
//                    id = mUser.userId,
//                    driversLicense = map.get(DataMessageVo.CarIdZ) ?: "",
//                    serviceLicense = map.get(DataMessageVo.CarIdF) ?: "",
//                    plateNumber = plateNumber,
//                    carPlateColour = carPlateColour,
//                    carType = carType,
//                    carLength = carLength,
//                    load = load
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //获取货源列表（发货中）
    fun getCoodsInfomList(
            context: Context, loadAreaCode: String, unloadAreaCode: String,
            sortType: String, screen: String, type: String, loadTime: String,
            carLength: String, carType: String,
            m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().getGoodsInfomList(
                    id = mUser.userId,
                    loadAreaCode = loadAreaCode,
                    unloadAreaCode = unloadAreaCode,
                    sortType = sortType,
                    type = type,
                    screen = screen,
                    carType = carType,
                    carLength = carLength,
                    loadTime = loadTime
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //查看货源详情（发货中）
    fun requestGoodInfom(
            context: Context, goodsId: String,
            m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().getGoodsInfom(
                    id = mUser.userId,
                    goodsId = goodsId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //获取车长列表
    fun getCarLength(
            context: Context,
            m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().getCarLength(
                    id = mUser.userId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //获取车型列表
    fun getCarType(
            context: Context,
            m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().getCarType(
                    id = mUser.userId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    fun requestFindGoodList(
            context: Context, loadAreaCode: String, unloadAreaCode: String,
            sortType: String, type: String, loadTime: String,
            carLength: String, carType: String, page: Int, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestDriverList(
                    id = mUser.userId,
                    loadAreaCode = loadAreaCode,
                    unloadAreaCode = unloadAreaCode,
                    sortType = sortType,
                    type = type,
                    loadTime = loadTime,
                    carLength = carLength,
                    carType = carType,
                    page = page.toString(),
                    size = DataMessageVo.CINT_PANGE_SIZE.toString()
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //查看货主信息简介
    fun requestShipperById(context: Context, shipperId: String, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestShipperById(
                    carOwnerId = mUser.userId,
                    shipperId = shipperId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //查看货主货源列表
    fun requestShipperGoodList(context: Context, shipperid: String, page: Int, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestShipperGoods(
                    carOwnerId = mUser.userId,
                    shipperId = shipperid,
                    page = page.toString(),
                    size = DataMessageVo.CINT_PANGE_SIZE.toString()
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //司机端获取订单列表
    fun requestOrderList(context: Context, status: String, page: Int, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestOrderList(
                    id = mUser.userId,
                    status = status,
                    page = page.toString(),
                    size = DataMessageVo.CINT_PANGE_SIZE.toString()
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //查看司机端订单详情
    fun requestOrderInfom(context: Context, orderId: String, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestOrderInfom(
                    userId = mUser.userId,
                    orderId = orderId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //司机端查看协议详情
    fun requestAgreementInfom(context: Context, orderId: String, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestAgreementInfom(
                    userId = mUser.userId,
                    orderId = orderId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //司机确认协议
    fun submitAgreementConfirm(context: Context, orderId: String, agree: Boolean, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        var agre = DataMessageVo.SURE_ARGREEMENT
        if (!agree) {
            agre = DataMessageVo.CANCLE_ARGREEMENT
        }

        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().submitAgreementConfirm(
                    carOwnerId = mUser.userId,
                    agree = agre,
                    orderId = orderId

            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //银行卡列表
    fun requestBankCardList(context: Context, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requesBankCardList(
                    userId = mUser.userId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //检查银行卡类型
    fun requestBankTyep(context: Context, cardNumber: String, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requesCheckCardType(
                    userId = mUser.userId,
                    cardNumber = cardNumber
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //根据银行卡id删除
    fun submitBankCardDelete(context: Context, cardId: String, m: StringResultInterface) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().submitBankCardDelete(
                    userId = mUser.userId,
                    cardId = cardId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //根据银行卡id添加
    fun submitBankCardAdd(
            context: Context, name: String, idCardNumber: String, cardNumber: String,
            phone: String, bank: String, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().submitBankCardAdd(
                    userId = mUser.userId,
                    name = name,
                    idCardNumber = idCardNumber,
                    cardNumber = cardNumber,
                    phone = phone,
                    bank = bank
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //意见反馈
    fun submitFeedBack(
            context: Context, content: String, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().submitFeedBack(
                    userId = mUser.userId,
                    content = content
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //个人中心
    fun requestDriverInfom(
            context: Context, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().reqeustDriverUser(
                    userId = mUser.userId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //查看回单
    fun requestLookReceipt(
            context: Context, orderId: String, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestOrderRecepit(
                    userId = mUser.userId,
                    orderId = orderId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //上传回单
    fun submitUploadReceipt(
            context: Context, orderId: String, map: MutableList<SelectPath>, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        val selectPath = hashMapOf<Int, String>()
        for (id in map.indices) {
            selectPath.put(id, map[id].updata!!)
        }


        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().submitUploadRecepit(
                    userId = mUser.userId,
                    orderId = orderId,
                    receiptOne = selectPath[DataMessageVo.ZONE] ?: "",
                    receiptTwo = selectPath[DataMessageVo.ONE] ?: "",
                    receiptThree = selectPath[DataMessageVo.TWO] ?: ""
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //上传回单
    fun submitUploadReceiptOne(
            context: Context, orderId: String, map: MutableList<SelectPath>, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
        builder.addFormDataPart("userId", mUser.userId)
        builder.addFormDataPart("orderId", orderId)
        var str = "userId=${mUser.userId}&orderId=$orderId"
        str = toSortData(str);
        str += "&token=${mUser.token}"
        val handParams = MD5.getMD5String(str).toUpperCase()
        builder.addFormDataPart("sign", handParams)
        for (id in map.indices) {
            if (id == 0) {
                if (!StringUtil.isEmpty(map[id].updata)) {
                    val file = File(map[id].updata!!)
                    val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
                    builder.addFormDataPart("receiptOne", file.name, imageBody)//imgfile 后台接收图片流的参数名
                }

            } else if (id == 1) {
                if (!StringUtil.isEmpty(map[id].updata)) {
                    val file = File(map[id].updata!!)
                    val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
                    builder.addFormDataPart("receiptTwo", file.name, imageBody)//imgfile 后台接收图片流的参数名

                }

            } else if (id == 2) {
                if (!StringUtil.isEmpty(map[id].updata)) {
                    val file = File(map[id].updata!!)
                    val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
                    builder.addFormDataPart("receiptThree", file.name, imageBody)//imgfile 后台接收图片流的参数名
                }
            }
        }
        val parts = builder.build().parts()
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().submitUploadRecepitOne(
                    parts
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //钱包首页
    fun requestCenterAccount(
            context: Context, page: Int, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestCenterAccount(
                    userId = mUser.userId,
                    page = page.toString(),
                    size = DataMessageVo.CINT_PANGE_SIZE.toString()

            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //获取公司账户
    fun requestCompanyAccount(
            context: Context, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestCompanyAccount(
                    userId = mUser.userId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //获取账户余额
    fun requestAccountBalance(
            context: Context, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestAccountBalance(
                    userId = mUser.userId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //提现
    fun submitAccounCash(
            context: Context, sum: String, bankid: String, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().submitAccountCash(
                    userId = mUser.userId,
                    sum = sum,
                    bankId = bankid
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //提现记录
    fun submitAccounCashList(
            context: Context, page: Int, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requesAccountCashList(
                    userId = mUser.userId,
                    page = page.toString(),
                    size = DataMessageVo.CINT_PANGE_SIZE.toString()
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }


    //请求个人信息
    fun requestInfomCheck(
            context: Context, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestInfomCheck(
                    userId = mUser.userId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //提现记录
    fun submitPointAdd(
            context: Context, latitude: String, longitude: String,
            loc_time: String, ciird_type_input: String, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().submitPointAdd(
                    userId = mUser.userId,
                    latitude = latitude,
                    longitude = longitude,
                    loc_time = loc_time,
                    coord_type_input = ciird_type_input
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //上传身份证正面
    fun submitIdCardFace(
            context: Context, file: File, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            builder.addFormDataPart("userId", mUser.userId)
            val str = "userId=${mUser.userId}&token=${mUser.token}"
            val handParams = MD5.getMD5String(str).toUpperCase()
            builder.addFormDataPart("sign", handParams)
            val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
            builder.addFormDataPart("file", file.name, imageBody)//imgfile 后台接收图片流的参数名
            val parts = builder.build().parts()
            RetrofitFactory.createMainRetrofit().submitIdCardZ(parts)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })

        } else {
            m.onComplise()
        }
    }

    //上传身份证反面
    fun submitIdCardBack(
            context: Context, file: File, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            builder.addFormDataPart("userId", mUser.userId)
            val str = "userId=${mUser.userId}&token=${mUser.token}"
            val handParams = MD5.getMD5String(str).toUpperCase()
            builder.addFormDataPart("sign", handParams)
            val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
            builder.addFormDataPart("file", file.name, imageBody)//imgfile 后台接收图片流的参数名
            val parts = builder.build().parts()
            RetrofitFactory.createMainRetrofit().submitIdCardF(parts)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })

        } else {
            m.onComplise()
        }
    }

    //识别驾驶证
    fun submitDriversLicense(
            context: Context, file: File, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            builder.addFormDataPart("userId", mUser.userId)
            val str = "userId=${mUser.userId}&token=${mUser.token}"
            val handParams = MD5.getMD5String(str).toUpperCase()
            builder.addFormDataPart("sign", handParams)
            val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
            builder.addFormDataPart("file", file.name, imageBody)//imgfile 后台接收图片流的参数名
            val parts = builder.build().parts()
            RetrofitFactory.createMainRetrofit().submitDriversLicense(parts)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })

        } else {
            m.onComplise()
        }
    }

    //识别驾驶证
    fun submitDriveringLicense(
            context: Context, file: File, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            builder.addFormDataPart("userId", mUser.userId)
            val str = "userId=${mUser.userId}&token=${mUser.token}"
            val handParams = MD5.getMD5String(str).toUpperCase()
            builder.addFormDataPart("sign", handParams)
            val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
            builder.addFormDataPart("file", file.name, imageBody)//imgfile 后台接收图片流的参数名
            val parts = builder.build().parts()
            RetrofitFactory.createMainRetrofit().submitDriveringLicense(parts)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })

        } else {
            m.onComplise()
        }
    }


    //修改头像
    fun submitUpdataHear(
            context: Context, file: File, m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            builder.addFormDataPart("userId", mUser.userId)
            val str = "userId=${mUser.userId}&token=${mUser.token}"
            val handParams = MD5.getMD5String(str).toUpperCase()
            builder.addFormDataPart("sign", handParams)
            val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
            builder.addFormDataPart("file", file.name, imageBody)//imgfile 后台接收图片流的参数名
            val parts = builder.build().parts()
            RetrofitFactory.createMainRetrofit().submitUploadHear(parts)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })

        } else {
            m.onComplise()
        }
    }

    /**
     * 请求个人信息
     * @param fromType    坐标类型
     * @param lat 纬度
     * @param lng  经度
     * @param need3  是否需要当天每3或6小时一次的天气预报列表
     * @param  needAlarm 是否需要天气预警
     * @param  needHourData  是否需要每小时数据的累积数组
     * @param  needIndex  是否需要返回指数数据
     * @param  neewMoreDay  是否需要返回7天数据中的后4天
     * @param  context
     */
    fun requestWeather(
            context: Context, fromType: String, lat: String, lng: String,
            need3: Boolean, needAlarm: Boolean, needHourData: Boolean
            , needIndex: Boolean, neewMoreDay: Boolean,
            m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestWeather(
                    userId = mUser.userId,
                    formType = fromType,
                    lat = lat,
                    lng = lng,
                    need3HourForcast = if (need3) "1" else "0",
                    needAlarm = if (needAlarm) "1" else "0",
                    needHourData = if (needHourData) "1" else "0",
                    needIndex = if (needIndex) "1" else "0",
                    needMoreDay = if (neewMoreDay) "1" else "0"
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        m.Success(it.data)
                    }, Consumer {
                        m.onError(it)
                    }, Action {
                        m.onComplise()
                    })
        } else {
            m.onComplise()
        }
    }

    //首頁內容
    fun requestMainContent(
        context: Context,m: StringResultInterface
    ) {
        if (GmContentVo.getUserInfom() == null || StringUtil.isEmpty(GmContentVo.getUserInfom()!!.userId)) {
            BaseApplication.toInstance().startActivty(context)
            return
        }
        var mUser: UserDbVo = GmContentVo.getUserInfom()!!
        if (RetrofitFactory.judgmentNetWork(context)) {
            RetrofitFactory.createMainRetrofit().requestMainContent(
                userId = mUser.userId
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    m.Success(it.data)
                }, Consumer {
                    m.onError(it)
                }, Action {
                    m.onComplise()
                })
        } else {
            m.onComplise()
        }
    }

    private fun toSortData(strUrl: String): String {
        val sortMap: MutableMap<String, String> = mutableMapOf()

        val allPair = strUrl.split("&")

        for (pair in allPair) {
//            if (pair.contains("name=")) {
//                sortMap["name"] = pair.replace("name=", "")
//            } else {
            val nameValues = pair.split(Regex("="), 2)
//                if (nameValues.size == 3) {
//                    if (nameValues[2] == "\n") {
//                        sortMap[nameValues[0]] = (nameValues[1] + "=")
//                    }
//                }else if(nameValues.size == 4){
//                    if(nameValues[2] == "\n"&&nameValues[3] == "\n"){
//
//                    }
//                } else
            if (nameValues.size == 2) {
                sortMap[nameValues[0]] = nameValues[1]
            } else {
                continue
            }
//            }
        }

        var sortString = ""
        for (str in sortMap.toSortedMap().keys) {
            var valueName = sortMap[str]

            sortString += "&$str=$valueName"
        }

        return sortString.substring(1)
    }
}