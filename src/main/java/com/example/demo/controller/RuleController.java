package com.example.demo.controller;

import com.example.demo.config.KieSessionHelper;
import com.example.demo.config.RuleLoader;
import com.example.demo.entity.Person;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujipeng
 * @date 2020/8/20 17:31
 * @mail liujipeng@cloud-er.com
 * @desc ...
 */
@RestController
public class RuleController {

    @Autowired
    private RuleLoader ruleLoader;

    @Autowired
    private KieSessionHelper kieSessionHelper;

    @GetMapping("/")
    public String index() {
        System.out.println("index");
        return "success";
    }

    /**
     * 重新加载所有规则
     */
    @GetMapping("reload")
    public String reload() {
        System.out.println("reload all");
        ruleLoader.reloadAll();
        return "success";
    }

    /**
     * 重新加载给定场景下的规则
     *
     * @param sceneId 场景ID
     */
    @GetMapping("reload/{sceneId}")
    public String reload(@PathVariable("sceneId") Long sceneId) {
        System.out.println("reload scene:" + sceneId);
        ruleLoader.reload(sceneId);
        return "success";
    }

    /**
     * 触发给定场景规则
     *
     * @param sceneId 场景ID
     */
    @GetMapping("fire/{sceneId}")
    public String fire(@PathVariable("sceneId") Long sceneId) {
        System.out.println("fire scene:" + sceneId);
        KieSession kieSession = kieSessionHelper.getKieSessionBySceneId(sceneId);
        Person person = new Person(35,"zhangsan");
        kieSession.insert(person);

        //执行 drl 里面的定义方法
     /*   QueryResults results = kieSession.getQueryResults("people");
        System.out.println("we have " + results.size() + " people over the age  of 30");

        for (QueryResultsRow row : results) {
            Person person1 = (Person) row.get("person");
            System.out.println(person1.getName() + "\n");
        }*/

        kieSession.fireAllRules();
        kieSession.dispose();
        return "success";
    }

}
