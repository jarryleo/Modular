package cn.leo.base.support

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import cn.leo.base.R
import cn.leo.base.base.SuperActionBarFragment
import cn.leo.frame.support.getColor
import cn.leo.frame.support.setDarkStatusBar
import cn.leo.frame.support.visibleOrGone

/**
 * @author : ling luo
 * @date : 2019-11-30
 */

/**
 * 自定义标题栏
 */
@SuppressLint("RestrictedApi")
inline fun AppCompatActivity.actionBar(
    title: String,
    menu: String = "",
    hasBack: Boolean = true,
    @ColorRes actionBarColor: Int = R.color.colorPrimary,
    @ColorRes statusBarColor: Int = actionBarColor,
    @ColorRes titleColor: Int = android.R.color.white,
    @ColorRes menuColor: Int = titleColor,
    @DrawableRes backIcon: Int = R.drawable.base_actionbar_back,
    @LayoutRes layout: Int = R.layout.base_actionbar_menu_layout,
    elevation: Float = 3.0f,
    crossinline menuClick: (View) -> Unit = {}
) {
    supportActionBar?.apply {
        setCustomView(layout)
        val backView: ImageView? = customView.findViewById(R.id.base_actionbar_back)
        val titleView: TextView? = customView.findViewById(R.id.base_actionbar_title)
        val menuView: TextView? = customView.findViewById(R.id.base_actionbar_menu)
        customView.setBackgroundColor(actionBarColor.getColor())
        setDarkStatusBar(statusBarColor.getColor())
        titleView?.text = title
        titleView?.setTextColor(titleColor.getColor())
        if (menu.isNotEmpty()) {
            menuView?.text = menu
            menuView?.setTextColor(menuColor.getColor())
            menuView?.setOnClickListener { menuClick(it) }
        }
        backView?.setImageResource(backIcon)
        backView?.setOnClickListener { onBackPressed() }
        backView?.visibleOrGone { hasBack }
        displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setDefaultDisplayHomeAsUpEnabled(false)
        setDisplayShowCustomEnabled(true)
        setDisplayShowHomeEnabled(false)
        setDisplayShowTitleEnabled(false)
        this.elevation = elevation
    }
}

inline fun SuperActionBarFragment.actionBar(
    title: String,
    menu: String = "",
    hasBack: Boolean = true,
    @ColorRes actionBarColor: Int = R.color.colorPrimary,
    @ColorRes statusBarColor: Int = actionBarColor,
    @ColorRes titleColor: Int = android.R.color.white,
    @ColorRes menuColor: Int = titleColor,
    @DrawableRes backIcon: Int = R.drawable.base_actionbar_back,
    crossinline menuClick: (View) -> Unit = {}
) {
    view?.findViewById<View>(R.id.actionBar)?.apply {
        val backView: ImageView? = findViewById(R.id.base_actionbar_back)
        val titleView: TextView? = findViewById(R.id.base_actionbar_title)
        val menuView: TextView? = findViewById(R.id.base_actionbar_menu)
        setBackgroundColor(actionBarColor.getColor())

        activity?.setDarkStatusBar(statusBarColor.getColor())
        if (activity is AppCompatActivity) {
            (activity as? AppCompatActivity)?.supportActionBar?.hide()
        } else {
            activity?.actionBar?.hide()
        }
        titleView?.text = title
        titleView?.setTextColor(titleColor.getColor())
        if (menu.isNotEmpty()) {
            menuView?.text = menu
            menuView?.setTextColor(menuColor.getColor())
            menuView?.setOnClickListener { menuClick(it) }
        }
        backView?.setImageResource(backIcon)
        backView?.setOnClickListener { activity?.onBackPressed() }
        backView?.visibleOrGone { hasBack }
    }
}

/**
 * 修改标题栏标题
 */
fun AppCompatActivity.setActionBarTitle(title: String) {
    supportActionBar?.apply {
        setTitle(title)
        val titleView: TextView? = customView?.findViewById(R.id.base_actionbar_title)
        titleView?.text = title
    }
}

fun SuperActionBarFragment.setActionBarTitle(title: String) {
    view?.findViewById<View>(R.id.actionBar)?.apply {
        val titleView: TextView? = findViewById(R.id.base_actionbar_title)
        titleView?.text = title
    }
}

/**
 * 修改标题栏菜单文字
 */
fun AppCompatActivity.setMenu(text: String) {
    supportActionBar?.apply {
        val menuView: TextView? = customView?.findViewById(R.id.base_actionbar_menu)
        menuView?.text = text
    }
}

fun SuperActionBarFragment.setMenu(text: String) {
    view?.findViewById<View>(R.id.actionBar)?.apply {
        val menuView: TextView? = findViewById(R.id.base_actionbar_menu)
        menuView?.text = text
    }
}

/**
 * 获取到标题栏view
 */
fun AppCompatActivity.actionBarView() = supportActionBar?.customView

/**
 * 获取到标题view
 */
fun AppCompatActivity.titleView(): TextView? =
    supportActionBar?.customView?.findViewById(R.id.base_actionbar_title)

/**
 * 获取到菜单view
 */
fun AppCompatActivity.menuView(): TextView? =
    supportActionBar?.customView?.findViewById(R.id.base_actionbar_menu)