package cn.leo.modular

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith

/**
 * @author : ling luo
 * @date : 2019-12-23
 */
@RunWith(AndroidJUnit4::class)
class TestFragmentTest {

    @Rule
    val activity = ActivityTestRule<Main3Activity>(Main3Activity::class.java)

    @Test
    fun getLayoutResId() {

    }
}