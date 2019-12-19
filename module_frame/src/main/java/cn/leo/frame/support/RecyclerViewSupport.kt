package cn.leo.frame.support

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import cn.leo.frame.support.RecyclerViewSupport.onScrollListenerTag
import com.bumptech.glide.Glide

/**
 * @author : ling luo
 * @date : 2019-12-19
 */


object RecyclerViewSupport {
    const val onScrollListenerTag = 2019_12_19_12
}

/**
 * 配置RecyclerView滑动时候不加载图片
 */
fun RecyclerView.setPauseLoadImageOnScrolling(toggle: Boolean) {
    val tag =
        getTag(onScrollListenerTag) as? RecyclerView.OnScrollListener
    if (toggle) {
        if (tag == null) {
            val listener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        SCROLL_STATE_IDLE ->
                            Glide.with(this@setPauseLoadImageOnScrolling).resumeRequests()
                        else -> Glide.with(context).pauseRequests()
                    }
                }
            }
            addOnScrollListener(listener)
            setTag(onScrollListenerTag, listener)
        }
    } else {
        if (tag != null) {
            removeOnScrollListener(tag)
        }
    }

}