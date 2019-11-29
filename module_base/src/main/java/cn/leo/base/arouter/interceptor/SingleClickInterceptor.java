package cn.leo.base.arouter.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

import cn.leo.frame.log.Logger;

@Interceptor(priority = 9, name = "点击多次拦截器")
public class SingleClickInterceptor implements IInterceptor {

    private static final long mParseTime = 800;

    private String mPagePath = "";
    private Long mClickTime = 0L;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        String path = postcard.getPath();
        long timeMillis = System.currentTimeMillis();
        if(mPagePath.equals(path) && (timeMillis - mClickTime) <= mParseTime){
            Logger.d("SingleClickInterceptor","点击太快了,页面被拦截了");
            callback.onInterrupt(null);
            return;
        }
        mClickTime = timeMillis;
        mPagePath = path;
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
        Logger.d("初始化点击多次拦截器");
    }
}
