package cn.leo.frame.ext

import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

/**
 * @author : ling luo
 * @date : 2019-08-31
 *
 * 委托 DataStore ，使用非常简单：
 * private var test: Int by DataStoreExt("test", 0)
 * 直接对 test 读取和赋值 即可操作 DataStore
 */
class DataStoreExt(
    private val name: String = DEFAULT_CONFIG
) {
    companion object {
        const val DEFAULT_CONFIG = "config"

        /**
         * 多个DataStore缓存
         */
        private val dataStoreMap: ConcurrentHashMap<String, DataStore<Preferences>> by lazy {
            ConcurrentHashMap<String, DataStore<Preferences>>()
        }

        /**
         * 清除对应文件全部数据
         */
        fun clear(name: String = DEFAULT_CONFIG) {
            io {
                getDataStore(name).edit {
                    it.clear()
                }
            }
        }

        /**
         * 获取DataStore
         */
        fun getDataStore(name: String): DataStore<Preferences> {
            if (!dataStoreMap.contains(name)) {
                dataStoreMap[name] =
                    ContextManager.context.createDataStore(DEFAULT_CONFIG)
            }
            return dataStoreMap[name]!!
        }
    }

    /**
     * 获取值
     * @param key preferencesKey("")
     */
    fun <R> getValue(key: Preferences.Key<R>): Flow<R?> =
        getDataStore(name).data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[key]
        }

    /**
     * 保存值
     * @param key preferencesKey("")
     */
    fun setValue(key: Preferences.Key<R>, value: R) {
        io {
            getDataStore(name).edit {
                it[key] = value
            }
        }
    }

}