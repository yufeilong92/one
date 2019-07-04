package com.zzzh.akhalteke.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.facebook.drawee.view.SimpleDraweeView
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.CarLengthVo
import com.zzzh.akhalteke.bean.vo.CarTypeVo
import com.zzzh.akhalteke.bean.vo.RlvSelectVo
import com.zzzh.akhalteke.retrofit.RetrofitFactory
import com.zzzh.akhalteke.view.customview.CenterAlignImageSpan
import java.io.*
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern


object Utils {
    fun getBrand(): String {
        val brand = Build.BRAND;
        if (brand == null || brand.equals(""))
            return "";
        return brand
    }

    fun getModel(): String {
        val model = Build.MODEL;
        if (model == null || model.equals(""))
            return "";
        return model
    }

    //更具地址转换获取String
    fun getBase64String(str: String): String {
        if (StringUtil.isEmpty(str)) return "";
        val fis = FileInputStream(str)
        val bitmap = BitmapFactory.decodeStream(fis)
        var ba64 = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ba64)
        var b = ba64.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    //更具地址转换获取String
    fun BitmapToString(bitmap: Bitmap): String {
        if (bitmap == null) return "";
        var ba64 = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ba64)
        var b = ba64.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun getArrayString(context: Context, id: Int): Array<String> {
        var lists = context.resources.getStringArray(id)
        return lists
    }

    fun getMutablistString(context: Context, id: Int): MutableList<String> {
        val lists = context.resources.getStringArray(id)
        var mutableList = arrayListOf<String>()
        for (item in lists) {
            mutableList.add(item)
        }
        return mutableList
    }


    //是否触目view 控件
    fun isInEditext(v: View, event: MotionEvent): Boolean {
        val frame = Rect()
        v.getHitRect(frame)
        val eventX = event.x
        val eventY = event.y
        return frame.contains(eventX.toInt(), eventY.toInt())
    }

    /**
     * 获取view 文字
     */
    fun getObjToStr(v: View): String {

        return when (v) {
            is TextView -> (v as TextView).text.toString().trim()
            is Button -> (v as Button).text.toString().trim()
            is EditText -> (v as EditText).text.toString().trim()
            else -> ""
        }
    }

    private val MIN_CLICK_DELAY_TIME = 2000
    private var lastClickTime: Long = 0

    /***
     * 处理多次点击问题
     * @return
     */
    fun handleOnDoubleClick(): Boolean {
        val l = System.currentTimeMillis()
        if (l - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = l
            return false
        }
        return true
    }


    fun hideInput(mcontext: Context, view: View) {
        val inputMethodManager: InputMethodManager =
                mcontext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 文件转Base64.
     * @param filePath
     * @return
     */
    fun file2Base64(filePath: String): String {
        var objFileIS: FileInputStream? = null
        try {
            objFileIS = FileInputStream(filePath)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val objByteArrayOS = ByteArrayOutputStream()
        val byteBufferString = ByteArray(1024)
        try {
            var readNum: Int
            while ((objFileIS!!.read(byteBufferString)).also { readNum = it } != -1) {
                objByteArrayOS.write(byteBufferString, 0, readNum)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return android.util.Base64.encodeToString(objByteArrayOS.toByteArray(), android.util.Base64.DEFAULT)
    }

    /**
     * 判断是否为手机号码
     *
     * @return
     */
    fun isPhoneNum(phone: String): Boolean {
        val pattern = Pattern.compile("1[0-9]{10}")
        val matcher = pattern.matcher(phone)
        return matcher.matches()
    }

    fun isInputNumber(str: String): Boolean {
        var pattern = Pattern.compile("^\\d*\\.{0,1}\\d{0,1}\$")
        val matcher = pattern.matcher(str)
        return matcher.matches()
    }

    fun replaceDou(str: String): String {
        if (StringUtil.isEmpty(str)) return "0"
        return str.replace(",", "")
    }

    /**
     * 隐藏键盘
     *
     * @param activity activity
     */
    fun hideInputMethod(activity: Activity) {
        hideInputMethod(activity, activity.currentFocus)
    }

    /**
     * 隐藏键盘
     *
     * @param context context
     * @param view    The currently focused view
     */
    fun hideInputMethod(context: Context?, view: View?) {
        if (context == null || view == null) {
            return
        }

        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 验证身份证号是否符合规则
     * @param text 身份证号
     * @return
     */
    fun personIdValidation(text: String): Boolean {
        val regx = "[0-9]{17}x"
        val reg1 = "[0-9]{15}"
        val regex = "[0-9]{18}"
        return text.matches(regx.toRegex()) || text.matches(reg1.toRegex()) || text.matches(regex.toRegex())
    }

    fun getFromAssets(m: Context, str: String): String {
        try {
            var resul: StringBuffer = StringBuffer()
            val open = m.resources.assets.open(str)
            val inputRead: InputStreamReader = InputStreamReader(open)
            val bufReader = BufferedReader(inputRead)
            var line: String = ""
            while ((bufReader.readLine().also { line = it }) != null) {
                resul.append(line)
            }
            open.close()
            inputRead.close()
            return resul.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun getFormAssets(context: Context, fileName: String): String? {
        val stringBuilder = StringBuilder()
        BufferedReader(InputStreamReader(context.assets.open(fileName), "UTF-8")).run {
            var line: String? = ""
            do {
                line = readLine()
                if (line != null) {
                    stringBuilder.append(line)
                } else {
                    break
                    close()
                }
            } while (true)
            return stringBuilder.toString()
        }
        return null
    }

    fun getRlvSelctVoUserCar(data: MutableList<String>): MutableList<RlvSelectVo> {
        var mSelectVo = arrayListOf<RlvSelectVo>()
        for (id in 0 until data.size) {
            val vo = RlvSelectVo()
            vo.select = false
            vo.id = (id + 1).toString()
            vo.name = data[id]

            mSelectVo.add(vo)
        }
        return mSelectVo
    }

    fun getRlvSelctVoTime(data: MutableList<String>): MutableList<RlvSelectVo> {
        var mSelectVo = arrayListOf<RlvSelectVo>()
        for (id in 0 until data.size) {
            val vo = RlvSelectVo()
            vo.select = false
            vo.id = (id + 1).toString()
            vo.name = data[id]
            mSelectVo.add(vo)
        }
        return mSelectVo
    }

    fun getRlvSelctVoCarLength(data: MutableList<CarLengthVo>): MutableList<RlvSelectVo> {
        var mSelectVo = arrayListOf<RlvSelectVo>()
        for (id in 0 until data.size) {
            val vo = RlvSelectVo()
            vo.select = false
            vo.id = data[id].id
            vo.name = data[id].length
            mSelectVo.add(vo)
        }
        return mSelectVo
    }

    fun getRlvSelctVoCarType(data: MutableList<CarTypeVo>): MutableList<RlvSelectVo> {
        var mSelectVo = arrayListOf<RlvSelectVo>()
        for (id in 0 until data.size) {
            val vo = RlvSelectVo()
            vo.select = false
            vo.id = data[id].id
            vo.name = data[id].name
            mSelectVo.add(vo)
        }
        return mSelectVo
    }

    fun getRlvSelctVo(size: Int): MutableList<RlvSelectVo> {
        var mSelectVo = arrayListOf<RlvSelectVo>()
        for (id in 0 until size) {
            val vo = RlvSelectVo()
            vo.select = false
            mSelectVo.add(vo)
        }
        return mSelectVo
    }

    fun getTextString(m: Context, id: Int): String {
        return m.resources.getString(id)
    }

    //获取用户是否选择
    fun getIsSelect(select: MutableList<RlvSelectVo>): Boolean {
        if (select == null || select.isEmpty()) return false
        for (item in select) {
            if (item.select) {
                return true
            }
        }
        return false
    }

    //清空用户选择
    fun clearSelect(select: MutableList<RlvSelectVo>) {
        if (select == null || select.isEmpty()) return
        for (item in select) {
            item.select = false
            item.name = ""
            item.id = ""
        }
    }

    //转换成字符串
    fun getUserSelectToStr(select: MutableList<RlvSelectVo>): String {
        if (select == null || select.isEmpty()) return ""
        var stringbuffer: StringBuffer = StringBuffer();
        for (id in 0 until select.size) {
            if (select[id].select) {
                stringbuffer.append(select[id].id)
                stringbuffer.append(",")
            }
        }
        if (stringbuffer == null || StringUtil.isEmpty(stringbuffer.toString())) return ""
        val string = stringbuffer.toString()
        val last = string.subSequence(0, string.length - 1)
        return last.toString()
    }

    //转换成字符串
    fun getUserSelectToList(m: String): List<String> {
        val mutableList = mutableListOf<String>()
        if (StringUtil.isEmpty(m)) return mutableList
        return m.split(",")
    }

    //转换成字符串"/"
    fun getUserSelectToStrOne(select: MutableList<RlvSelectVo>): String {
        if (select == null || select.isEmpty()) return ""
        var stringbuffer: StringBuffer = StringBuffer();
        for (id in 0 until select.size) {
            if (select[id].select) {
                stringbuffer.append(select[id].name)
                stringbuffer.append("/")
            }
        }
        if (stringbuffer == null || StringUtil.isEmpty(stringbuffer.toString())) return ""
        val string = stringbuffer.toString()
        val last = string.subSequence(0, string.length - 1)
        return last.toString()
    }

    fun playPhone(m: Context, phone: String) {
        if (StringUtil.isEmpty(phone)) {
            Toast.makeText(m, "电话号码为空", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phone");
        intent.data = data;
        m.startActivity(intent);
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    fun getVersionName(context: Context): String {
        try {
            return context.packageManager.getPackageInfo(
                    context.packageName, 0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 获取当前本地apk的版本
     */
    fun getAppVersionCode(mContext: Context): Int {
        var version = 0
        try {
            version = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionCode
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return version
    }


    /**
     * EditText获取焦点并显示软键盘
     */
    fun showSoftInputFromWindow(activity: Activity, editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val inputManager = activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(editText, 0)
            }
        }, 300)
    }

    /***
     * @param content 网络图片
     */
    fun showNetImager(iv: SimpleDraweeView, content: String?) {
        if (StringUtil.isEmpty(content)) return
        val mPath = StringBuffer()
        mPath.append(RetrofitFactory.BASE_URL)
        mPath.append(content)
        ImageLoadingUtils.loadNetImage(iv, mPath.toString())
    }

    /**
     * 显示本地图片
     * @param content 本地图片
     */
    fun showLoadmager(iv: SimpleDraweeView, content: String?) {
        if (StringUtil.isEmpty(content)) return
        ImageLoadingUtils.loadLocalImage(iv, content!!)
    }
    fun addMoneyComma(str: String?): String {
        if (StringUtil.isEmpty(str)) return "0"
        return str!!;
        val decimalFormat = DecimalFormat("###,###.00")
        val format = decimalFormat.format(str!!.toDouble() / 100)
        return format
    }

    fun showAddress(context: Context,start: String?, end: String?) :SpannableStringBuilder{
        val spang = SpannableStringBuilder()
        spang.append(start)
        spang.append("图")
        spang.append(end)
        var startLength=0
        if (!StringUtil.isEmpty(start)){
            startLength = start!!.length
        }
        val drawable = context.resources.getDrawable(R.mipmap.jiantou_copy)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        val centerImageSpank = CenterAlignImageSpan(drawable)
        spang.setSpan(centerImageSpank, startLength, startLength + 1, ImageSpan.ALIGN_BASELINE)
        return spang;
    }
    fun getRandom4():Int{
      return ((Math.random()*9+1)*1000000).toInt()
    }


}