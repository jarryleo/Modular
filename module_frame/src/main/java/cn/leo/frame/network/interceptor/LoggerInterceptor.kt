package cn.leo.frame.network.interceptor


import android.util.Log
import cn.leo.frame.network.sign.UrlUtil
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset


/**
 * @author lingluo
 */
class LoggerInterceptor : Interceptor {

    @Synchronized
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.nanoTime()
        Log.d(TAG, "----------Start----------")
        Log.d(TAG, String.format("请求地址：| %s", request.url))
        //执行请求
        val response = chain.proceed(request)
        printParams(request.body)
        //拿到请求结果
        val body = response.body
        val source = body!!.source()
        // Buffer the entire body.
        source.request(Integer.MAX_VALUE.toLong())
        val buffer = source.buffer()
        //接口数据大于16K不显示
        if (buffer.size > 1024 * 32) {
            Log.d(
                TAG, UrlUtil.getBaseUrl(request.url.toString())
                        + "请求返回：| (长度:" + buffer.size
                        + ")大于16K不打印,点击链接在网页查看"
            )
            return response
        } else {
            val headers = response.headers
            val header = headers.toString()
            Log.d(TAG, String.format("headers：|\n%s\n", header))

            var charset: Charset? = Charset.defaultCharset()
            val contentType = body.contentType()
            if (contentType != null) {
                charset = contentType.charset(charset)
            }
            val bodyString = buffer.clone().readString(charset!!)
            //String str = URLDecoder.decode(bodyString, "UTF-8");
            Log.d(TAG, String.format("请求返回：| %s", bodyString))
        }
        val endTime = System.nanoTime()
        val duration = (endTime - startTime) / 1e6
        Log.d(TAG, "----------End:请求耗时:" + duration + "毫秒----------")
        return response
    }

    @Throws(IOException::class)
    private fun printParams(body: RequestBody?) {
        if (body == null) {
            return
        }
        val buffer = Buffer()
        body.writeTo(buffer)
        var charset: Charset? = Charset.forName("UTF-8")
        val contentType = body.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        val params = buffer.readString(charset!!)
        Log.d(TAG, "请求参数：| $params")
    }

    companion object {
        var TAG = "LoggerInterceptor"
    }
}
