package cn.leo.frame.ui



/**
 * 加载弹窗
 * @author Max
 * @date 2019-09-17.
 */
interface ILoading {

    fun showLoading(message: CharSequence? = null)

    fun dismissLoading()
}