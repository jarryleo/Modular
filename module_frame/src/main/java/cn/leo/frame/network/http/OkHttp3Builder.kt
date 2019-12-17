package cn.leo.frame.network.http

import cn.leo.frame.BuildConfig
import cn.leo.frame.MFrame
import cn.leo.frame.network.interceptor.LoggerInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Created by Leo on 2018/1/4.
 */

class OkHttp3Builder {
    private var mCacheDir: File = MFrame.context.cacheDir
    private val mInterceptorList = ArrayList<Interceptor>()
    fun connectTimeout(timeout: Int): OkHttp3Builder {
        CONNECT_TIMEOUT = timeout
        return this
    }

    fun writeTimeout(timeout: Int): OkHttp3Builder {
        WRITE_TIMEOUT = timeout
        return this
    }

    fun readTimeout(timeout: Int): OkHttp3Builder {
        READ_TIMEOUT = timeout
        return this
    }

    fun cache(cacheDir: File, cacheSize: Int): OkHttp3Builder {
        mCacheDir = cacheDir
        mCacheSize = cacheSize
        return this
    }

    fun addInterceptor(interceptor: Interceptor): OkHttp3Builder {
        mInterceptorList.add(interceptor)
        return this
    }

    fun build(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(
                CONNECT_TIMEOUT.toLong(),
                TimeUnit.SECONDS
            )
            .writeTimeout(
                WRITE_TIMEOUT.toLong(),
                TimeUnit.SECONDS
            )
            .readTimeout(
                READ_TIMEOUT.toLong(),
                TimeUnit.SECONDS
            )
        mCacheDir.let {
            builder.cache(Cache(it, mCacheSize.toLong()))
        }
        for (interceptor in mInterceptorList) {
            builder.addInterceptor(interceptor)
        }
        mInterceptorList.clear()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(LoggerInterceptor())
        }
        /**
         * 支持https操作
         */
        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        val trustAllCerts = arrayOf<TrustManager>(trustManager)
        var sslSocketFactory: SSLSocketFactory? = null
        var hostnameVerifier: HostnameVerifier? = null
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            sslSocketFactory = sslContext.socketFactory
            hostnameVerifier = HostnameVerifier { hostname, session -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        builder.sslSocketFactory(sslSocketFactory!!, trustManager)
        builder.hostnameVerifier(hostnameVerifier!!)
        return builder.build()
    }

    companion object {
        private var READ_TIMEOUT = 30
        private var WRITE_TIMEOUT = 30
        private var CONNECT_TIMEOUT = 30
        private var mCacheSize = 10 * 1024 * 1024
    }
}
