package cn.leo.frame.network.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class HttpLoader private constructor(private val mRetrofit: Retrofit) {

    class Builder() {
        constructor(baseUrl: String) : this() {
            mBaseUrl = baseUrl
        }

        private var mBaseUrl: String = ""
            set(value) {
                field = if (value.endsWith("/")) value
                else "$value/"
            }

        private var mHttpClient: OkHttpClient = OkHttp3Builder()
            .build()

        fun client(okHttpClient: OkHttpClient): Builder {
            mHttpClient = okHttpClient
            return this
        }

        fun build(): HttpLoader {
            return HttpLoader(
                Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .client(mHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(MJobAdapterFactory())
                    .build()
            )
        }
    }

    fun <T> create(service: Class<T>): T {
        return mRetrofit.create(service)
    }


}