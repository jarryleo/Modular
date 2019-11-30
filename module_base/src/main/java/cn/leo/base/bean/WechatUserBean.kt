package cn.leo.base.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * @author : ling luo
 * @date : 2019-06-24
 */
@Parcelize
data class WechatUserBean(
    val errcode: Int,
    val errmsg: String,
    val openid: String,
    val nickname: String,
    val headimgurl: String,
    val sex: String,
    val city: String,
    val country: String,
    val unionid: String,
    val province: String
) : Parcelable