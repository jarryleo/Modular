package cn.leo.frame.ext

import android.content.Context
import androidx.startup.Initializer
import kotlin.properties.Delegates

/**
 * @author : ling luo
 * @date : 2020/7/1
 * 清单文件注册启动器获取上下文
 *
 * JetPack 最新库
 *
dependencies {
    implementation "androidx.startup:startup-runtime:1.0.0-alpha01"
}
清单文件注册：
<provider
android:name="androidx.startup.InitializationProvider"
android:authorities="${applicationId}.androidx-startup"
android:exported="false"
tools:node="merge">
<meta-data
    android:name="cn.leo.frame.ext.ContextInitializer"
    android:value="androidx.startup" />
</provider>
 *
 *
 */
class ContextInitializer : Initializer<ContextManager> {
    override fun create(context: Context): ContextManager {
        return ContextManager.getInstance(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}

class ContextManager private constructor() {
    companion object {
        var context by Delegates.notNull<Context>()
        fun getInstance(ctx: Context): ContextManager {
            context = ctx
            return ContextManager()
        }
    }
}