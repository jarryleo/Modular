package cn.leo.base.base

/**
 * @author : ling luo
 * @date : 2020/4/27
 * @description view层统一接口，保证fragment和activity可以统一
 */
interface SuperView {

    /**
     * 初始化观察者，观察请求结果
     */
    fun onInitObserve()

    /**
     * 初始化View
     */
    fun onInitView()

    /**
     * 初始化事件
     */
    fun onInitEvent()

    /**
     * 初始化请求，网络请求，数据库请求等
     */
    fun onInitRequest()
}