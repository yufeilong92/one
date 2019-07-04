package com.zzzh.akhalteke.ui.home.find

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.zzzh.akhalteke.MainActivity
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.GmCityAdapter
import com.zzzh.akhalteke.adapter.GmProvinceAdapter
import com.zzzh.akhalteke.bean.vo.CityVo
import com.zzzh.akhalteke.fragment.home.HomeFindGoodFragment
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_select_city.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.home
 * @Package com.zzzh.akhalteke.ui.home
 * @Email : yufeilong92@163.com
 * @Time :2019/3/13 14:11
 * @Purpose : 地址选择界面
 */
class SelectCityActivity : BaseActivity() {

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_select_city)
        initData()
    }

    fun initData() {
        val assets = Utils.getFormAssets(mContext, "city.json")
        val gosn = Gson().fromJson(assets, CityVo::class.java)
        //省
        setMangager(rlv_province)
        val provinceLists = gosn.data
        val mSelectProvince = Utils.getRlvSelctVo(provinceLists!!.size)
        var provinceAdapter = GmProvinceAdapter(mContext, provinceLists, mSelectProvince)
        rlv_province.adapter = provinceAdapter
        provinceAdapter.setOnItemClickListener { adapter, view, position ->//省带点击
            //市
            for (id in 0 until provinceLists!!.size) {
                mSelectProvince[id].select = false
                mSelectProvince[position].id = ""
                mSelectProvince[position].name = ""
            }
            val mProvince = provinceLists[position]
            mSelectProvince[position].select = true
            mSelectProvince[position].id = mProvince.id
            mSelectProvince[position].name = mProvince.area_name
            provinceAdapter.Refresh(mSelectProvince)
            val cityLists = mProvince.citys
            if (cityLists == null || cityLists.isEmpty()) {
                var intent = Intent(this@SelectCityActivity, MainActivity::class.java)
                intent.putExtra(HomeFindGoodFragment.KEY_ID, mProvince.id)
                intent.putExtra(HomeFindGoodFragment.VALUES_NAME, mProvince.area_name)
                setResult(HomeFindGoodFragment.REQUESTCODE, intent)
                finishBase()
                return@setOnItemClickListener
            }
            rlv_city.visibility = View.VISIBLE
            rlv_county.visibility = View.GONE
            view_line.visibility=View.GONE
            setMangager(rlv_city)
            val mSelectCity = Utils.getRlvSelctVo(cityLists.size)
            var cityAdapter = GmCityAdapter(mContext, cityLists, mSelectCity)
            rlv_city.adapter = cityAdapter
            cityAdapter.setOnItemClickListener { adapter, view, position ->
                //县
                for (id in 0 until cityLists.size) {
                    mSelectCity[id].select = false
                    mSelectCity[position].id = ""
                    mSelectCity[position].name = ""
                }
                val mCity = cityLists.get(position)
                mSelectCity[position].select = true
                mSelectCity[position].id = mCity.id
                mSelectCity[position].name = mCity.area_name
                cityAdapter.Refresh(mSelectCity)
                val countyList = mCity.zones
                if (countyList == null || countyList.isEmpty()) {
                    var intent = Intent(this@SelectCityActivity, MainActivity::class.java)
                    intent.putExtra(HomeFindGoodFragment.KEY_ID, mCity.id)
                    if (mCity.area_name.equals("全部")){
                        intent.putExtra(HomeFindGoodFragment.VALUES_NAME,mProvince.area_name)
                    }else{
                        intent.putExtra(HomeFindGoodFragment.VALUES_NAME, mCity.area_name)
                    }
                    setResult(HomeFindGoodFragment.REQUESTCODE, intent)
                    finishBase()
                    return@setOnItemClickListener
                }
                rlv_county.visibility = View.VISIBLE
                view_line.visibility=View.VISIBLE
                setMangager(rlv_county)
                val mSelectCounty = Utils.getRlvSelctVo(countyList.size)
                var countyAdapter = GmCityAdapter(mContext, countyList, mSelectCounty)
                rlv_county.adapter = countyAdapter
                countyAdapter.setOnItemClickListener { adapter, view, position ->
                    val mCounty = countyList[position]
                    mSelectCounty[position].select = true
                    mSelectCounty[position].id = mCounty.id
                    mSelectCounty[position].name = mCounty.area_name
                    countyAdapter.Refresh(mSelectCounty)
                    var intent = Intent(this@SelectCityActivity, MainActivity::class.java)
                    intent.putExtra(HomeFindGoodFragment.KEY_ID, mCounty.id)
                    if (mCounty.area_name.equals("全市")||mCounty.area_name.equals("全县")){
                        intent.putExtra(HomeFindGoodFragment.VALUES_NAME, mCity.area_name)
                    }else{
                        intent.putExtra(HomeFindGoodFragment.VALUES_NAME, mCounty.area_name)
                    }
                    setResult(HomeFindGoodFragment.REQUESTCODE, intent)
                    finishBase()
                }
            }
        }


    }

}
