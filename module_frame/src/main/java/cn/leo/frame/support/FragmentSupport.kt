package cn.leo.frame.support

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


/**
 * Fragment 展示扩展
 * @author : ling luo
 * @date : 2019-05-28
 */


/**
 * Activity show
 */
inline fun <reified T : Fragment> FragmentActivity.showFragment(
    replaceViewId: Int, init: (T).() -> Unit = {}
): T {
    val sfm = supportFragmentManager
    val transaction = sfm.beginTransaction()
    var fragment = sfm.findFragmentByTag(T::class.java.name)
    if (fragment == null) {
        fragment = T::class.java.newInstance()
        transaction.add(replaceViewId, fragment, T::class.java.name)
    }
    sfm.fragments.filter { it != fragment }.forEach { transaction.hide(it) }
    transaction.show(fragment!!)
    transaction.commitAllowingStateLoss()
    sfm.executePendingTransactions()
    init(fragment as T)
    return fragment
}

inline fun <reified T : Fragment> FragmentActivity.getFragment(
    init: (T)?.() -> Unit = {}
): T? {
    val fragment = supportFragmentManager.findFragmentByTag(T::class.java.name)
    init(fragment as T?)
    return fragment
}

/**
 * Activity show
 */
inline fun FragmentActivity.showFragment(
    fragment: Fragment,
    replaceViewId: Int
) {
    val sfm = supportFragmentManager
    val transaction = sfm.beginTransaction()
    if (!fragment.isAdded) {
        transaction.add(replaceViewId, fragment, fragment.javaClass.name)
    }
    sfm.fragments.filter { it != fragment }.forEach { transaction.hide(it) }
    transaction.show(fragment)
    transaction.commitAllowingStateLoss()
    sfm.executePendingTransactions()
}

/**
 * Fragment show
 */
inline fun <reified T : Fragment> Fragment.showFragment(
    replaceViewId: Int, init: (T).() -> Unit = {}
): T {
    val sfm = childFragmentManager
    val transaction = sfm.beginTransaction()
    var fragment = sfm.findFragmentByTag(T::class.java.name)
    if (fragment == null) {
        fragment = T::class.java.newInstance()
        transaction.add(replaceViewId, fragment, T::class.java.name)
    }
    sfm.fragments.filter { it != fragment }.forEach { transaction.hide(it) }
    transaction.show(fragment!!)
    transaction.commitAllowingStateLoss()
    sfm.executePendingTransactions()
    init(fragment as T)
    return fragment
}

/**
 * Fragment show
 */
inline fun Fragment.showFragment(
    fragment: Fragment,
    replaceViewId: Int
) {
    val sfm = childFragmentManager
    val transaction = sfm.beginTransaction()
    if (!fragment.isAdded) {
        transaction.add(replaceViewId, fragment, fragment.javaClass.name)
    }
    sfm.fragments.filter { it != fragment }.forEach { transaction.hide(it) }
    transaction.show(fragment)
    transaction.commitAllowingStateLoss()
    sfm.executePendingTransactions()
}



