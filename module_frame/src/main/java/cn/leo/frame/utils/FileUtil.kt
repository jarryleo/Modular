package cn.leo.frame.utils

import android.os.Environment
import android.os.StatFs
import cn.leo.frame.MFrame
import java.io.*

/**
 * @author : ling luo
 * @date : 2019-08-31
 */

object FileUtil {
    /**
     * 获取外部缓存路径
     *
     * @return
     */
    fun getExternalCacheDir(): File? {
        return MFrame.context.externalCacheDir
    }

    /**
     * 获取内部缓存路径
     *
     * @return
     */
    fun getCacheDir(): File? {
        return MFrame.context.cacheDir
    }


    /**
     * 获取内部文件路径
     *
     * @return
     */
    fun getFileDir(): File {
        return MFrame.context.filesDir
    }

    /**
     * 获取下载目录
     *
     * @return
     */
    fun getDownloadDir(): File? {
        return MFrame.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    }

    /**
     * 获取图片目录
     *
     * @return
     */
    fun getPicDir(): File? {
        return MFrame.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }


    /**
     * 删除指定路经文件
     * @param path 文件路经
     */
    fun delFile(path: String): Boolean {
        val file = File(path)
        var del = false
        if (file.exists()) {
            del = file.delete()
        }
        return del
    }


    /**
     * 获取SD卡剩余空间
     */
    fun getSDCardAvailSize(): Long {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            val blockSize = sf.blockSizeLong
            val availCount = sf.availableBlocksLong
            return availCount * blockSize
        }
        return 0L
    }
}