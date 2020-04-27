package cn.leo.modular.activity_result

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

/**
 * @author : ling luo
 * @date : 2020/4/24
 */
class PermissionResult : ActivityResultContract<Void, Boolean>() {
    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return true
    }
}