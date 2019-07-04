package com.zzzh.akhalteke.weather.Observer

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.weather.Observer
 * @Email : yufeilong92@163.com
 * @Time :2019/5/10 11:43
 * @Purpose :被观察者
 */
class WeatherManger : WeatherObserverable {


    var mList: MutableList<WeatherObserver>? = null

    constructor() {
        mList = mutableListOf()
    }

    override fun registerOberver(o: WeatherObserver?) {
        if (o == null) return
        if (mList!!.contains(o)) return
        mList!!.add(o)
    }

    override fun removerOberver(o: WeatherObserver?) {
        if (o == null) return
        if (mList != null && !mList.isNullOrEmpty())
            mList!!.remove(o)
    }

    override fun removerAll() {
        if (mList != null) {
            mList!!.clear()
        }
    }

    override fun notifyObserver() {
        if (mList != null && !mList!!.isEmpty()) {
            for (item in mList!!) {
                item.upDataWeather()
            }
        }
    }

}