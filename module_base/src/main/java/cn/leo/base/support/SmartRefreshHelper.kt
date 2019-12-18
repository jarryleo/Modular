package cn.leo.base.support

import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import cn.leo.base.R
import cn.leo.base.bean.BaseListBean
import cn.leo.frame.network.exceptions.ApiException
import cn.leo.frame.ui.StatusManager
import cn.leo.frame.utils.toast
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-11-29
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class SmartRefreshHelper<T : Any>(private val startPage: Int = 1) :
    ReadOnlyProperty<SmartRefreshHelper.IView<T>, SmartRefreshHelper<T>> {
    private lateinit var mView: IView<T>
    private lateinit var model: ISource<T>
    private lateinit var mSmartRefresh: SmartRefreshLayout
    var onRefreshListener: (() -> Unit)? = null
    var onLoadMoreListener: (() -> Unit)? = null
    //当前页码
    private var mPage: Int = startPage

    override fun getValue(thisRef: IView<T>, property: KProperty<*>): SmartRefreshHelper<T> {
        return SmartRefreshHelper<T>().apply {
            mView = thisRef
            model = mView.getModel()
            mSmartRefresh = mView.getSmartRefresh()
            init()
        }
    }

    private fun init() {
        mSmartRefresh.setOnRefreshListener {
            refresh()
            onRefreshListener?.invoke()
        }

        mSmartRefresh.setOnLoadMoreListener {
            loadMore()
            onLoadMoreListener?.invoke()
        }

        model.observeList(
            mView,
            successCallback = {
                onLoadDataSuccess(it)
            },
            failedCallback = {
                onLoadDataFailed(it)
            }
        )
    }

    /**
     * 配置状态页
     */
    fun statusConfig(
        @LayoutRes loadingRes: Int = R.layout.base_status_loading,
        @LayoutRes emptyRes: Int = R.layout.base_status_empty,
        @LayoutRes errorRes: Int = R.layout.base_status_error
    ) {
        mSmartRefresh.statusConfig(loadingRes, emptyRes, errorRes) { _, _ ->
            getData()
        }
    }

    fun getData() {
        mSmartRefresh.showLoading()
        refresh()
    }

    private fun refresh() {
        mPage = startPage
        model.requestList(mPage)
    }

    private fun loadMore() {
        model.requestList(++mPage)
    }

    private fun isRefresh() = mPage == startPage

    private fun onLoadDataSuccess(data: BaseListBean<T>) {
        val count = data.list?.size ?: 0
        if (data.list == null || count == 0) {
            //显示空页面
            if (isRefresh()) {
                mView.onShowEmpty(mSmartRefresh.showEmpty())
            }
        } else {
            //显示成功页面
            mSmartRefresh.showContent()
            if (isRefresh()) {
                mView.getAdapter().replaceData(data.list)
            } else {
                mView.getAdapter().addData(data.list)
            }
            mSmartRefresh.setNoMoreData(data.hasNextPage())
        }
        mSmartRefresh.finishRefresh(true)
    }

    private fun onLoadDataFailed(exception: ApiException) {
        if (isRefresh()) {
            mSmartRefresh.finishRefresh(false)
            //显示错误页面
            mSmartRefresh.showError()
        } else {
            mSmartRefresh.finishLoadMore(false)
        }
        exception.msg?.let {
            toast(it)
        }
    }

    interface IView<T> : LifecycleOwner {
        fun getAdapter(): IAdapter<T>
        fun getModel(): ISource<T>
        fun getSmartRefresh(): SmartRefreshLayout
        fun onShowEmpty(helper: StatusManager.ViewHelper?) {}
    }

    interface ISource<T> {
        fun requestList(page: Int)
        fun observeList(
            lifecycleOwner: LifecycleOwner,
            failedCallback: (apiException: ApiException) -> Unit,
            successCallback: (data: BaseListBean<T>) -> Unit
        )
    }

    interface IAdapter<T> {
        fun addData(data: List<T>)
        fun replaceData(data: List<T>)
    }
}