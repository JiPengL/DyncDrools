package com.example.demo.config;

import com.example.demo.entity.RuleInfo;
import com.example.demo.service.RuleInfoService;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author liujipeng
 * @date 2020/8/20 17:28
 * @mail liujipeng@cloud-er.com
 * @desc ...
 */

@Component
public class RuleLoader  implements InitializingBean {

    private final ConcurrentMap<String, KieContainer> kieContainerMap = new ConcurrentHashMap<>();

    @Autowired
    private RuleInfoService ruleInfoService;

    @Override
    public void afterPropertiesSet() throws Exception {
        reloadAll();
    }

    private String buildKcontainerName(long sceneId) {
        return "kcontainer_" + sceneId;
    }


    private String buildKbaseName(long sceneId) {
        return "kbase_" + sceneId;
    }


    private String buildKsessionName(long sceneId) {
        return "ksession_" + sceneId;
    }

    public KieContainer getKieContainerBySceneId(long sceneId) {
        return kieContainerMap.get(buildKcontainerName(sceneId));
    }

    /**
     * 重新加载所有规则
     */
    public void reloadAll() {
        Map<Long, List<RuleInfo>> sceneId2RuleInfoListMap = ruleInfoService.getRuleInfoListMap();
        for (Map.Entry<Long, List<RuleInfo>> entry : sceneId2RuleInfoListMap.entrySet()) {
            long sceneId = entry.getKey();
            reload(sceneId, entry.getValue());
        }
    }

    /**
     * 重新加载给定场景下的规则
     *
     * @param sceneId 场景ID
     */
    public void reload(Long sceneId) {
        List<RuleInfo> ruleInfos = ruleInfoService.getRuleInfoListBySceneId(sceneId);
        reload(sceneId, ruleInfos);
    }

    /**
     * 重新加载给定场景给定规则列表，对应一个kmodule
     */
    private void reload(long sceneId, List<RuleInfo> ruleInfos) {
        KieServices kieServices = KieServices.get();
        //2.创建kiemodule xml对应的class
        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
        //TODO  .添加具体的KieBase标签
        KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel(buildKbaseName(sceneId));
        kieBaseModel.setDefault(true);
        //3.配合 drl的包名
        kieBaseModel.addPackage(MessageFormat.format("test.scene_{0}", String.valueOf(sceneId)));
        kieBaseModel.newKieSessionModel(buildKsessionName(sceneId));
        //4.创建KieFileSystem虚拟文件系统
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        for (RuleInfo ruleInfo : ruleInfos) {
            //5.这里是把规则文件添加到虚拟系统，第一个参数是文件在虚拟系统中的路径，这里的文件目录和3.处的addPackage必须一致，否则失败
            String fullPath = MessageFormat.format("src/main/resources/test/scene_{0}/rule_{1}.drl", String.valueOf(sceneId), String.valueOf(ruleInfo.getId()));
            kieFileSystem.write(fullPath, ruleInfo.getContent());
        }
        //6.添加kiemodule.xml文件到虚拟文件系统
        kieFileSystem.writeKModuleXML(kieModuleModel.toXML());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        Results results = kieBuilder.getResults();
    }

}
