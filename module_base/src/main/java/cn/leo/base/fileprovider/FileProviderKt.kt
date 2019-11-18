package cn.leo.base.fileprovider

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import cn.leo.frame.MFrame
import java.io.File

/**
 * @author : ling luo
 * @date : 2019-09-04
 */

fun File.getUri(): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        FileProvider.getUriForFile(
            MFrame.context,
            "${MFrame.context.packageName}.FileProvider",this)

    }else{
        Uri.fromFile(this)
    }
}

fun Intent.grantUriPermission(uri:Uri){
    //加入uri权限
    val infoList =
        MFrame.context.packageManager.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY)
    for (resolveInfo in infoList) {
        val packageName = resolveInfo.activityInfo.packageName
        MFrame.context.grantUriPermission(packageName, uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}