package com.moxiaowei.interceptor;

public interface InterceptorHandle {

    public boolean preHandle();

    public void afterComplete();
}
