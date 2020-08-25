package com.example.demo.entity;

import lombok.Data;

/**
 * @author liujipeng
 * @date 2020/8/20 17:46
 * @mail liujipeng@cloud-er.com
 * @desc ...测试用数据
 */
@Data
public class Person {

    private Integer age;

    private String name;

    private String desc;

    public Person(Integer age, String name) {
        this.age = age;
        this.name = name;
    }
}
