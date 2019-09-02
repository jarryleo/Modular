package cn.leo.base.bean


/**
 * @author : ling luo
 * @date : 2019-06-24
 */
data class WechatUserBean(
    val openid: String,
    val nickname: String,
    val headimgurl: String,
    val sex: String,
    val city: String,
    val country: String,
    val unionid: String,
    val province: String
) : BaseBean()