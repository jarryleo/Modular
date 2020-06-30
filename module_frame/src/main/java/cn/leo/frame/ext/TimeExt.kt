package cn.leo.frame.ext

import java.text.SimpleDateFormat
import java.util.*


/**
 * 时间日期工具类
 * @author : ling luo
 * @date : 2019-05-10
 */


/**
 * 秒数（非时间戳）转换成时分秒
 */
fun Long.secondToHHmmSS(): String {
    val hours = this / (60 * 60)
    val minute = (this - (hours * 60 * 60)) / 60
    val second = this % 60
    return String.format(
        "%d:%2$02d:%3$02d",
        hours,
        minute,
        second
    )
}

/**
 * 毫秒时间转分秒
 * @param isAutoFill 是否自动填充分钟数，如小于10分钟的前面补0  如:01:30
 */
fun Long.toDateMS(isAutoFill: Boolean = true): String {
    val i = this / 1000
    val minute = i / 60
    val second = i % 60
    return String.format(
        if (isAutoFill && minute < 10) {
            "%1\$02d:%2$02d"
        } else {
            "%1\$d:%2$02d"
        },
        minute,
        second
    )
}

fun Long.toDateyyyyMMdd(): String = this.toDate("yyyy-MM-dd")

fun String.parseDateyyyyMMdd(): Long = this.parseDate("yyyy-MM-dd")

fun Long.toDateyyyyMMddHHmm(): String = this.toDate("yyyy-MM-dd HH:mm")

fun Long.toDateMMddHHmm(): String {
    return when {
        this.isToday() -> {
            this.toDate("今天 HH:mm")
        }
        this.isThisYear() -> {
            this.toDate("MM-dd HH:mm")
        }
        else -> {
            this.toDateyyyyMMddHHmm()
        }
    }
}

/**
 * 刚刚，几分钟前，几小时前，几天前，年月日
 */
fun Long.toCommentDate(): String {
    val now = System.currentTimeMillis()
    return when (val diff = (now - this) / 1000) {
        in 0L until 60L -> "刚刚"
        in 60L until 60 * 60L -> "${diff / 60L}" + "分钟前"
        in 60 * 60L until 60 * 60 * 24L -> "${diff / 60L / 60L}" + "小时前"
        in 60 * 60 * 24L until 60 * 60 * 24L * 3 -> "${diff / 60L / 60L / 24L}" + "天前"
        else -> toDateMMddHHmm()
    }
}

/**
 * 刚刚，几分钟前，几小时前，几天前，今年显示月日，不是今年显示年月日
 */
fun Long.toCommentDateToYMD(): String {
    val now = System.currentTimeMillis()
    return when (val diff = (now - this) / 1000) {
        in 0L until 60L -> "刚刚"
        in 60L until 60 * 60L -> "${diff / 60L}" + "分钟前"
        in 60 * 60L until 60 * 60 * 24L -> "${diff / 60L / 60L}" + "小时前"
        in 60 * 60 * 24L until 60 * 60 * 24L * 3 -> "${diff / 60L / 60L / 24L}" + "天前"
        else -> {
            if (isThisYear()) {
                toDate("MM-dd")
            } else {
                toDateyyyyMMdd()
            }
        }
    }
}


/**
 * 判断时间戳是不是今天
 */
fun Long.isToday(): Boolean {
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    val todayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    calendar.time = Date(this)
    val targetYear = calendar.get(Calendar.YEAR)
    val targetDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    return todayYear == targetYear && todayOfYear == targetDayOfYear
}

/**
 * 判断时间戳是不是今年
 */
fun Long.isThisYear(): Boolean {
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    calendar.time = Date(this)
    val targetYear = calendar.get(Calendar.YEAR)
    return todayYear == targetYear
}

/**
 * 判断时间戳是不是今年且不是今天
 */
fun Long.isThisYearAndNotToday(): Boolean {
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    calendar.time = Date(this)
    val targetYear = calendar.get(Calendar.YEAR)
    return !this.isToday() && todayYear == targetYear
}

/**
 * 判断时间戳是不是往年
 */
fun Long.isPassYear(): Boolean {
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    calendar.time = Date(this)
    val targetYear = calendar.get(Calendar.YEAR)
    return todayYear > targetYear
}

/**
 * 转换成指定日期格式
 */
fun Long.toDate(format: String): String =
    SimpleDateFormat(format, Locale.CHINA).apply {
        timeZone = TimeZone.getTimeZone("GMT+8:00")
    }.format(Date(this))

/**
 * 指定日期格式字符串转时间戳
 */
fun String.parseDate(format: String): Long {
    val dateFormat = SimpleDateFormat(format, Locale.CHINA)
    dateFormat.apply {
        timeZone = TimeZone.getTimeZone("GMT+8:00")
    }
    return try {
        dateFormat.parse(this).time
    } catch (e: Exception) {
        0L
    }

}
