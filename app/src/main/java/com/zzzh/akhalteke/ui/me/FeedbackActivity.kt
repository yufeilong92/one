package com.zzzh.akhalteke.ui.me

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.mvp.contract.FeedbookContract
import com.zzzh.akhalteke.mvp.model.FeedbookModel
import com.zzzh.akhalteke.mvp.presenter.FeedbookPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.StringUtil
import kotlinx.android.synthetic.main.activity_feedback.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/3/16 8:53
 * @Purpose :反馈界面
 */
class FeedbackActivity : BaseActivity(), FeedbookContract.View {

    private var mPresenter: FeedbookPresenter? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_feedback)
        initEvent()
        initRequest()
    }

    fun initRequest() {
        mPresenter = FeedbookPresenter()
        mPresenter!!.initMvp(this, model = FeedbookModel())

    }

    fun initEvent() {
        et_feedback_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val length = et_feedback_input.text.toString().trim().length
                tv_input_number.text = length.toString()

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        btn_submit.setOnClickListener {
            val input = et_feedback_input.text.toString()
            input.trim()
            if (StringUtil.isEmpty(input)) {
                showToast(getString(R.string.feebbook))
                return@setOnClickListener
            }
            showProgress()
            mPresenter!!.submitFeedbook(mContext, input)
        }


    }

    override fun Success(t: Any?) {
        finishBase()
        showToast(getString(R.string.submit_successOne))
    }

    override fun Error(ex: Throwable) {
        this.onError(ex)
    }

    override fun Complise() {
        this.onComplise()
    }

}
