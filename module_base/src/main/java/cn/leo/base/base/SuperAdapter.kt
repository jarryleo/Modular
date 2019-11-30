package cn.leo.base.base

import cn.leo.base.support.SmartRefreshHelper
import cn.leo.frame.ui.LeoRvAdapter

/**
 * @author : ling luo
 * @date : 2019-11-29
 */
abstract class SuperAdapter<T : Any> : LeoRvAdapter<T>(), SmartRefreshHelper.IAdapter<T> {
    override fun addData(data: List<T>) {
        super.add(data)
    }

    override fun replaceData(data: List<T>) {
        super.data = data
    }
}