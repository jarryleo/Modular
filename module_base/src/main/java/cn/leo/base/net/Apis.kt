package cn.leo.base.net

import cn.leo.base.bean.WechatAccessBean
import cn.leo.base.bean.WechatUserBean
import cn.leo.frame.network.MJob
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * @author : ling luo
 * @date : 2019-07-02
 */
interface Apis {
    /**
     * 获取微信用户信息
     */
    @GET(Urls.GET_USER_INFO)
    fun getWechatUserInfo(
        @Query("access_token") access_token: String,
        @Query("openid") openid: String
    ): MJob<WechatUserBean>


    /**
     * 获取微信授权
     */
    @GET(Urls.GET_ACCESS_TOKEN)
    fun getWecharAccessToken(
        @Query("appid") appid: String,
        @Query("secret") secret: String,
        @Query("errcode") code: String,
        @Query("grant_type") grant_type: String = "authorization_code"
    ): MJob<WechatAccessBean>

    /*
    @FormUrlEncoded
    @POST(Urls.POST_UPDATE_USER_SHARE_INFO)
    fun updateUserShareInfo(
        @Field("isShare") isShare: Int,
        @Field("videoId") videoId: Long
    ): MJob<UpdateUserShareInfoBean>
    */


}