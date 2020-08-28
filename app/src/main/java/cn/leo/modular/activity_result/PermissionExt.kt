package cn.leo.modular.activity_result

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2020/7/1
 */

class PermissionExt : ReadOnlyProperty<LifecycleOwner, LiveData<Boolean>>, LifecycleObserver {
    companion object {
        const val KEY = "PermissionLiveData"
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val liveData = MutableLiveData<Boolean>()

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): LiveData<Boolean> {
        thisRef.lifecycle.addObserver(this)
        val activityResultRegistry = thisRef as ActivityResultRegistry
        permissionLauncher = activityResultRegistry.register(
            PermissionLiveData.KEY,
            ActivityResultContracts.RequestPermission()
        ) {
            Log.d("PermissionExt", "requestPermission result =  $it")
            liveData.postValue(it)
        }
        return liveData
    }

    fun requestPermission(permission: String) {
        permissionLauncher.launch(permission)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        permissionLauncher.unregister()
    }

}