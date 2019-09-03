package cn.leo.frame.log

/**
 * @author : ling luo
 * @date : 2019-09-02
 */

fun String.toLogD(tag:String = "LoggerKt"){
    Logger.d(tag,this)
}

fun String.toLogI(tag:String = "LoggerKt"){
    Logger.i(tag,this)
}

fun String.toLogE(tag:String = "LoggerKt"){
    Logger.e(tag,this)
}

fun String.toLogW(tag:String = "LoggerKt"){
    Logger.w(tag,this)
}

fun String.toLogV(tag:String = "LoggerKt"){
    Logger.v(tag,this)
}