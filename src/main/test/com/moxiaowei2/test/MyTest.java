package com.moxiaowei2.test;

import com.moxiaowei.MySpring;

public class MyTest {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        MySpring mySpring = new MySpring(MyTest.class);
        Table bean = mySpring.getBean(Table.class);
        bean.save();
    }

}
