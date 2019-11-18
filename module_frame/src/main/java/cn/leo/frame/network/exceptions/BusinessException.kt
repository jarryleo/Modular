package cn.leo.frame.network.exceptions

/**
 * @author : ling luo
 * @date : 2019-04-26
 */
class BusinessException : Exception {
    var code: Int = 0
    var msg: String = ""

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(msg: String) : super(msg) {
        this.msg = msg
    }

    constructor(code: Int, msg: String)
            : super(msg) {
        this.code = code
        this.msg = msg
    }
}
