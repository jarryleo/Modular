package cn.leo.frame.image

import android.app.Activity
import android.content.Context
import android.support.annotation.DrawableRes
import android.widget.ImageView
import cn.leo.frame.utils.UIUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import java.io.File

/**
 * @author : ling luo
 * @date : 2019-08-31
 */

fun ImageView.netImage(
    url: String,
    circle: Boolean = false,
    corners: Float = 0f,
    @DrawableRes defResId: Int = -1,
    @DrawableRes errResId: Int = -1
) {
    if ((context as? Activity)?.isDestroyed == true) {
        return
    }
    Glide.with(this)
        .load(url)
        .transform(
            *when {
                circle -> arrayOf(CircleCrop())
                corners > 0 -> arrayOf(CenterCrop(), RoundedCorners(UIUtil.dip2px(corners)))
                else -> arrayOf(FitCenter())
            }
        )
        .transition(withCrossFade())
        .placeholder(defResId)
        .error(errResId)
        .into(this)
}

fun Context.downloadImage(url: String): File? {
    return try {
        Glide.with(this)
            .asFile()
            .load(url)
            .submit()
            .get()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}