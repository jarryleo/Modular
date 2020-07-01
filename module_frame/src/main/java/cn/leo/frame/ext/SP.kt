package cn.leo.frame.ext

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-08-31
 *
 * 委托 SharedPreferences ，使用非常简单：
 * private var test: Int by SP("test", 0)
 * 直接对 test 读取和赋值 即可操作 SharedPreferences
 */
class SP<T>(
    private val key: String,
    private val default: T,
    private val name: String = DEFAULT_CONFIG
) :
    ReadWriteProperty<Any?, T> {

    companion object {
        const val DEFAULT_CONFIG = "config"

        //多sharedPreferences
        private val preferenceMap: ConcurrentHashMap<String, SharedPreferences> by lazy {
            ConcurrentHashMap<String, SharedPreferences>()
        }

        //清除
        fun clear(name: String = DEFAULT_CONFIG) {
            getSharedPreferences(name).edit().clear().apply()
        }

        //获取SP
        fun getSharedPreferences(name: String): SharedPreferences {
            if (!preferenceMap.contains(name)) {
                preferenceMap[name] =
                    ContextManager.context.getSharedPreferences(name, Context.MODE_PRIVATE)
            }
            return preferenceMap[name]!!
        }
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        with(getSharedPreferences(name)) {
            val value = when (default) {
                is String -> getString(key, default)
                is Int -> getInt(key, default)
                is Long -> getLong(key, default)
                is Float -> getFloat(key, default)
                is Boolean -> getBoolean(key, default)
                else -> throw IllegalArgumentException("This type of data can not be get! ")
            }
            value as T
        }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        with(getSharedPreferences(name).edit()) {
            when (default) {
                is String -> putString(key, value as String)
                is Int -> putInt(key, value as Int)
                is Long -> putLong(key, value as Long)
                is Float -> putFloat(key, value as Float)
                is Boolean -> putBoolean(key, value as Boolean)
                else -> throw IllegalArgumentException("This type of data can not be saved! ")
            }.apply()
        }
}