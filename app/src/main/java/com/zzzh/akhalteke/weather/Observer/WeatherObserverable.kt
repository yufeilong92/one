package com.zzzh.akhalteke.weather.Observer

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.view
 * @Email : yufeilong92@163.com
 * @Time :2019/5/10 11:40
 * @Purpose :天气观察这模式
 */
interface WeatherObserverable {
    fun registerOberver(o: WeatherObserver?)
    fun removerOberver(o: WeatherObserver?)
    fun removerAll()
    /**
     * @param  lat 纬度
     * @param  lng 经度
     */
    fun notifyObserver()

}