package cn.leo.modular

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.leo.base.arouter.pages.PagesHome
import cn.leo.frame.support.showFragment
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = PagesHome.homeMain3Activity)
class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        showFragment<TestFragment>(R.id.fragmentContainer)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}
