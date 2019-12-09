package cn.leo.base.model

import androidx.lifecycle.LifecycleOwner
import cn.leo.base.bean.BaseListBean
import cn.leo.base.bean.WechatUserBean
import cn.leo.base.db.DB
import cn.leo.base.db.bean.User
import cn.leo.base.db.helper.DbModelProperty
import cn.leo.base.net.Apis
import cn.leo.base.support.SmartRefreshHelper
import cn.leo.frame.network.exceptions.ApiException
import cn.leo.frame.network.exceptions.BusinessException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class WechatModel : BaseModel(), SmartRefreshHelper.ISource<WechatUserBean> {

    private val db by DbModelProperty(DB::class.java)

    fun test(id: Int) = sync {
        if (add() > 3) {
            testSecondSub()
            add() + id
        } else {
            throw BusinessException("测试错误")
        }
    }

    private fun add(): Int {
        return 5
    }

    fun testSecondSub() = async {
        22222
    }

    fun insert(user: User) = async {
        db.userDao().insert(user)
    }

    fun findUserById(id: Long) = async {
        db.userDao().findUserById(id)
    }


    fun test2() = async {
        api.getWechatUserInfo("", "").await()
    }

    override fun requestList(page: Int) {
        apis().getWechatUserInfo("", "")
    }


    override fun observeList(
        lifecycleOwner: LifecycleOwner,
        failedCallback: (apiException: ApiException) -> Unit,
        successCallback: (data: BaseListBean<WechatUserBean>) -> Unit
    ) {
        observe(lifecycleOwner, Apis::getWechatUserInfo) {
            success {
                //successCallback(it)
            }
            failed = failedCallback
        }
    }
}