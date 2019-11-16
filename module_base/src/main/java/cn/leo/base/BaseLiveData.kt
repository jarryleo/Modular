package cn.leo

import cn.leo.base.bean.BaseBean
import cn.leo.frame.network.MLiveData

/**
 * @author : ling luo
 * @date : 2019-08-30
 */

class BaseLiveData<T : BaseBean> : MLiveData<T>() {
    /*override fun failed(value: T) {
        if (value.errcode == 10086) {
            //错误code全局处理
        } else {
            super.failed(value)
        }
    }*/

    override fun success(value: T) {
        //判断结果是不是错误码
        if (value.errcode != 0) {
            //failed(value)
        } else {
            super.success(value)
        }
    }
}