package cn.leo.frame.log

/**
 * @author : ling luo
 * @date : 2019-09-02
 */

fun String.toLogD(tag: String = "LoggerKt") {
    Logger.d(tag, this)
}

fun String.toLogI(tag: String = "LoggerKt") {
    Logger.i(tag, this)
}

fun String.toLogE(tag: String = "LoggerKt") {
    Logger.e(tag, this)
}

fun String.toLogW(tag: String = "LoggerKt") {
    Logger.w(tag, this)
}

fun String.toLogV(tag: String = "LoggerKt") {
    Logger.v(tag, this)
}

fun Any.logE(log: String, tag: String = "LoggerKt") {
    Logger.e(tag, log)
}

fun Any.logW(log: String, tag: String = "LoggerKt") {
    Logger.w(tag, log)
}

fun Any.logD(log: String, tag: String = "LoggerKt") {
    Logger.d(tag, log)
}

fun Any.logI(log: String, tag: String = "LoggerKt") {
    Logger.i(tag, log)
}

fun Any.logV(log: String, tag: String = "LoggerKt") {
    Logger.v(tag, log)
}