package cn.leo.frame.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

/**
 * @author : ling luo
 * @date : 2019-08-31
 */

fun ImageView.netImage(
    url: String?,
    circle: Boolean = false,
    skipMemoryCache: Boolean = false,
    corners: Int = 0,
    listener: RequestListener<Drawable>? = null,
    @DrawableRes defResId: Int = -1,
    @DrawableRes errResId: Int = defResId
) {
    if ((context as? Activity)?.isDestroyed == true) {
        return
    }

    val transforms = when (scaleType) {
        ImageView.ScaleType.FIT_CENTER -> FitCenter()
        ImageView.ScaleType.CENTER_INSIDE -> CenterInside()
        else -> CenterCrop()
    }

    Glide.with(this)
        .load(url ?: "")
        .skipMemoryCache(skipMemoryCache)
        .diskCacheStrategy(
            if (skipMemoryCache) {
                DiskCacheStrategy.NONE
            } else {
                DiskCacheStrategy.AUTOMATIC
            }
        )
        .transform(
            *when {
                circle -> arrayOf(CircleCrop(), transforms)
                (corners > 0) -> arrayOf(RoundedCorners(corners), transforms)
                else -> arrayOf(transforms)
            }
        )
        .transition(withCrossFade())
        .placeholder(defResId)
        .error(errResId)
        .listener(listener)
        .into(this)
}

fun Context.getBitmap(
    url: String,
    callback: (bitmap: Bitmap, width: Int, height: Int) -> Unit
) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                callback(resource, resource.width, resource.height)
            }

        })
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


