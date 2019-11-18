package cn.leo.frame.network.exceptions

import android.net.ParseException
import cn.leo.frame.R
import cn.leo.frame.utils.ResUtil
import com.bumptech.glide.load.HttpException
import com.google.gson.JsonParseException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author : ling luo
 * @date : 2019-04-26
 */
object FactoryException {
    private val HttpException_MSG = ResUtil.getString(R.string.network_exception)
    private val ConnectException_MSG = ResUtil.getString(R.string.connect_exception)
    private val JSONException_MSG = ResUtil.getString(R.string.parse_json_exception)
    private val UnknownHostException_MSG = ResUtil.getString(R.string.parse_host_exception)

    /**
     * 解析异常
     *
     * @param e
     * @return
     */
    fun analysisException(e: Throwable): ApiException {
        val apiException = ApiException(e)
        if (e is NetWorkException) {
            /*网络异常*/
            apiException.code = e.code
            apiException.msg = e.displayMessage
        } else if (e is HttpException) {
            /*网络异常*/
            apiException.code = CodeException.HTTP_ERROR
            apiException.msg = HttpException_MSG
        } else if (e is HttpTimeException) {
            /*自定义运行时异常*/
            apiException.code = CodeException.RUNTIME_ERROR
            apiException.msg = e.message
        } else if (e is ConnectException || e is SocketTimeoutException) {
            /*链接异常*/
            apiException.code = CodeException.HTTP_ERROR
            apiException.msg = ConnectException_MSG
        } else if (e is JsonParseException ||
            e is JSONException ||
            e is ParseException
        ) {
            /*json解析异常*/
            apiException.code = CodeException.JSON_ERROR
            apiException.msg = JSONException_MSG
        } else if (e is BusinessException) {
            /*业务异常（服务器返回的请求失败信息）*/
            apiException.code = e.code
            apiException.msg = e.msg
        } else if (e is UnknownHostException) {
            /*无法解析该域名异常*/
            apiException.code = CodeException.UNKOWN_HOST_ERROR
            apiException.msg = UnknownHostException_MSG
        } else {
            /*未知异常*/
            apiException.code = CodeException.UNKNOWN_ERROR
            apiException.msg = e.message
        }
        return apiException
    }
}
