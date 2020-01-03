package cn.leo.base.model

import androidx.lifecycle.LifecycleOwner
import cn.leo.base.bean.BaseListBean
import cn.leo.base.bean.WechatUserBean
import cn.leo.base.db.DB
import cn.leo.base.db.bean.User
import cn.leo.base.db.helper.DbModelProperty
import cn.leo.base.net.Apis
import cn.leo.base.support.SmartRefreshHelper
import cn.leo.frame.log.logE
import cn.leo.frame.network.exceptions.ApiException
import cn.leo.frame.network.exceptions.BusinessException
import cn.leo.frame.network.http.awaitAll

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class WechatModel : BaseModel(), SmartRefreshHelper.ISource<WechatUserBean> {

    private val db by DbModelProperty(DB::class.java)

    fun test(id: Int) = async {
        if (add() > 3) {
            add() + id
        } else {
            throw BusinessException("测试错误")
        }
    }

    private fun add(): Int {
        return 5
    }

    fun wechat() {
        apis(123).getWechatUserInfo("12311", "45611")
    }

    fun nativeTest() {
        testSecondSub()
    }

    fun testSecondSub() = async {
        val jobs =
            /*(0 until 10).map {
                api.getWechatUserInfo("", "")
            }*/
            mutableListOf(
                api.getWechatUserInfo("", "")
            )
        logE("请求数据 --------")
        val list = jobs.awaitAll()
        //list.map { it.data }
        list
    }

    fun insert(user: User) = async {
        db.userDao().insert(user)
    }

    fun findUserById(id: Long) = async {
        db.userDao().findUserById(id)
    }


    override fun requestList(page: Int) {
        apis.getWechatUserInfo("", "")
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