package cn.leo.base.bean

/**
 * @author : ling luo
 * @date : 2019-06-24
 */
data class WechatAccessBean(
    val errcode: Int,
    val errmsg: String,
    val access_token: String,
    val openid: String
)