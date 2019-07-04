package com.zzzh.akhalteke.ui.home.find

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.fragment.home.HomeFindGoodFragment
import com.zzzh.akhalteke.ui.BaseActivity

class FindGoodActivity : BaseActivity() {
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_find_good)
        val fragment = HomeFindGoodFragment.newInstance("", "")
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.fl_findgood_content, fragment)
        beginTransaction.commit()
    }


}
