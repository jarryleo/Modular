package cn.leo.frame.utils

import java.io.File


/**
 * @author : ling luo
 * @date : 2019-07-05
 */

object FileUtilKt {
    fun saveFile(file: File, text: String) {
        file.writeText(text)
    }

    fun getOutFile(): File {
        return File(FileUtil.getExternalCacheDir()!!.absolutePath,
                "Crash_Log ${System.currentTimeMillis().toDate("yyyy-MM-dd HH:mm:ss")}.html")
    }
}