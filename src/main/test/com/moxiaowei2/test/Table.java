package com.moxiaowei2.test;

import com.moxiaowei.annotation.Autowired;
import com.moxiaowei.annotation.Bean;

@Bean
public class Table {

    @Autowired
    private User user;

    public void save(){
        user.save();
        System.out.println("Table finish");
    }
}
