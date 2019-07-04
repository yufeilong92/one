package com.zzzh.akhalteke.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ViewPagerAdaper(
    fm: FragmentManager?,
    var mlist: MutableList<Fragment>,
    var titles: MutableList<String>
) :
    FragmentStatePagerAdapter(fm) {


    override fun getItem(p0: Int): Fragment {
        return mlist[p0]
    }

    override fun getCount(): Int {
        return mlist.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]

    }

    fun refreshTitle(title: MutableList<String>) {
        this.titles = title
        notifyDataSetChanged()
    }


}