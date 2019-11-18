package cn.leo.base.model

import cn.leo.frame.log.Logger
import kotlinx.coroutines.delay

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class WechatModel : BaseModel() {

    fun test(id: Int) = async {
        id + 1
    }


    fun test2() = async {

        Logger.e("执行完毕")
        delay(3000)

    }


}