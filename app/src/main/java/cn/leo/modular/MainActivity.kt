package cn.leo.modular

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.leo.base.model.WechatModel
import cn.leo.base.net.Apis
import cn.leo.base.utils.toast
import cn.leo.frame.log.Logger
import cn.leo.frame.network.ModelCreator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val model by ModelCreator(WechatModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.observe(Apis::getWechatUserInfo) {
            success {
                toast("请求成功：$obj  ")
            }
            failed {
                toast("请求失败：${it.code} + ${it.msg}  " + obj)
            }
        }


        model.observe(model::test) {
            get {
                Logger.e("result = $it")
            }
        }


        tvTest.setOnClickListener {
            test()
        }
    }


    private fun test() {

        model.test(1)

        model.apis(123).getWechatUserInfo("123", "456")

    }
}
