package cn.leo.base.base

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by Leo on 2018/2/1.
 */

abstract class BaseFragmentVPAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList = mutableListOf<Fragment?>()

    override fun getItem(position: Int): Fragment {
        while (mFragmentList.size <= position) {
            mFragmentList.add(null)
        }
        var fragment = mFragmentList[position]
        if (fragment == null) {
            fragment = createFragment(position)
            mFragmentList[position] = fragment
        }
        return fragment
    }

    fun getFragment(position: Int): Fragment? {
        if (position >= mFragmentList.size) {
            return null
        }
        return mFragmentList[position]
    }

    abstract fun createFragment(position: Int): Fragment

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        initFragment(fragment, position)
        return fragment
    }

    open fun initFragment(fragment: Fragment, position: Int) {

    }


    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}
