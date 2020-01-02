package cn.leo.base.model

import cn.leo.base.net.Apis
import cn.leo.base.net.Urls
import cn.leo.frame.network.viewmodel.MViewModel

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class BaseModel : MViewModel<Apis>() {

    override fun getBaseUrl(): String {
        return Urls.BASE_URL
    }

}