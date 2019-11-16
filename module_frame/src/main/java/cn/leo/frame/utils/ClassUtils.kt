package cn.leo.frame.utils

import java.lang.reflect.ParameterizedType

/**
 * @author Max
 * @date 2019-10-06.
 */

object ClassUtils {

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenericManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration,
     * or <code>Object.class</code> if cannot be determine
     */
    fun <T> getSuperClassGenericType(clazz: Class<*>): Class<T> {
        return getSuperClassGenericType(clazz, 0)
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenericManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.</Book>
     */
    fun <T> getSuperClassGenericType(clazz: Class<*>, index: Int): Class<T> {
        var cls: Class<*>? = clazz
        var genType = cls?.genericSuperclass
        while (genType !is ParameterizedType) {
            cls = cls?.superclass
            requireNotNull(cls)
            genType = cls.genericSuperclass
        }
        val params = genType.actualTypeArguments
        require(!(index >= params.size || index < 0))
        return if (params[index] !is Class<*>) {
            throw IllegalArgumentException()
        } else params[index] as Class<T>
    }
}