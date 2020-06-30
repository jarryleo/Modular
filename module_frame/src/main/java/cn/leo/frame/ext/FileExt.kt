package cn.leo.frame.ext

import java.io.File
import java.io.RandomAccessFile


/**
 * @author : ling luo
 * @date : 2019-07-05
 */

/**
 * 分块大小 1M
 */
const val CHUNK_LENGTH = 1 * 1024 * 1024

/**
 * 从指定位置获取指定长度的byte
 *
 * @param offset    起始位置
 * @param blockSize 读取的块的长度
 * @return
 */
inline fun File.getBlock(offset: Long, blockSize: Int = CHUNK_LENGTH): ByteArray? {
    val result = ByteArray(blockSize)
    var randomAccessFile: RandomAccessFile? = null
    try {
        randomAccessFile = RandomAccessFile(this, "r")
        randomAccessFile.seek(offset)
        return when (val readLength = randomAccessFile.read(result)) {
            -1 -> {
                null
            }
            blockSize -> {
                result
            }
            else -> {
                val tmpByte = ByteArray(readLength)
                System.arraycopy(result, 0, tmpByte, 0, readLength)
                tmpByte
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            randomAccessFile?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return null
}
