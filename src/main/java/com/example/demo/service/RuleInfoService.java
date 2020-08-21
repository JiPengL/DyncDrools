package com.example.demo.service;

import com.example.demo.entity.RuleInfo;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujipeng
 * @date 2020/8/20 17:27
 * @mail liujipeng@cloud-er.com
 * @desc ...
 */

@Service
public class RuleInfoService {

    /**
     * 获取给定场景下的规则信息列表
     *
     * @param sceneId 场景ID
     * @return 规则列表
     */
    public List<RuleInfo> getRuleInfoListBySceneId(Long sceneId) {
        Map<Long, List<RuleInfo>> sceneId2RuleInfoListMap = getRuleInfoListMap();
        return sceneId2RuleInfoListMap.get(sceneId);
    }

    /**
     * 获取场景与规则信息列表的Map
     *
     * @return 场景规则信息列表Map，Map(sceneId : List < RuleInfo >)
     */
    public Map<Long, List<RuleInfo>> getRuleInfoListMap() {
        Map<Long, List<RuleInfo>> ruleInfoListMap = new HashMap<>();
        //获取到所有 状态为可用的场景
        List<Long> list = Dao.getAllSceneId();
        for (Long sid:list) {
            List<RuleInfo> ruleInfoList = getRuleInfoList(sid);
            ruleInfoListMap.put(sid,ruleInfoList);

        }
        return ruleInfoListMap;
    }


    List<RuleInfo> getRuleInfoList(Long sceneId){
        List<RuleInfo> infoList = Dao.getRuleInfoListBysceneId(sceneId);
        for (RuleInfo info :infoList) {
            //将动参封装到 规则里面
            String format = MessageFormat.format(info.getContent(), info.getArg());
            info.setContent(format);
        }
        return infoList;
    }












    //    --------------------------------   TEST    --------------------------------
    /**
     * 规则 包的数字要对好 场景的ID值
     */

    List<RuleInfo> getRuleInfo(){
        List<RuleInfo> ruleInfoList = new ArrayList<>();
        RuleInfo info = new RuleInfo();
        info.setSceneId(1L);
        info.setId(1L);
        String con = "package com.xu.rules.scene_{0};\n" +
                "import com.example.demo.entity.Person;\n" +
                "rule \"1\"\n" +
                "\twhen\n" +
                "        $p : Person(age > 30);\n" +
                "    then\n" +
                "\t\tSystem.out.println(\"hello, young xu2!\");\n" +
                "end\n" +
                "\n" +
                "query \"people2\"\n" +
                "    person : Person( age > 30 )\n" +
                "end";
        String format = MessageFormat.format(con, 1L, 1L);
        info.setContent(format);
        ruleInfoList.add(info);

        return ruleInfoList;
    }
}
