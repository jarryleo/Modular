package cn.leo.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import cn.leo.base.R

/**
 * @author : ling luo
 * @date : 2019-12-04
 */
abstract class SuperActionBarFragment : SuperFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!hasActionBar()) {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
        if (getLayoutResId() == -1) {
            return createView()
        }
        val view =
            layoutInflater.inflate(R.layout.base_super_action_bar_fragment, container, false)
        val viewStub = view.findViewById<ViewStub>(R.id.viewStub)
        viewStub.layoutResource = getLayoutResId()
        viewStub.visibility = View.VISIBLE
        return view
    }

    protected open fun hasActionBar(): Boolean {
        return false
    }
}