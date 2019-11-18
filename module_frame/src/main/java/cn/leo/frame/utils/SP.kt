package cn.leo.frame.utils

import android.content.Context
import cn.leo.frame.MFrame
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-08-31
 */
class SP<T>(private val key: String, private val default: T) : ReadWriteProperty<Any?, T> {
    companion object {
        val preference = MFrame.context.getSharedPreferences("config", Context.MODE_PRIVATE)
        fun clear() {
            preference.edit().clear().apply()
        }
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        with(preference) {
            val value = when (default) {
                is String -> getString(key,default)
                is Int -> getInt(key, default)
                is Long -> getLong(key, default)
                is Float -> getFloat(key, default)
                is Boolean -> getBoolean(key, default)
                else -> throw IllegalArgumentException("This type of data can not be get! ")
            }
            value as T
        }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T)=
        with(preference.edit()){
            when (default) {
                is String -> putString(key, default)
                is Int -> putInt(key, default)
                is Long -> putLong(key, default)
                is Float -> putFloat(key, default)
                is Boolean -> putBoolean(key, default)
                else -> throw IllegalArgumentException("This type of data can not be saved! ")
            }.apply()
        }
}