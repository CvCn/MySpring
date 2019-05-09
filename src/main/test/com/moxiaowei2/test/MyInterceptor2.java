package com.moxiaowei2.test;

import com.moxiaowei.annotation.Interceptor;
import com.moxiaowei.interceptor.InterceptorHandle;

@Interceptor
public class MyInterceptor2 implements InterceptorHandle {

    @Override
    public boolean preHandle() {
        return false;
    }

    @Override
    public void afterComplete() {
    }
}
