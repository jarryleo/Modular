package cn.leo.base.support

import cn.leo.base.bean.BaseBean
import cn.leo.base.bean.BaseListBean
import cn.leo.frame.network.MLiveData
import cn.leo.frame.network.exceptions.ApiException
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-11-29
 */

class SmartRefreshHelper<T : Any>(
    private val model: ISource<T>,
    private val startPage: Int = 1
) :
    ReadOnlyProperty<SmartRefreshHelper.IView<T>, SmartRefreshHelper<T>> {
    private lateinit var mView: IView<T>

    //当前页码
    private var mPage: Int = startPage

    override fun getValue(thisRef: IView<T>, property: KProperty<*>): SmartRefreshHelper<T> {
        return SmartRefreshHelper(model).apply { mView = thisRef }
    }

    private val mSmartRefresh by lazy { mView.getSmartRefresh() }

    init {
        mSmartRefresh.setOnRefreshListener {
            refresh()
        }

        mSmartRefresh.setOnLoadMoreListener {
            loadMore()
        }

        model.observeList {
            success {
                onLoadDataSuccess(it.data)
            }
            failed {
                onLoadDataFailed(it)
            }
        }
    }

    private fun refresh() {
        model.requestList(mPage)
    }

    private fun loadMore() {
        model.requestList(++mPage)
    }

    fun isRefresh() = mPage == startPage

    private fun onLoadDataSuccess(data: BaseListBean<T>) {
        val count = data.list?.size ?: 0
        if (data.list == null || count == 0) {
            //显示空页面 todo
        } else {
            //显示成功页面 todo
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
            //显示错误页面 todo
        } else {
            mSmartRefresh.finishLoadMore(false)
        }
    }

    interface IView<T> {
        fun getAdapter(): IAdapter<T>
        fun getSmartRefresh(): RefreshLayout
    }

    interface ISource<T> {
        fun requestList(page: Int)
        fun observeList(result: (MLiveData.Result<BaseBean<BaseListBean<T>>>).() -> Unit = {})
    }

    interface IAdapter<T> {
        fun addData(data: List<T>)
        fun replaceData(data: List<T>)
    }
}