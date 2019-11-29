package cn.leo.base.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import cn.leo.frame.log.toLogD
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Fragment 基类 （可选择性懒加载）
 * @author Max
 * @date 2019-09-15.
 */
abstract class SuperFragment : Fragment() {


    @Volatile
    private var mIsFirstVisible = true

    @Volatile
    private var isViewCreated = false

    @Volatile
    private var currentVisibleState = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (getLayoutResId() != -1) {
            layoutInflater.inflate(getLayoutResId(), container, false)
        } else {
            createView()
        }
    }

    /**
     * 初始化方法，只会调用一次，(若开启懒加载后，只有当一次可见时才会回调此方法，若关闭懒加载则按正常流程)
     */
    protected abstract fun onInitialize()

    /**
     * 初始化方法之后
     */
    protected fun onInitializeAfter() {

    }

    /**
     * 创建内容视图
     */
    protected open fun createView(): View? {
        return TextView(context).apply {
            setTextColor(Color.RED)
            text = "Fragment view not initialize!"
        }
    }

    /**
     * 创建内容视图 根据布局ID
     */
    @LayoutRes
    protected open fun getLayoutResId(): Int {
        return -1
    }

    /**
     * 是否懒加载
     *
     * @return
     */
    protected open fun isLazyLoad(): Boolean {
        return true
    }

    @CallSuper
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        // 对于默认 tab 和 间隔 checked tab 需要等到 isViewCreated = true 后才可以通过此通知用户可见
        // 这种情况下第一次可见不是在这里通知 因为 isViewCreated = false 成立,等从别的界面回到这里后会使用 onFragmentResume 通知可见
        // 对于非默认 tab mIsFirstVisible = true 会一直保持到选择则这个 tab 的时候，因为在 onActivityCreated 会返回 false
        if (isViewCreated) {
            if (isVisibleToUser && !currentVisibleState) {
                dispatchUserVisibleHint(true)
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false)
            }
        }
    }

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isViewCreated = true
        // !isHidden() 默认为 true  在调用 hide show 的时候可以使用
        if (!isLazyLoad()) {
            dispatchUserVisibleHint(true)
        } else if (!isHidden && userVisibleHint) {
            dispatchUserVisibleHint(true)
        }
    }

    @CallSuper
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden) {
            dispatchUserVisibleHint(false)
        } else {
            dispatchUserVisibleHint(true)
        }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        if (!mIsFirstVisible) {
            if (!isHidden && !currentVisibleState && userVisibleHint) {
                dispatchUserVisibleHint(true)
            }
        }
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        // 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
        // 子 fragment 走到这里的时候自身又会调用一遍 ？
        if (currentVisibleState && userVisibleHint) {
            dispatchUserVisibleHint(false)
        }
    }

    /**
     * 统一处理 显示隐藏
     *
     * @param visible
     */
    private fun dispatchUserVisibleHint(visible: Boolean) {
        if (!isViewCreated) {
            return
        }

        //当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment getUserVisibleHint = true
        //但当父 fragment 不可见所以 currentVisibleState = false 直接 return 掉
        // 这里限制则可以限制多层嵌套的时候子 Fragment 的分发
        if (visible && isParentInvisible()) return

        //此处是对子 Fragment 不可见的限制，因为 子 Fragment 先于父 Fragment回调本方法 currentVisibleState 置位 false
        // 当父 dispatchChildVisibleState 的时候第二次回调本方法 visible = false 所以此处 visible 将直接返回
        if (currentVisibleState == visible) {
            return
        }

        currentVisibleState = visible

        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                onFragmentFirstVisible()
            }
            onFragmentResume()
            dispatchChildVisibleState(true)
        } else {
            dispatchChildVisibleState(false)
            onFragmentPause()
        }
    }

    /**
     * 用于分发可见时间的时候父获取 fragment 是否隐藏
     *
     * @return true fragment 不可见， false 父 fragment 可见
     */
    private fun isParentInvisible(): Boolean {
        val parentFragment = parentFragment
        return if (parentFragment is SuperFragment) {
            !parentFragment.isSupportVisible()
        } else {
            false
        }
    }

    protected fun isSupportVisible(): Boolean {
        return currentVisibleState
    }

    /**
     * 当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment 的唯一或者嵌套 VP 的第一 fragment 时 getUserVisibleHint = true
     * 但是由于父 Fragment 还进入可见状态所以自身也是不可见的， 这个方法可以存在是因为庆幸的是 父 fragment 的生命周期回调总是先于子 Fragment
     * 所以在父 fragment 设置完成当前不可见状态后，需要通知子 Fragment 我不可见，你也不可见，
     *
     *
     * 因为 dispatchUserVisibleHint 中判断了 isParentInvisible 所以当 子 fragment 走到了 onActivityCreated 的时候直接 return 掉了
     *
     *
     * 当真正的外部 Fragment 可见的时候，走 setVisibleHint (VP 中)或者 onActivityCreated (hide show) 的时候
     * 从对应的生命周期入口调用 dispatchChildVisibleState 通知子 Fragment 可见状态
     *
     * @param visible
     */
    private fun dispatchChildVisibleState(visible: Boolean) {
        val childFragmentManager = childFragmentManager
        val fragments = childFragmentManager.fragments
        if (fragments.isNotEmpty()) {
            for (child in fragments) {
                if (child is SuperFragment && !child.isHidden() && child.getUserVisibleHint()) {
                    child.dispatchUserVisibleHint(visible)
                }
            }
        }
    }

    private fun onFragmentFirstVisible() {
        "${javaClass.simpleName} + 对用户第一次可见".toLogD()
        onInitialize()
        onInitializeAfter()
    }

    open fun onFragmentResume() {
        "${javaClass.simpleName} + 对用户可见".toLogD()
    }

    open fun onFragmentPause() {
        "${javaClass.simpleName} + 对用户不可见".toLogD()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        mIsFirstVisible = true
    }

}