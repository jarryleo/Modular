package cn.leo.base.model

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
        return 4
    }


    fun test2() = async {
        api.getWechatUserInfo("", "").await()
    }


}