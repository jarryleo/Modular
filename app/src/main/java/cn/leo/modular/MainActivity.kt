package cn.leo.modular

import android.os.Bundle
import cn.leo.base.BaseModelActivity
import cn.leo.base.model.WechatModel
import cn.leo.base.net.Apis
import cn.leo.base.utils.toast
import cn.leo.frame.log.Logger
import cn.leo.frame.view_support.text
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseModelActivity<WechatModel>() {

    private var mTestText by text { tvTest }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTest.setOnClickListener {
            test()
        }
    }


    override fun initObserve() {

        model.observe(Apis::getWechatUserInfo) {
            success {
                toast("请求成功：$obj  ")
            }
            failed {
                toast("请求失败：${it.code} + ${it.msg}  " + obj)
            }
        }


        model.observe(model::test) {
            success {
                Logger.e("result = $it")
            }

            failed {
                toast(it.msg ?: "")
            }
        }

    }

    private fun test() {

        model.test(1)

        //model.apis(123).getWechatUserInfo("123", "456")


        mTestText = etTest.text.toString()
    }

}
