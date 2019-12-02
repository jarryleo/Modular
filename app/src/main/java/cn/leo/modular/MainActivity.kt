package cn.leo.modular

import android.os.Bundle
import cn.leo.base.arouter.pages.PagesHome
import cn.leo.base.base.BaseModelActivity
import cn.leo.base.model.WechatModel
import cn.leo.base.net.Apis
import cn.leo.base.support.actionBar
import cn.leo.base.support.setMenu
import cn.leo.frame.log.Logger
import cn.leo.frame.support.*
import cn.leo.frame.utils.toast
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = PagesHome.homeMainActivity)
class MainActivity : BaseModelActivity<WechatModel>() {

    private var mTestText by int { tvTest }
    private var mEditText by text { etTest }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar("测试哈哈哈哈哈测试哈哈哈哈哈", "菜单") {
            toast("点击右侧菜单")
        }


        tvTest.setOnClickListener {
            test()
            setMenu("hahahahah")
        }
    }

    override fun onInitObserve() {

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

        model.apis(123, true).getWechatUserInfo("123", "456")

        count(onCount = {
            Logger.d(it.toString())
            mTestText = it
        }, onComplete = {
            toast("倒计时结束")
        })

    }

}
