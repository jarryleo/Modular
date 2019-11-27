package cn.leo.base.model

import cn.leo.frame.log.Logger
import cn.leo.frame.network.MJob
import cn.leo.frame.network.exceptions.BusinessException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class WechatModel : BaseModel() {

    fun test(id: Int) = sync {
        if (add() > 3) {
            add() + id
        } else {
            throw BusinessException("测试错误")
        }
    }

    fun add(): Int {
        return 3
    }


    fun test2() = async {
        Logger.e("执行完毕")
        val request = api.getWechatUserInfo("", "").await()
        request
    }


}