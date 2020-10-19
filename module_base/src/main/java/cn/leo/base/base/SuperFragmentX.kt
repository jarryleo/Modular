package cn.leo.base.base

/**
 * @author : ling luo
 * @date : 2020/8/28
 * @description : android X  Fragment 懒加载基类 后续完成
 */
abstract class SuperFragmentX : SuperView {

    /**
     * 是否懒加载
     *
     * @return
     */
    protected open fun isLazyLoad(): Boolean {
        return true
    }

    override fun onInitObserve() {

    }

    override fun onInitView() {

    }

    override fun onInitEvent() {

    }

    override fun onInitRequest() {

    }

}