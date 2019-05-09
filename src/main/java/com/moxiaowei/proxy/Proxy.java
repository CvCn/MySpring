package com.moxiaowei.proxy;

import com.moxiaowei.annotation.ExcludePath;
import com.moxiaowei.annotation.Path;
import com.moxiaowei.annotation.Patterns;
import com.moxiaowei.interceptor.InterceptorHandle;
import com.moxiaowei.scan.ClassScanner;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;

public class Proxy implements MethodInterceptor {
    //维护目标对象
    private Object target;

    public Proxy(Object target) {
        this.target = target;
    }

    //给目标对象创建一个代理对象
    public Object getProxyInstance() {
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target.getClass());
        //3.设置回调函数
        en.setCallback(this);
        //4.创建子类(代理对象)
        return en.create();

    }

    /**
     * 代理对象方法的执行要经过这个
     * @param obj
     * @param method
     * @param args
     * @param proxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (!interceptorPre(target.getClass())) return null;

//        System.out.println("开始执行：" + method.getName());
        //执行目标对象的方法
        Object returnValue = method.invoke(target, args);

        interceptorAfter(target.getClass());

//        System.out.println("执行结束：" + method.getName());

        return returnValue;
    }

    //拦截器对象
    private Set<InterceptorHandle> interObjs = new LinkedHashSet<>();

    //初始化代理
    public void init() throws IllegalAccessException, InstantiationException {
        //扫描拦截器
        TreeMap<Integer, Class<?>> stringClassTreeMap = ClassScanner.annotationScan();
        Set<Integer> keys = stringClassTreeMap.keySet();
        for (Integer key : keys) {
            Object o = stringClassTreeMap.get(key).newInstance();
            if (o instanceof InterceptorHandle) {
                InterceptorHandle interceptorHandle = (InterceptorHandle) o;
                interObjs.add(interceptorHandle);
            }
        }

    }

    //前置拦截器
    //在方法执行前
    private boolean interceptorPre(Class cls) {
        for (InterceptorHandle in : interObjs) {
            if(patt(in, cls)){
                System.out.println("执行前置拦截器：" + in.getClass().getName());
                if(!in.preHandle()) return  true;
            }
        }
        return true;
    }

    //匹配拦截规则
    private boolean patt(InterceptorHandle interceptorHandle, Class cls) {
        Patterns annotation = interceptorHandle.getClass().getAnnotation(Patterns.class);
        String name = cls.getName();
        if (annotation != null) {
            Path paths = annotation.paths();
            for (String path : paths.value()) {
                if (!path.trim().equals("")) {
                    if (name.equals(path)) {
                        return true;
                    }
                }
            }
            ExcludePath excludePaths = annotation.excludePaths();
            for(String excludePath : excludePaths.value()){
                if (!excludePath.trim().equals("")) {
                    if (name.equals(excludePath)) {
                        return false;
                    }else{
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }

    //后置拦截器
    //在方法执行结束后
    private void interceptorAfter(Class cls) {
        for (InterceptorHandle in : interObjs) {
            if(patt(in, cls)){
                System.out.println("执行后置拦截器：" + in.getClass().getName());
                in.afterComplete();
            }
        }
    }
}
