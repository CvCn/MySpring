package com.moxiaowei.scan;

import com.moxiaowei.annotation.Autowired;
import com.moxiaowei.proxy.Proxy;

import java.lang.reflect.Field;
import java.util.HashSet;

public class BeanScan {

    public static HashSet<Object> beanObjs = new HashSet<>();

    public static void scan() throws IllegalAccessException, InstantiationException {
        //扫描bean
        HashSet<Class<?>> classes = ClassScanner.annotationScanBean();

        for(Class<?> cls : classes){
            Object o = cls.newInstance();
            beanObjs.add(o);
        }

        for(Object obj : beanObjs){
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for(Field field : declaredFields){
                if(field.getAnnotation(Autowired.class) != null){
                    for(Object temp_obj : beanObjs){
                        if(temp_obj.equals(obj))
                            continue;
//                        System.out.println(field.getGenericType());
//                        System.out.println(temp_obj.getClass());
                        if(field.getGenericType().equals(temp_obj.getClass())){
                            field.setAccessible(true);
                            Proxy proxy = new Proxy(temp_obj);
                            proxy.init();
                            Object proxyInstance = proxy.getProxyInstance();
                            field.set(obj, proxyInstance);
                            field.setAccessible(false);
                        }
                    }
                }
            }
        }
    }
}
