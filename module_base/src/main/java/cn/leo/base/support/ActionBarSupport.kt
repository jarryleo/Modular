package cn.leo.base.support

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import cn.leo.base.R

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
    @DrawableRes backIcon: Int = R.drawable.base_actionbar_back,
    @LayoutRes layout: Int = R.layout.base_actionbar_menu_layout,
    elevation: Float = 3.0f,
    crossinline menuClick: (View) -> Unit = {}
) {
    supportActionBar?.apply {
        val actionBarView: View =
            LayoutInflater.from(this@actionBar).inflate(layout, null)
        val backView: ImageView? = actionBarView.findViewById(R.id.base_actionbar_back)
        val titleView: TextView? = actionBarView.findViewById(R.id.base_actionbar_title)
        val menuView: TextView? = actionBarView.findViewById(R.id.base_actionbar_menu)
        titleView?.text = title
        menuView?.text = menu
        menuView?.setOnClickListener { menuClick(it) }
        backView?.setImageResource(backIcon)
        backView?.setOnClickListener { onBackPressed() }
        backView?.visibility = if (hasBack) {
            View.VISIBLE
        } else {
            View.GONE
        }
        setCustomView(
            actionBarView, ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT
            )
        )
        displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setDefaultDisplayHomeAsUpEnabled(false)
        setDisplayShowCustomEnabled(true)
        setDisplayShowHomeEnabled(false)
        setDisplayShowTitleEnabled(false)
        this.elevation = elevation
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

/**
 * 修改标题栏菜单文字
 */
fun AppCompatActivity.setMenu(text: String) {
    supportActionBar?.apply {
        val menuView: TextView? = customView?.findViewById(R.id.base_actionbar_menu)
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