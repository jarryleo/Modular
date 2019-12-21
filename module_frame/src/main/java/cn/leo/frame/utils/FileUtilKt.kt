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

}