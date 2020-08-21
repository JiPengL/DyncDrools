package com.example.demo.config;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liujipeng
 * @date 2020/8/20 17:30
 * @mail liujipeng@cloud-er.com
 * @desc ...
 */

@Component
public class KieSessionHelper {

    @Autowired
    private RuleLoader ruleLoader;

    /**
     * 获取KieSession
     *
     * @param sceneId 场景ID
     * @return KieSession
     */
    public KieSession getKieSessionBySceneId(long sceneId) {
        return ruleLoader.getKieContainerBySceneId(sceneId).getKieBase().newKieSession();
    }

}
