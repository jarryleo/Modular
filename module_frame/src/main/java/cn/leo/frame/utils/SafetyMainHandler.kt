package cn.leo.frame.utils

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author : Jarry Leo
 * @date : 2019/2/26 10:54
 */
class SafetyMainHandler : Handler, LifecycleObserver {
    constructor(activityOrFragment: LifecycleOwner) : super(Looper.getMainLooper()) {
        bindLifecycle(activityOrFragment)
    }

    constructor(activityOrFragment: LifecycleOwner, callback: Callback?) : super(
        Looper.getMainLooper(),
        callback
    ) {
        bindLifecycle(activityOrFragment)
    }

    private fun bindLifecycle(activity: LifecycleOwner) {
        activity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeCallbacksAndMessages(null)
    }
}