package cn.leo.modular

import cn.leo.base.arouter.pages.PagesHome
import cn.leo.base.base.BaseModelActivity
import cn.leo.base.model.WechatModel
import cn.leo.frame.log.logE
import cn.leo.frame.support.showFragment
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = PagesHome.homeMain3Activity)
class Main3Activity : BaseModelActivity<WechatModel>() {


    override fun getLayoutRes(): Int {
        return R.layout.activity_main3
    }

    override fun onInitView() {
        showFragment<TestFragment>(R.id.fragmentContainer)
    }

    override fun onInitObserve() {
        super.onInitObserve()
        model.observe(this, model::test) {
            success {
                logE("Main3Activity wechatModel test = $it")
            }
        }
    }
}
