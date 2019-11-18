package cn.leo.base.model

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class WechatModel : BaseModel() {

    fun test(id: Int, result: (Int) -> Unit) {

        result(id + 1)
    }

}