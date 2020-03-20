package cn.leo.frame.log

/**
 * @author : ling luo
 * @date : 2019-09-02
 */

fun String.toLogD(tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.d(tag, this)
}

fun String.toLogI(tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.i(tag, this)
}

fun String.toLogE(tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.e(tag, this)
}

fun String.toLogW(tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.w(tag, this)
}

fun String.toLogV(tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.v(tag, this)
}

fun Any.logE(log: String, tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.e(tag, log)
}

fun Any.logW(log: String, tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.w(tag, log)
}

fun Any.logD(log: String, tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.d(tag, log)
}

fun Any.logI(log: String, tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.i(tag, log)
}

fun Any.logV(log: String, tag: String = "LoggerKt") {
    cn.leo.z_logger.Logger.v(tag, log)
}