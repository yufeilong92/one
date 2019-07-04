package com.zzzh.akhalteke.ui.me

import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.ui.BaseActivity

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 16:54
 * @Purpose :银行卡验证提交
 */
class BankAddCodeActivity : BaseActivity() {
    companion object {
        var NAME: String = "Name"
        var PHONE: String = "phone"
        var NUMBER: String = "number"
        var CARNUMER: String = "Carnumber"
    }

    private var mName: String? = null
    private var mPhone: String? = null
    private var mNumber: String? = null
    private var mCarNumber: String? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_bank_add_code)
        if (intent != null) {
            mName = intent.getStringExtra(NAME)
            mPhone = intent.getStringExtra(PHONE)
            mNumber = intent.getStringExtra(NUMBER)
            mCarNumber = intent.getStringExtra(CARNUMER)
        }

    }

}
