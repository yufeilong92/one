package com.zzzh.akhalteke.fragment.guider

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.ui.BaseFragment
import com.zzzh.akhalteke.ui.ImageActivity.Companion.BENDI_TYPE
import com.zzzh.akhalteke.ui.ImageActivity.Companion.NET_TYPE
import kotlinx.android.synthetic.main.fragment_imager.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.fragment.guider
 * @Package com.zzzh.akhalteke.fragment.guider
 * @Email : yufeilong92@163.com
 * @Time :2019/4/2 10:56
 * @Purpose :图片查看你
 */
class ImagerFragment : BaseFragment() {

    private var mType: String? = null
    private var mPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mType = it.getString(ARG_PARAM1)
            mPath = it.getString(ARG_PARAM2)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(type: String, path: String) =
                ImagerFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, type)
                        putString(ARG_PARAM2, path)
                    }
                }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_imager
    }

    override fun setCreatedContentView(view: View, savedInstanceState: Bundle?) {
        when (mType) {
            BENDI_TYPE -> {
                iv_phone_view.setPhotoUri(Uri.parse("file://$mPath"))
            }
            NET_TYPE -> {
                iv_phone_view.setPhotoUri(Uri.parse(mPath))
            }
        }

    }


}
