package cn.leo.base.model

import cn.leo.BaseLiveData
import cn.leo.base.bean.WechatUserBean
import cn.leo.frame.network.JLLiveData

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class WechatModel : BaseModel() {
    private val data = BaseLiveData<WechatUserBean>()

    fun test(): JLLiveData<WechatUserBean> {
        //自动把结果分类传给presenter
        api.getWechatUserInfo("abc", "123").request(data)
        return data
    }
}