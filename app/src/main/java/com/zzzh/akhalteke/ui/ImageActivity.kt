package com.zzzh.akhalteke.ui

import android.os.Bundle
import com.zzzh.akhalteke.R
import android.support.v4.app.Fragment
import com.zzzh.akhalteke.adapter.ViewPagerAdaper
import com.zzzh.akhalteke.bean.vo.SelectPath
import com.zzzh.akhalteke.fragment.guider.ImagerFragment
import kotlinx.android.synthetic.main.activity_image.*


/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui
 * @Package com.zzzh.akhalteke.ui
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 14:25
 * @Purpose :图片展示
 */
class ImageActivity : BaseActivity() {
    companion object {
        var TYPE: String = "type"
        var BENDI_TYPE: String = "bendi_type"
        var NET_TYPE: String = "net_type"
        var IMAGEPATH: String = "imagepath"
        var INDEX: String = "String"
    }

    var mIndex: Int = 0
    var mPathList: ArrayList<SelectPath>? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_image)
        if (intent != null) {
           mPathList = intent.getSerializableExtra(IMAGEPATH) as ArrayList<SelectPath>
            mIndex = intent.getIntExtra(INDEX, 0)
        }

        initView()
    }

    fun initView() {
        val mList = mutableListOf<Fragment>()

        for (item in mPathList!!) {
            mList.add(ImagerFragment.newInstance(item.type!!, item.path!!))
        }
        val adapter = ViewPagerAdaper(supportFragmentManager, mList, mutableListOf())
        viewpager_imager.adapter = adapter
        viewpager_imager.currentItem = mIndex
    }
}
