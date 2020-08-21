package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }




    /1.获取一个KieServices
    KieServices kieServices = KieServices.Factory.get();
    //2.创建kiemodule xml对应的class
    KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
    //3.创建KieFileSystem虚拟文件系统
    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

    //4.添加具体的KieBase标签
    KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel("mydecision").
            addPackage("mydecision");//kie fileSystem 中资源文件的文件夹
//<KieBase></KieBase>标签添加KieSession属性
kieBaseModel.newKieSessionModel("kiession-mydecision");//a


    //5.添加kiemodule.xml文件到虚拟文件系统
    String kieModuleModelXml = kieModuleModel.toXML();
kieFileSystem.writeKModuleXML(kieModuleModelXml);//kieModuleModel

    //6.把规则文件加载到虚拟文件系统
    Resource resource = getResource("drools/decisiontable/mydecisiontable.xls");
    String fileName = "mydecision-table" + resource.getResourceType().getDefaultExtension();
    //这里是把规则文件添加到虚拟系统，第一个参数是文件在虚拟系统中的路径，这里的文件目录和4.处的addPackage必须一致，否则失败
    String kieFilePath = new StringBuilder("src/main/resources/").
            append("mydecision").append("/").append(fileName).toString();
kieFileSystem.write(kieFilePath, resource);

//7.构建所有的KieBase并把所有的KieBase添加到仓库里
kieServices.newKieBuilder(kieFileSystem).buildAll();
    KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());//创建kie容器

    //8.从容器中获取一个会话，这里和a处添加的是一个key，否则找不到 找不到任何一个会报异常
    KieSession kieSession = kieContainer.newKieSession("kiession-mydecision");
kieSession.insert(new Student(11, "max"));
kieSession.insert(new Student(13, "max"));
kieSession.fireAllRules();


}
