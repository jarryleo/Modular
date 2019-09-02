package cn.leo.frame.utils

import com.alibaba.android.arouter.launcher.ARouter
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author : ling luo
 * @date : 2019-05-10
 */

//转换为多少万
fun Long.toShortCount(): String {
    var count = this.toString()
    if (this > 9999) {
        count = BigDecimal(this)
                .divide(BigDecimal(10000), 1, BigDecimal.ROUND_DOWN)
                .toString()
                .plus("万")
    }
    return count
}

//转换成百分比
fun Long.toPercent(sum: Long): String {
    if (sum == 0L || this < 0) {
        return "0%"
    }
    if (this > sum) {
        return "100%"
    }
    return BigDecimal(this)
            .multiply(BigDecimal(100))
            .divide(BigDecimal(sum), 0, BigDecimal.ROUND_HALF_UP)
            .toString().plus("%")
}

fun Long.toPercentInt(sum: Long): Int {
    if (sum == 0L) {
        return 0
    }
    return BigDecimal(this)
            .multiply(BigDecimal(100))
            .divide(BigDecimal(sum), 0, BigDecimal.ROUND_HALF_UP)
            .toInt()
}

//转换成时分秒
fun Long.toDuration(): String {
    var time = ""
    val hours = this / (60 * 60)
    val minute = (this - (hours * 60 * 60)) / 60
    val second = this % 60
    if (hours < 10) {
        time += "0"
    }
    time += "$hours:"

    if (minute < 10) {
        time += "0"
    }
    time += "$minute:"
    if (second < 10) {
        time += "0"
    }
    time += second
    return time
}

//转换成日期2019-05-20
fun Long.toDateyyyyMMdd(): String = this.toDate("yyyy-MM-dd")

fun Long.toDateMMddHHmm(): String {
    if (this.isToday()) {
        return this.toDate("今天 HH:mm")
    }
    return this.toDate("MM-dd HH:mm")
}

//评论时间，刚刚，几分钟前，几小时前，几天前
fun Long.toCommentDate(): String {
    val now = System.currentTimeMillis()
    val diff = (now - this) / 1000
    return when (diff) {
        in 0L until 60L -> "刚刚"
        in 60L until 60 * 60L -> "${diff / 60L}" + "分钟前"
        in 60 * 60L until 60 * 60 * 24L -> "${diff / 60L / 60L}" + "小时前"
        in 60 * 60 * 24L until 60 * 60 * 24L * 3 -> "${diff / 60L / 60L / 24L}" + "天前"
        else -> toDateMMddHHmm()
    }
}

//判断时间戳是不是今天
fun Long.isToday(): Boolean {
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    val todayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    calendar.time = Date(this)
    val targetYear = calendar.get(Calendar.YEAR)
    val targetDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    return todayYear == targetYear && todayOfYear == targetDayOfYear
}

//判断时间戳是不是今年且不是今天
fun Long.isThisYearAndNotToday(): Boolean {
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    calendar.time = Date(this)
    val targetYear = calendar.get(Calendar.YEAR)
    return !this.isToday() && todayYear == targetYear
}

//判断时间戳是不是往年
fun Long.isPassYear(): Boolean {
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    calendar.time = Date(this)
    val targetYear = calendar.get(Calendar.YEAR)
    return todayYear > targetYear
}

//转换成指定日志格式
fun Long.toDate(format: String): String = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

fun String.getFileName(): String {
    val start = this.lastIndexOf("/")
    if (start < 0) {
        return ""
    }
    return this.substring(start + 1)
}

fun String.jump() {
    ARouter.getInstance().build(this).navigation()
}
