package cn.leo.modular

import cn.leo.base.base.SuperActionBarFragment
import cn.leo.base.support.actionBar
import cn.leo.frame.utils.toast

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
    }
}