package cn.leo.base.arouter.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

import cn.leo.base.arouter.PagesConfig;
import cn.leo.frame.log.Logger;

/**
 * @author : ling luo
 * @date : 2019-04-19
 */
@Interceptor(priority = 8, name = "登录检查拦截器")
public class LoginInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        //检查用户是否已经登录
        if (postcard.getExtra() == PagesConfig.NEED_LOGIN) {
            //页面需要登录后才能进入，这里跳转到登录页
            /*boolean isLogin = false;
            if (!isLogin) {
                Logger.i("拦截页面，跳转到登录页");
                ARouter.getInstance().build(Pages_my.MY_PAGE_LOGIN).navigation();
                callback.onInterrupt(null);
                return;
            }*/
            if(!postcard.isGreenChannel()){

            }
        }
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
        Logger.i("登录检查拦截器加载成功");

    }
}
