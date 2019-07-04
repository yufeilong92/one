package com.zzzh.akhalteke.adapter.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.yanzhenjie.permission.Permission
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerClickListener
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.loader.ImageLoader
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.WebViewActivity
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.StringInfo
import com.zzzh.akhalteke.bean.vo.ActivityVo
import com.zzzh.akhalteke.bean.vo.BannerVo
import com.zzzh.akhalteke.bean.vo.BitmapVo
import com.zzzh.akhalteke.bean.vo.MainVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp.mContext
import com.zzzh.akhalteke.ui.home.EmptyActivity
import com.zzzh.akhalteke.ui.home.find.FindGoodActivity
import com.zzzh.akhalteke.utils.GlideUtil
import com.zzzh.akhalteke.utils.PermissionUtils
import com.zzzh.akhalteke.utils.StringUtil
import java.util.*

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/5/8 10:25
 * @Purpose :首页——首页
 */
class MainHomeAdaptervar(var mContext: Context, var infoList: MainVo) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val MAIN_BANNER: Int = 1001//轮播图
    private val MAIN_SERVICE: Int = 1002//特色服务
    private val MAIN_ACTIVITY: Int = 1003//购车优化
    private var MAIN_COMMON: Int = MAIN_BANNER

    var OnBannerClikcItem: OnBannerClickItemListener? = null

    interface OnBannerClickItemListener {
        fun onBannerClickItemLisener(postion: Int, item: BannerVo)
    }

    fun setOnBannerClikcListener(lisenter: OnBannerClickItemListener) {
        this.OnBannerClikcItem = lisenter
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val mInface = LayoutInflater.from(mContext)
        when (p1) {
            MAIN_BANNER -> {
                return BannerViewHolde(mInface!!.inflate(R.layout.item_main_home_banner, null))
            }
            MAIN_SERVICE -> {
                return ServiceViewHolde(mInface.inflate(R.layout.item_main_home_service, null))
            }
            MAIN_ACTIVITY -> {
                return ActivityViewHolde(mInface.inflate(R.layout.item_main_home_activity, null))
            }
            else -> return BannerViewHolde(mInface!!.inflate(R.layout.item_main_home_banner, null))
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is BannerViewHolde) {
            val bannerViewHolde: BannerViewHolde = p0 as BannerViewHolde
            initBanner(bannerViewHolde)
        } else if (p0 is ServiceViewHolde) {
            val serviceViewHodle = p0 as ServiceViewHolde
            initService(serviceViewHodle)
        } else if (p0 is ActivityViewHolde) {
            val activityViewHolde = p0 as ActivityViewHolde
            initActivity(activityViewHolde)
        }

    }

    var OnActivityClikcItem: OnActiviyClickItemListener? = null

    interface OnActiviyClickItemListener {
        fun onActivityClickItemLisener(postion: Int, item: ActivityVo)
    }

    fun setOnActivityClikcListener(lisenter: OnActiviyClickItemListener) {
        this.OnActivityClikcItem = lisenter
    }

    private fun initActivity(activityViewHolde: ActivityViewHolde) {
        val m = GridLayoutManager(mContext, 1)
        m.orientation = GridLayoutManager.VERTICAL
        val list = mutableListOf<String>()
        for (item in infoList.activity!!) {
            list.add(item.image)
        }
        val adapter = HomeImagerAdapter(mContext, list)
        adapter.setOnItemClickListener { adapter, view, position ->
            val activityVo = infoList.activity!![position]
            if (activityVo.type == "1" && !StringUtil.isEmpty(activityVo.url)) {
                if (OnActivityClikcItem != null) {
                    OnActivityClikcItem!!.onActivityClickItemLisener(position, activityVo)
                }
            } else {
                val intent = Intent(mContext, EmptyActivity::class.java)
                mContext.startActivity(intent)

            }
        }
        activityViewHolde.mrlv.layoutManager = m
        activityViewHolde.mrlv.adapter = adapter
        val vo = infoList.bitmap
        if (StringUtil.isEmpty(vo.image)) {
            activityViewHolde.mIvActivity.visibility = View.GONE
        } else {
            activityViewHolde.mIvActivity.visibility = View.VISIBLE
            GlideUtil.LoadImagerWithOutHttp(mContext, activityViewHolde.mIvActivity, vo.image)
        }
        activityViewHolde.mIvActivity.setOnClickListener {
            if (vo.type == "1" && !StringUtil.isEmpty(vo.url)) {
                if (OnUrlClikcItem != null) {
                    OnUrlClikcItem!!.onUrlClickItemLisener(1, vo.url)
                }
            } else {
                val intent = Intent(mContext, EmptyActivity::class.java)
                mContext.startActivity(intent)
            }
        }

    }

    var OnUrlClikcItem: OnUrlClickItemListener? = null

    interface OnUrlClickItemListener {
        fun onUrlClickItemLisener(postion: Int, str: String)
    }

    fun setOnUrlClikcListener(lisenter: OnUrlClickItemListener) {
        this.OnUrlClikcItem = lisenter
    }

    private fun initService(serviceViewHodle: ServiceViewHolde) {
        serviceViewHodle.mTvItemFindGood.setOnClickListener {
            val intent = Intent(mContext, FindGoodActivity::class.java)
            mContext.startActivity(intent)
        }
        serviceViewHodle.mTvItemFindcar.setOnClickListener {
            //购车优惠
            if (StringUtil.isEmpty(infoList.buyCar)) {
                val intent = Intent(mContext, EmptyActivity::class.java)
                mContext.startActivity(intent)
                return@setOnClickListener
            }
            startempty(5, infoList.buyCar)
        }
        serviceViewHodle.mTvItemFindetc.setOnClickListener {
            //etc
            if (StringUtil.isEmpty(infoList.etc)) {
                val intent = Intent(mContext, EmptyActivity::class.java)
                mContext.startActivity(intent)
                return@setOnClickListener
            }
            startempty(2, infoList.etc)
        }
        //油卡
        serviceViewHodle.mTvItemFindoil_card.setOnClickListener {
            if (StringUtil.isEmpty(infoList.oilCard)) {
                val intent = Intent(mContext, EmptyActivity::class.java)
                mContext.startActivity(intent)
                return@setOnClickListener
            }
            startempty(3, infoList.oilCard)
        }
        //查询违章
        serviceViewHodle.mTvItemFindquery.setOnClickListener {
            if (StringUtil.isEmpty(infoList.violation)) {
                val intent = Intent(mContext, EmptyActivity::class.java)
                mContext.startActivity(intent)
                return@setOnClickListener
            }
            startempty(4, infoList.violation)
        }
        //保险
        serviceViewHodle.mTvItemFindsafe.setOnClickListener {
            if (StringUtil.isEmpty(infoList.insurance)) {
                val intent = Intent(mContext, EmptyActivity::class.java)
                mContext.startActivity(intent)
                return@setOnClickListener
            }
            startempty(6, infoList.insurance)
        }


    }

    private fun startempty(postion: Int, url: String) {
        if (OnUrlClikcItem != null) {
            OnUrlClikcItem!!.onUrlClickItemLisener(postion, url)
        }
//        val intent = Intent(mContext, EmptyActivity::class.java)
//        mContext.startActivity(intent)
    }


    private fun initBanner(bannerViewHolde: BannerViewHolde) {

        val list = ArrayList<String>()
        for (item in infoList.banners!!) {
            list.add(DataMessageVo.HTTP_HEAR + item.image)
        }
        bannerViewHolde.mBanner.setBannerStyle(BannerConfig.NOT_INDICATOR)
        bannerViewHolde.mBanner.setIndicatorGravity(BannerConfig.CENTER)
        bannerViewHolde.mBanner.setDelayTime(2000)
        bannerViewHolde.mBanner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                GlideUtil.LoadImager(context, imageView, path.toString())

            }
        })
        bannerViewHolde.mBanner.setImages(list)
        bannerViewHolde.mBanner.start()
        bannerViewHolde.mBanner.setOnBannerListener {
            val banner = infoList.banners!![it]
            if (banner.type == "1" && !StringUtil.isEmpty(banner.url))
                OnBannerClikcItem?.onBannerClickItemLisener(it, banner)
            else{
                val intent = Intent(mContext, EmptyActivity::class.java)
                mContext.startActivity(intent)
            }

        }

    }

    inner class BannerViewHolde(var item: View) : RecyclerView.ViewHolder(item) {
        val mBanner = item.findViewById<Banner>(R.id.banner)


    }

    inner class ServiceViewHolde(var item: View) : RecyclerView.ViewHolder(item) {
        //找货
        val mTvItemFindGood = item.findViewById<TextView>(R.id.tv_item_home_findgood)
        //查询违章
        val mTvItemFindquery = item.findViewById<TextView>(R.id.tv_item_home_query)
        //etc
        val mTvItemFindetc = item.findViewById<TextView>(R.id.tv_item_home_etc)
        //购车优惠
        val mTvItemFindcar = item.findViewById<TextView>(R.id.tv_item_home_car)
        //油卡
        val mTvItemFindoil_card = item.findViewById<TextView>(R.id.tv_item_home_oil_card)
        //保险
        val mTvItemFindsafe = item.findViewById<TextView>(R.id.tv_item_home_safe)
    }

    inner class ActivityViewHolde(var item: View) : RecyclerView.ViewHolder(item) {
        val mrlv = item.findViewById<RecyclerView>(R.id.rlv_main_home_activity)
        val mIvActivity = item.findViewById<ImageView>(R.id.ic_item_main_home_activity)
    }

    override fun getItemViewType(position: Int): Int {

        when (position) {
            0 -> {
                MAIN_COMMON = MAIN_BANNER
            }
            1 -> {
                MAIN_COMMON = MAIN_SERVICE
            }
            2 -> {
                MAIN_COMMON = MAIN_ACTIVITY
            }
            else -> {
                MAIN_COMMON = MAIN_BANNER
            }
        }
        return MAIN_COMMON
    }

}