package cn.leo.modular

import cn.leo.frame.ext.io
import cn.leo.frame.ext.merge
import cn.leo.frame.ext.wait
import cn.leo.frame.support.measureTimeMillis
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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

    @Test
    fun test1() {
        io {
            val time = measureTimeMillis {
                val a1 = async {
                    delay(1000)
                    1
                }
                val a2 = async {
                    delay(2000)
                    2
                }
                val c = merge(a1, a2)
                println(c)
            }
            println(time)
        }
        /*上面的协程代码并不会阻塞掉线程，所以我们这里让线程睡4秒，保证线程的存活，在实际的Android开发中无需这么做*/
        Thread.sleep(4000)
    }
}
