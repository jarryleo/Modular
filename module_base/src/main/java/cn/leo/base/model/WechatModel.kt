package cn.leo.base.model

import cn.leo.frame.log.Logger
import kotlinx.coroutines.delay

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class WechatModel : BaseModel() {

    fun test(id: Int) = async {
        add() + id
    }

    fun add(): Int {
        return 3
    }


    fun test2() = async {

        Logger.e("执行完毕")
        delay(3000)

    }


}