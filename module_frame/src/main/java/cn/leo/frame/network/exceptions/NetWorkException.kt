package cn.leo.frame.network.exceptions

/**
 * @author : ling luo
 * @date : 2019-04-26
 */
class NetWorkException(showMsg: String) : Exception(showMsg) {
    /*错误码*/
    @get:CodeException.CodeEp
    var code = CodeException.NETWORD_ERROR
    /*显示的信息*/
    var displayMessage: String? = null


    init {
        code = CodeException.NETWORD_ERROR
        displayMessage = showMsg
    }
}
