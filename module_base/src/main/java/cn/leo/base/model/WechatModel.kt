package cn.leo.base.model

import cn.leo.base.bean.BaseBean
import cn.leo.base.bean.BaseListBean
import cn.leo.base.bean.WechatUserBean
import cn.leo.base.support.SmartRefreshHelper
import cn.leo.frame.network.MLiveData
import cn.leo.frame.network.exceptions.BusinessException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class WechatModel : BaseModel(), SmartRefreshHelper.ISource<WechatUserBean> {

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

    override fun requestList(page: Int) {
        apis().getWechatUserInfo("", "")
    }

    override fun observeList(result: MLiveData.Result<BaseBean<BaseListBean<WechatUserBean>>>.() -> Unit) {
        //observe(Apis::getWechatUserInfo, result)
    }

}