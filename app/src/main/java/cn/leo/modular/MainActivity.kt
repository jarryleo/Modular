package cn.leo.modular

import android.os.Bundle
import cn.leo.base.arouter.pages.PagesHome
import cn.leo.base.base.BaseModelActivity
import cn.leo.base.model.WechatModel
import cn.leo.base.net.Apis
import cn.leo.frame.log.Logger
import cn.leo.frame.support.getString
import cn.leo.frame.utils.toast
import cn.leo.frame.support.text
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = PagesHome.homeMainActivity)
class MainActivity : BaseModelActivity<WechatModel>() {

    private var mTestText by text { tvTest }
    private var mEditText by text { etTest }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTest.setOnClickListener {
            test()
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

        model.apis(123).getWechatUserInfo("123", "456")

        mTestText = R.string.app_name.getString()
    }

}
