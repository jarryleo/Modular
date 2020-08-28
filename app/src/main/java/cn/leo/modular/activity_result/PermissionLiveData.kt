package cn.leo.modular.activity_result

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData

/**
 * @author : ling luo
 * @date : 2020/7/1
 */
class PermissionLiveData(
    private val activityResultRegistry: ActivityResultRegistry
) : MutableLiveData<Map<String, Boolean>>() {
    companion object {
        const val KEY = "PermissionLiveData"
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<out String>>

    override fun onActive() {
        super.onActive()
        Log.d("PermissionLiveData", "onActive: ")
        permissionLauncher = activityResultRegistry.register(
            KEY,
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            Log.d("PermissionLiveData", "requestPermission result =  $it")
            postValue(it)
        }
    }

    fun requestPermission(vararg permissions: String) {
        permissionLauncher.launch(permissions)
    }

    override fun onInactive() {
        super.onInactive()
        Log.d("PermissionLiveData", "onInactive: ")
        permissionLauncher.unregister()
    }
}