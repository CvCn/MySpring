package com.moxiaowei;

import com.moxiaowei.proxy.Proxy;
import com.moxiaowei.scan.BeanScan;
import com.moxiaowei.scan.ClassScanner;

import java.util.HashSet;

public class MySpring {

    public MySpring(Class cls) throws InstantiationException, IllegalAccessException {
        this.init(cls.getPackage().getName());
    }

    private void init(String basePackage)throws InstantiationException, IllegalAccessException{
        ClassScanner.scan(basePackage);
        BeanScan.scan();
    }

    public <T> T getBean(Class<T> cls){
        Object re = null;
        try{
            HashSet<Object> beanObjs = BeanScan.beanObjs;
            for(Object obj : beanObjs){
                if(obj.getClass().equals(cls)){
                    Proxy proxy = new Proxy(obj);
                    proxy.init();
                    re = proxy.getProxyInstance();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return (T) re;
    }
}
