package cn.leo.modular

import cn.leo.base.base.SuperActionBarFragment
import cn.leo.base.support.actionBar
import cn.leo.base.support.setActionBarTitle
import cn.leo.frame.support.singleClick
import cn.leo.frame.utils.toast
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author : ling luo
 * @date : 2019-12-04
 */
class TestFragment : SuperActionBarFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun onInitialize() {
        actionBar("Fragment标题", "菜单") {
            toast("点击菜单")
        }

        tvTest.singleClick {
            setActionBarTitle("标题欢乐")
        }
    }

    override fun hasActionBar(): Boolean {
        return true
    }

}