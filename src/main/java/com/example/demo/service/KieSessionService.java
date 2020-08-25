package com.example.demo.service;

import com.example.demo.config.RuleLoader;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liujipeng
 * @date 2020/8/25 17:42
 * @mail liujipeng@cloud-er.com
 * @desc ...
 */
@Service
public class KieSessionService {

    @Autowired
    private RuleLoader ruleLoader;

    public KieSession getKieSessionBySceneId(long sceneId) {
        return ruleLoader.getKieContainerBySceneId(sceneId).getKieBase().newKieSession();
    }

}
