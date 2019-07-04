package com.zzzh.akhalteke.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.zzzh.akhalteke.R

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/3/25 10:20
 * @Purpose :银行工具类
 */
class BankLogUtil(var m: Context) {
    companion object {//被companion object包裹的语句都是private的
        private var singletonInstance: BankLogUtil? = null
        @Synchronized
        fun getInstance(m:Context): BankLogUtil? {
            if (singletonInstance == null) {
                singletonInstance = BankLogUtil(m)
            }
            return singletonInstance
        }
    }
    var smallLogo: Int? = null
    var BgLogo: Drawable? = null
    var title:String?=""
    fun  init(m: Context){
        title=""
    }


    fun selecBankLogo( status: BankStatus) {
          when(status){
              BankStatus.JIANSHE->{
                  BgLogo = m.resources.getDrawable(R.mipmap.jiansheyinhang)
                  smallLogo=R.mipmap.jianshetubiao
                  title="建设银行"
              }
              BankStatus.GONGSHANG->{
                  BgLogo = m.resources.getDrawable(R.mipmap.gongshangyinhang)
                  smallLogo=R.mipmap.gongshangtubiao
                  title = "工商银行"
              }
              BankStatus.YOUZHENG->{
                  BgLogo = m.resources.getDrawable(R.mipmap.youzhengyinhan)
                  smallLogo=R.mipmap.youzhengtubiao
                  title="邮政银行"
              }
              BankStatus.GUANGDA->{
                  BgLogo = m.resources.getDrawable(R.mipmap.guangdayinhang)
                  smallLogo=R.mipmap.guangdatubiao
                  title="光大银行"
              }
              BankStatus.XINGYE->{
                  BgLogo = m.resources.getDrawable(R.mipmap.xingyeyinhang)
                  smallLogo=R.mipmap.xingyetubiao
                  title="兴业银行"
              }
              BankStatus.ZHOGNGUO->{
                  BgLogo = m.resources.getDrawable(R.mipmap.zhongguoyinhang)
                  smallLogo=R.mipmap.zhongguotubiao
                  title="中国银行"
              }
              BankStatus.ZHAOSHANG->{
                  BgLogo = m.resources.getDrawable(R.mipmap.zhaoshangyinhang)
                  smallLogo=R.mipmap.zhaoshangtubiao
                  title="招商银行"
              }
              BankStatus.NOGNYE->{
                  BgLogo = m.resources.getDrawable(R.mipmap.nongyeyinhang)
                  smallLogo=R.mipmap.nongyeyinhangtuboao
                  title="农业银行"
              }
              BankStatus.GUANGFA->{
                  BgLogo = m.resources.getDrawable(R.mipmap.guangfayinhang)
                  smallLogo=R.mipmap.guangfayinhangtubio
                  title="广发银行"
              }
              BankStatus.PINGAN->{
                  BgLogo = m.resources.getDrawable(R.mipmap.pinganyinhang)
                  smallLogo=R.mipmap.pinganyinhangtubiao
                  title="平安银行"
              }
              BankStatus.JIAOTONG->{
                  BgLogo = m.resources.getDrawable(R.mipmap.jiaotongyinhang)
                  smallLogo=R.mipmap.jiaotongyinhangtubio
                  title="交通银行"
              }
              BankStatus.ZHONGXIN->{
                  BgLogo = m.resources.getDrawable(R.mipmap.zhongxinyinhang)
                  smallLogo=R.mipmap.zhongxinyinhangtubio
                  title="中信银行"
              }
              BankStatus.HUAXIA->{
                  BgLogo = m.resources.getDrawable(R.mipmap.huaxiayinhang)
                  smallLogo=R.mipmap.huaxiayinhangtubio
                  title="中信银行"
              }
              BankStatus.MINSHENG->{
                  BgLogo = m.resources.getDrawable(R.mipmap.minshengyinhang)
                  smallLogo=R.mipmap.mingshengyinhangtubiao
                  title="民生银行"
              }
          }
    }
}
enum class BankStatus(s: String) {
    JIANSHE("0105"),////建设
    GONGSHANG("0102"),////工商银行
    YOUZHENG("0100"),////邮政银行
    GUANGDA("0303"),///光大银行
    XINGYE("0309"),//兴业银行
    ZHOGNGUO("0104"),//中国银行
    ZHAOSHANG("0308"),//招商银行
    NOGNYE("0103"),//农业银行
    GUANGFA("0306"),//,广发银行
    PINGAN("0307"),//平安银行
    JIAOTONG("0301"),//交通银行
    ZHONGXIN("0302"),//中信银行
    HUAXIA("0304"),//华夏银行
    MINSHENG("0305")//民生银行

}