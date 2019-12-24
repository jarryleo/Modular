package cn.leo.modular

import cn.leo.base.arouter.pages.PagesHome
import cn.leo.base.base.BaseModelActivity
import cn.leo.base.db.bean.User
import cn.leo.base.model.WechatModel
import cn.leo.base.net.Apis
import cn.leo.base.support.actionBar
import cn.leo.base.support.doubleClickExit
import cn.leo.base.support.setActionBarTitle
import cn.leo.frame.image.loadImage
import cn.leo.frame.log.Logger
import cn.leo.frame.log.logE
import cn.leo.frame.network.model.GlobalModelCreator
import cn.leo.frame.support.*
import cn.leo.frame.utils.toast
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
        actionBar(
            "测试哈哈哈哈哈测试哈哈哈哈哈",
            "菜单"
        ) {

            model.insert(User(1, "Tom"))
            PagesHome.homeMain3Activity.jump()
        }


        tvTest.setOnClickListener {
            test()
            model.findUserById(1)

            ivTest.loadImage(
                "https://pic2.zhimg.com/v2-7cb8b1ea5e11779e25b4b35d52b777f2_xll.jpg",
                corners = 12.dp(),
                defResId = R.drawable.ic_launcher_background,
                errResId = R.drawable.ic_launcher_background,
                onLoadFailed = { _, _ ->
                    logE("图片加载失败")
                }
            )
        }

        val nodes = arrayOf("v1", "v2", "v3", "v4", "v5", "v6")
        node.setNodes(nodes)
        node.setProgress(3)

        etTest.imeSearch {
            etTest.checkEmpty { toast("输入不能为空"); return@imeSearch }
                .checkNum(2, 6) { toast("字符不符合");return@imeSearch }
            toast(it)
        }

    }

    override fun onPostResume() {
        super.onPostResume()
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
                logE("请求失败====")
            }
        }


        model.observe(this, model::test) {
            success {
                Logger.e("result = $it")
            }

            failed {
                logE(it.msg ?: "")
            }
        }

        globalModel.observe(this, globalModel::setTitle) {
            success { setActionBarTitle(it) }
        }

    }

    private fun test() {

        //model.test(1)

        //model.apis(123).getWechatUserInfo("12311", "45611")
        model.apis.getWechatUserInfo("123", "456")
    }

    override fun onBackPressed() {
        doubleClickExit { msg = "再按一次试试" }
    }

}
