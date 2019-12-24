package cn.leo.modular

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

/**
 * @author : ling luo
 * @date : 2019-12-23
 */
class MainActivityTest {
    @Rule
    val mainActivity = ActivityTestRule<MainActivity>(MainActivity::class.java)
    @Before
    fun setUp() {
        mainActivity.activity.title = "测试标题"
    }

    @Test
    fun getLayoutRes() {
    }

    @Test
    fun onInitView() {
        onView(withId(R.id.base_actionbar_title)).check(matches(withText("测试标题")))
    }

    @Test
    fun onInitObserve() {
    }

    @Test
    fun onBackPressed() {
    }
}