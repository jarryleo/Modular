package cn.leo.frame.network.model

import cn.leo.frame.log.logD
import java.util.concurrent.ConcurrentHashMap

/**
 * @author : ling luo
 * @date : 2019-12-12
 * liveData 创建和缓存类
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class LiveDataHelper {

    private val mLiveDataCache = ConcurrentHashMap<String, MLiveData<*>>()

    /**
     * 获取LiveData
     */
    fun <R> getLiveData(key: String): MLiveData<R> {
        logD("LiveData key  = $key")
        return if (mLiveDataCache.containsKey(key)) {
            mLiveDataCache[key] as MLiveData<R>
        } else {
            val liveData = MLiveData<R>()
            mLiveDataCache[key] = liveData
            liveData
        }
    }

    /**
     * 释放资源
     */
    fun clear() {
        mLiveDataCache.clear()
    }

}