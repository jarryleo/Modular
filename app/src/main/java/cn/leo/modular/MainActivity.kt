package cn.leo.modular

import android.graphics.Color
import cn.leo.base.arouter.pages.PagesHome
import cn.leo.base.base.BaseModelActivity
import cn.leo.base.model.WechatModel
import cn.leo.base.net.Apis
import cn.leo.base.support.doubleClickExit
import cn.leo.frame.ext.*
import cn.leo.frame.image.loadImage
import cn.leo.frame.log.logE
import cn.leo.frame.network.viewmodel.GlobalModelCreator
import cn.leo.frame.support.getColor
import cn.leo.frame.utils.WifiChecker
import cn.leo.frame.utils.toast
import cn.leo.modular.test.ArcNodeProgress
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = PagesHome.homeMainActivity)
class MainActivity : BaseModelActivity<WechatModel>() {

    private val globalModel by GlobalModelCreator(
        TestModel::class.java
    )

    private var mTestText by text { tvTest }
    private var mEditText by text { etTest }

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onInitView() {


        tvTest.setOnClickListener {
            test()
            //model.findUserById(1)

            //toast("wifi 开关状态 =" + WifiChecker.isWifiOpen(this))
            toast("wifi 连接状态 =" + WifiChecker.isWifiConnected(this))

            ivTest.loadImage(
                "https://pic2.zhimg.com/v2-7cb8b1ea5e11779e25b4b35d52b777f2_xll.jpg",
                //corners = 12.dp,
                circle = true,
                circleBorderWidth = 2.dp,
                circleBorderColor = R.color.colorPrimary.getColor(),
                defResId = R.drawable.ic_launcher_background,
                errResId = R.drawable.ic_launcher_background,
                onLoadFailed = { _, _ ->
                    logE("图片加载失败")
                }
            )
        }

        val nodes = arrayOf(
            ArcNodeProgress.Node(1500, "1"),
            ArcNodeProgress.Node(3000, "2"),
            ArcNodeProgress.Node(8000, "3"),
            ArcNodeProgress.Node(18000, "4"),
            ArcNodeProgress.Node(28000, "5"),
            ArcNodeProgress.Node(38000, "6"),
            ArcNodeProgress.Node(58000, "7"),
            ArcNodeProgress.Node(88000, "8"),
            ArcNodeProgress.Node(128000, "9"),
            ArcNodeProgress.Node(208000, "10")
        )
        node.setNodes(nodes)
        node.setProgress(36000)

        etTest.imeSearch {
            etTest
                .checkEmpty { toast("输入不能为空"); return@imeSearch }
                .checkNum(2, 6) { toast("字符不符合");return@imeSearch }
            toast(it)
            node.setProgress(it.toInt())
        }

        count(208000, start = 1500, end = 209000, interval = 5, step = 10) {
            if (it % 100 == 0) node.setProgress(it)
        }

    }

    override fun onInitObserve() {
        model.observe(this, model::findUserById) {
            success {
                mTestText = it.name
                logE("数据库查询成功")
            }
            failed {
                toast(it.msg ?: "数据库查询失败")
            }
        }

        model.observe(this, Apis::getWechatUserInfo) {
            loading = loadingLazyFun
            success {
                toast("请求成功：$obj  ")
            }
            failed {
                toast("请求失败：${it.code} + ${it.msg}  " + obj)
                logE("请求失败====1")
            }
        }


        model.observe(this, model::test) {
            success {
                logE("result = $it")
            }

            failed {
                logE(it.msg ?: "")
            }
        }

    }

    private fun test() {
        tvTest.text = "你好啊，哈哈哈，吼吼吼".highLight("哈哈哈", Color.RED)
        //model.test(1)
        //model.wechat()
        //model.apis(123).getWechatUserInfo("12311", "45611")
        //model.apis.getWechatUserInfo("123", "456")
    }

    override fun onBackPressed() {
        doubleClickExit { msg = "再按一次试试" }
    }

}
