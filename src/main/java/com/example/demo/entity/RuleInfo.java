package com.example.demo.entity;

import lombok.Data;

/**
 * @author liujipeng
 * @date 2020/8/20 17:26
 * @mail liujipeng@cloud-er.com
 * @desc ...
 */
@Data
public class RuleInfo {

    /**
     * 规则
     */
    private Long id;

    /**
     * 场景
     */
    private Long sceneId;

    /**
     * 规则内容，既drl文件内容
     */
    private String content;

    //封装 content 的入参
    private String[] arg;


}
