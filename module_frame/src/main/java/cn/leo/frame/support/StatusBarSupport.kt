package cn.leo.frame.support

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import cn.leo.frame.MFrame

/**
 * @author : ling luo
 * @date : 2019-11-29
 */

/**
 * 获取状态栏高度——方法
 */
fun getStatusBarHeight(): Int {
    var statusBarHeight = -1
    //获取status_bar_height资源的ID
    val resourceId = MFrame.context.resources
        .getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        //根据资源ID获取响应的尺寸值
        statusBarHeight = MFrame.context.resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}


/**
 * 透明状态栏
 */
fun Activity.translucentStatusBar() {
    //KITKAT也能满足，只是SYSTEM_UI_FLAG_LIGHT_STATUS_BAR（状态栏字体颜色反转）只有在6.0才有效
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.apply {
            //透明状态栏
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
            /* win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);*/
            // 不调节状态栏文字颜色
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            // 部分机型的statusBar会有半透明的黑色背景
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // SDK21
            statusBarColor = Color.TRANSPARENT
        }
    }
}

/**
 * 设置透明状态栏且文字为深颜色
 */
fun Activity.setLightTranslucentStatusBar() {
    window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //将字体颜色设置为深色
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        } else {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}

/**
 * 设置不透明状态栏且文字为深颜色
 */
fun Activity.setLightStatusBar(@ColorInt color: Int = Color.WHITE) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            statusBarColor = color
        }
    }
}

/**
 * 设置透明状态栏文字为浅颜色
 */
fun Activity.setDarkTranslucentStatusBar() {
    window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        } else {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}

/**
 * 设置不透明状态栏且文字为浅颜色
 */
fun Activity.setDarkStatusBar(@ColorInt color: Int = Color.BLACK) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            statusBarColor = color
        }
    }
}

/**
 * 隐藏状态栏
 */
fun Activity.hideStatusBar() {
    val params = window.attributes
    params.systemUiVisibility =
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN
    window.attributes = params
    var uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

            or View.SYSTEM_UI_FLAG_FULLSCREEN) // hide status bar

    uiFlags = if (Build.VERSION.SDK_INT >= 19) {
        uiFlags or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
    } else {
        uiFlags or View.SYSTEM_UI_FLAG_LOW_PROFILE
    }
    window.decorView.systemUiVisibility = uiFlags
}