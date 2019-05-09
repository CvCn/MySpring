package com.moxiaowei2.test;

import com.moxiaowei.annotation.Interceptor;
import com.moxiaowei.annotation.Path;
import com.moxiaowei.annotation.Patterns;
import com.moxiaowei.interceptor.InterceptorHandle;

@Interceptor
@Patterns(
        paths = @Path({
                "com.moxiaowei2.test.User"
        })
)
public class MyInterceptor implements InterceptorHandle {

    @Override
    public boolean preHandle() {
        return true;
    }

    @Override
    public void afterComplete() {
    }
}
