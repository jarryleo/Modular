package cn.leo.modular

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    //private val value = AtomicInteger(1)
    var a = 1
        get() = field++

    @Test
    fun test() {
        //assertEquals(4, 2 + 2)
        a = 1
        if (a == 1 && a == 2 && a == 3) {
            print("Success")
        } else {
            print("Failed")
        }
    }
}
