package cn.leo.base.support

import android.view.View
import androidx.annotation.LayoutRes
import cn.leo.base.R
import cn.leo.frame.ui.StatusManager

/**
 * @author : ling luo
 * @date : 2019-11-30
 */
/**
 * 多状态初始化
 */
fun View.statusConfig(
    @LayoutRes loadingRes: Int = R.layout.base_status_loading,
    @LayoutRes emptyRes: Int = R.layout.base_status_empty,
    @LayoutRes errorRes: Int = R.layout.base_status_error,
    retry: (statusManager: StatusManager, stateView: View) -> Unit = { _, _ -> Unit }
): StatusManager {

    this.getTag(R.id.base_status_manager)?.apply {
        //已经初始配置过
        if (this is StatusManager) {
            return this
        }
    }
    val statusManager = StatusManager.builder(this)
        // 设置加载中布局
        .loadingViewLayout(loadingRes)
        // 设置空数据布局
        .emptyViewLayout(emptyRes)
        // 设置出错布局
        .errorViewLayout(errorRes)
        // 设置空数据布局重试按钮 ID
        .addRetryButtonId(R.id.base_status_retry)
        //重试事件
        .setRetryClickListener(retry)
        .build()

    this.setTag(R.id.base_status_manager, statusManager)
    return statusManager
}

/**
 * 切换错误状态
 */
fun View.showError() {
    this.getTag(R.id.base_status_manager)?.apply {
        if (this is StatusManager) {
            this.showError()
        }
    }
}

/**
 * 切换空数据状态
 */
fun View.showEmpty(): StatusManager.ViewHelper? {
    this.getTag(R.id.base_status_manager)?.apply {
        if (this is StatusManager) {
            return this.showEmpty()
        }
    }
    return null
}

/**
 * 切换内容显示状态
 */
fun View.showContent() {
    this.getTag(R.id.base_status_manager)?.apply {
        if (this is StatusManager) {
            this.showContent()
        }
    }
}

/**
 * 切换加载状态
 */
fun View.showLoading() {
    this.getTag(R.id.base_status_manager)?.apply {
        if (this is StatusManager) {
            this.showLoading()
        }
    }
}

/**
 * 当前状态
 */
@StatusManager.ViewState
fun View.getStatus(): Int {
    this.getTag(R.id.base_status_manager)?.apply {
        if (this is StatusManager) {
            return this.currentStatus
        }
    }
    return StatusManager.VIEW_STATUS_CONTENT
}

/**
 * 获取状态管理器
 */
fun View.getStatusManager(): StatusManager? {
    this.getTag(R.id.base_status_manager)?.apply {
        if (this is StatusManager) {
            return this
        }
    }
    return null
}
