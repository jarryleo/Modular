package cn.leo.modular

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.leo.base.bean.WechatUserBean
import cn.leo.base.model.WechatModel
import cn.leo.base.utils.toast
import cn.leo.frame.network.ModelCreator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val model by ModelCreator(WechatModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.request.observe<WechatUserBean> {
            get(
                failed = {
                    toast("请求失败：${it.code}  ")
                },
                success = {
                    toast("请求成功：$obj  ")
                }
            )

        }

        /*val liveData =
            model.request.getLiveData<WechatUserBean>().map {
                PositionBean<WechatUserBean>(it).apply {
                    position = 111
                }
            }

        liveData.observe {
            get {
                toast("请求成功：" + it.position)
            }
        }*/

        tvTest.setOnClickListener {


            model.request.apis(123) {
                getWechatUserInfo("", "")
            }


        }
    }
}
