package cn.leo.modular

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.leo.base.bean.WechatUserBean
import cn.leo.base.model.WechatModel
import cn.leo.base.utils.toast
import cn.leo.frame.log.Logger
import cn.leo.frame.network.ModelCreator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val model by ModelCreator(WechatModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.request.observe<WechatUserBean>(this) {
            get(
                failed = {
                    toast("请求失败：${it.code} + ${it.msg}  ")
                },
                success = {
                    toast("请求成功：$obj  ")
                }
            )

        }


        model.observe(this, model::test) {
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
    }
}
