package cn.leo.base.presenter

import android.support.v7.app.AppCompatActivity
import cn.leo.base.model.WechatModel
import cn.leo.base.utils.toast
import cn.leo.frame.network.JLPresenter

/**
 * @author : ling luo
 * @date : 2019-07-04
 */
class WechatPresenter(view: AppCompatActivity) : JLPresenter<AppCompatActivity>(view) {
    fun test() {
        model<WechatModel>().test().observe(
            mView,
            error = {
                mView.toast(it?.displayMessage?:"error")
            },
            failed = {
                mView.toast(it?.nickname?:"failed")
            },
            success = {
                mView.toast(it?.nickname?:"success")
            }
        )
    }
}