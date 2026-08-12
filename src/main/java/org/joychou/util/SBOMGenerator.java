package org.joychou.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joychou.Application;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * SBOM (Software Bill of Materials) Generator
 * Generates a Software Bill of Materials report based on project dependencies
 */
public class SBOMGenerator {

    // 定义项目依赖信息
    public static class Component {
        public String groupId;
        public String artifactId;
        public String version;
        public String scope;
        public List<String> vulnerabilities; // 潜在的安全漏洞

        public Component(String groupId, String artifactId, String version, String scope) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            this.scope = scope;
            this.vulnerabilities = new ArrayList<>();
        }

        public void addVulnerability(String vulnerability) {
            this.vulnerabilities.add(vulnerability);
        }
    }

    /**
     * 获取项目的所有依赖组件
     * 这里是从pom.xml中提取的信息
     */
    public static List<Component> getProjectComponents() {
        List<Component> components = new ArrayList<>();

        // 核心框架依赖
        components.add(new Component("org.springframework.boot", "spring-boot-starter-web", "1.5.1.RELEASE", "compile"));
        components.add(new Component("org.springframework.boot", "spring-boot-starter-thymeleaf", "1.5.1.RELEASE", "compile"));
        components.add(new Component("org.springframework.boot", "spring-boot-starter-actuator", "1.5.1.RELEASE", "compile"));
        components.add(new Component("org.springframework.boot", "spring-boot-starter-security", "2.1.5.RELEASE", "compile"));

        // 日志相关依赖
        components.add(new Component("org.slf4j", "slf4j-api", "1.7.36", "compile"));
        components.add(new Component("ch.qos.logback", "logback-classic", "1.4.14", "compile"));
        components.add(new Component("ch.qos.logback", "logback-core", "1.4.14", "compile"));
        components.add(new Component("org.apache.logging.log4j", "log4j-core", "2.9.1", "compile"));
        components.add(new Component("org.apache.logging.log4j", "log4j-api", "2.9.1", "compile"));

        // 数据库相关依赖
        components.add(new Component("mysql", "mysql-connector-java", "8.0.12", "compile"));
        components.add(new Component("org.mybatis.spring.boot", "mybatis-spring-boot-starter", "1.3.2", "compile"));
        components.add(new Component("com.h2database", "h2", "1.4.199", "test"));

        // JSON/XML处理依赖
        components.add(new Component("com.alibaba", "fastjson", "1.2.24", "compile"));
        components.add(new Component("com.fasterxml.jackson.core", "jackson-databind", "2.16.0", "compile"));
        components.add(new Component("com.fasterxml.jackson.core", "jackson-annotations", "2.16.0", "compile"));
        components.add(new Component("com.fasterxml.jackson.core", "jackson-core", "2.16.0", "compile"));
        components.add(new Component("org.jdom", "jdom2", "2.0.6", "compile"));
        components.add(new Component("org.dom4j", "dom4j", "2.1.0", "compile"));
        components.add(new Component("com.thoughtworks.xstream", "xstream", "1.4.20", "compile"));

        // HTTP客户端相关
        components.add(new Component("org.apache.httpcomponents", "httpclient", "4.5.12", "compile"));
        components.add(new Component("org.apache.httpcomponents", "fluent-hc", "4.3.6", "compile"));
        components.add(new Component("org.apache.httpcomponents", "httpasyncclient", "4.1.4", "compile"));
        components.add(new Component("commons-httpclient", "commons-httpclient", "3.1", "compile"));
        components.add(new Component("com.squareup.okhttp", "okhttp", "2.5.0", "compile"));

        // Apache Commons相关
        components.add(new Component("commons-collections", "commons-collections", "3.1", "compile"));
        components.add(new Component("commons-lang", "commons-lang", "2.4", "compile"));
        components.add(new Component("commons-net", "commons-net", "3.6", "compile"));
        components.add(new Component("commons-io", "commons-io", "2.5", "compile"));
        components.add(new Component("commons-beanutils", "commons-beanutils", "1.9.4", "compile"));
        components.add(new Component("org.apache.commons", "commons-digester3", "3.2", "compile"));

        // 安全相关依赖
        components.add(new Component("org.springframework.security", "spring-security-web", "4.2.12.RELEASE", "compile"));
        components.add(new Component("org.springframework.security", "spring-security-config", "4.2.12.RELEASE", "compile"));
        components.add(new Component("org.apache.shiro", "shiro-core", "1.2.4", "compile"));
        components.add(new Component("org.jsecurity", "jsecurity", "0.9.0", "compile"));
        components.add(new Component("io.jsonwebtoken", "jjwt", "0.9.1", "compile"));
        components.add(new Component("com.auth0", "java-jwt", "4.0.0", "compile"));

        // 模板引擎和表达式语言
        components.add(new Component("org.apache.velocity", "velocity", "1.7", "compile"));
        components.add(new Component("org.springframework", "spring-expression", "4.3.16.RELEASE", "compile"));

        // Office文档处理
        components.add(new Component("org.apache.poi", "poi", "3.10-FINAL", "compile"));
        components.add(new Component("org.apache.poi", "poi-ooxml", "3.9", "compile"));
        components.add(new Component("com.monitorjbl", "xlsx-streamer", "2.0.0", "compile"));

        // 网络和解析库
        components.add(new Component("org.jsoup", "jsoup", "1.10.2", "compile"));
        components.add(new Component("com.google.guava", "guava", "23.0", "compile"));
        components.add(new Component("cn.hutool", "hutool-all", "5.8.10", "compile"));
        components.add(new Component("org.javassist", "javassist", "3.27.0-GA", "compile"));
        components.add(new Component("org.yaml", "snakeyaml", "1.21", "compile"));

        // 云服务和监控
        components.add(new Component("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-client", "1.4.0.RELEASE", "compile"));
        components.add(new Component("org.jolokia", "jolokia-core", "1.6.0", "compile"));

        // 工具类库
        components.add(new Component("com.fasterxml.uuid", "java-uuid-generator", "3.1.4", "compile"));
        components.add(new Component("io.springfox", "springfox-swagger2", "2.9.2", "compile"));
        components.add(new Component("io.springfox", "springfox-swagger-ui", "2.9.2", "compile"));
        components.add(new Component("org.projectlombok", "lombok", "1.18.20", "provided"));
        components.add(new Component("com.jayway.jsonpath", "json-path", "1.5.1.RELEASE", "compile")); // 使用Spring Boot默认版本

        // 数据库驱动
        components.add(new Component("org.postgresql", "postgresql", "42.3.1", "compile"));
        components.add(new Component("com.ibm.db2", "jcc", "11.5.8.0", "compile"));

        // 其他
        components.add(new Component("org.springframework", "spring-test", "4.3.7.RELEASE", "compile")); // 使用Spring Boot默认版本
        components.add(new Component("junit", "junit", "4.12", "test")); // 使用Spring Boot默认版本
        components.add(new Component("org.springframework.data", "spring-data-commons", "1.13.11.RELEASE", "compile"));
        components.add(new Component("org.xmlbeam", "xmlprojector", "1.4.13", "compile"));
        components.add(new Component("org.apache.tomcat", "tomcat-dbcp", "9.0.8", "compile"));
        components.add(new Component("com.alibaba", "QLExpress", "3.3.1", "compile"));

        // 为一些已知存在安全风险的组件添加漏洞标识
        for (Component component : components) {
            // Fastjson存在多个已知漏洞
            if ("com.alibaba".equals(component.groupId) && "fastjson".equals(component.artifactId)) {
                component.addVulnerability("CVE-2022-25845");
                component.addVulnerability("CVE-2022-25847");
                component.addVulnerability("CVE-2021-25646");
            }
            // Log4j存在严重漏洞
            else if ("org.apache.logging.log4j".equals(component.groupId) && "log4j-core".equals(component.artifactId)) {
                component.addVulnerability("CVE-2021-44228");
                component.addVulnerability("CVE-2021-45046");
                component.addVulnerability("CVE-2021-45105");
            }
            // XStream存在反序列化漏洞
            else if ("com.thoughtworks.xstream".equals(component.groupId) && "xstream".equals(component.artifactId)) {
                component.addVulnerability("CVE-2021-21351");
                component.addVulnerability("CVE-2021-29505");
            }
            // Commons Collections存在反序列化漏洞
            else if ("commons-collections".equals(component.groupId) && "commons-collections".equals(component.artifactId)) {
                component.addVulnerability("CVE-2015-6420");
                component.addVulnerability("CVE-2015-7501");
            }
            // Shiro存在身份验证绕过漏洞
            else if ("org.apache.shiro".equals(component.groupId) && "shiro-core".equals(component.artifactId)) {
                component.addVulnerability("CVE-2022-32532");
                component.addVulnerability("CVE-2021-41303");
            }
            // Jackson databind存在反序列化漏洞
            else if ("com.fasterxml.jackson.core".equals(component.groupId) && "jackson-databind".equals(component.artifactId)) {
                component.addVulnerability("CVE-2022-42003");
                component.addVulnerability("CVE-2022-42004");
            }
        }

        return components;
    }

    /**
     * 生成SBOM JSON格式报告
     */
    public static String generateSBOMJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        // 添加SBOM元数据
        rootNode.put("bomFormat", "CycloneDX");
        rootNode.put("specVersion", "1.4");
        rootNode.put("serialNumber", "urn:uuid:" + java.util.UUID.randomUUID().toString());
        rootNode.put("version", 1);

        // 添加生成时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        rootNode.put("creationInfo", mapper.createObjectNode()
                .put("created", now.format(formatter))
                .put("creators", mapper.createArrayNode().add("Tool: Java-Sec-Code SBOM Generator")));

        // 添加组件信息
        ArrayNode componentsNode = mapper.createArrayNode();
        List<Component> projectComponents = getProjectComponents();

        for (Component comp : projectComponents) {
            ObjectNode compNode = mapper.createObjectNode();
            compNode.put("type", "library");
            compNode.put("bom-ref", comp.groupId + ":" + comp.artifactId + ":" + comp.version);
            compNode.put("group", comp.groupId);
            compNode.put("name", comp.artifactId);
            compNode.put("version", comp.version);
            compNode.put("scope", comp.scope != null ? comp.scope : "required");

            // 添加许可证信息（如果可用）
            ObjectNode licenseNode = mapper.createObjectNode();
            licenseNode.put("id", "Apache-2.0"); // 默认使用Apache 2.0许可证
            ArrayNode licensesNode = mapper.createArrayNode().add(licenseNode);
            compNode.set("licenses", licensesNode);

            // 添加漏洞信息
            if (!comp.vulnerabilities.isEmpty()) {
                ArrayNode vulnerabilitiesNode = mapper.createArrayNode();
                for (String vuln : comp.vulnerabilities) {
                    ObjectNode vulnNode = mapper.createObjectNode();
                    vulnNode.put("id", vuln);
                    vulnNode.put("source", mapper.createObjectNode().put("name", "NVD"));
                    vulnerabilitiesNode.add(vulnNode);
                }
                compNode.set("vulnerabilities", vulnerabilitiesNode);
            }

            componentsNode.add(compNode);
        }

        rootNode.set("components", componentsNode);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

    /**
     * 将SBOM报告保存到文件
     */
    public static void saveSBOMToFile(String filePath) throws IOException {
        String sbomJson = generateSBOMJson();
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(sbomJson);
        }
    }

    /**
     * 生成摘要报告
     */
    public static String generateSummaryReport() {
        List<Component> components = getProjectComponents();
        StringBuilder report = new StringBuilder();
        
        report.append("=== 软件物料清单 (SBOM) 汇总报告 ===\n");
        report.append("生成时间: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        report.append("项目名称: java-sec-code\n");
        report.append("项目版本: 1.0.0\n\n");
        
        report.append("依赖组件总数: ").append(components.size()).append("\n");
        
        int vulnerableComponents = 0;
        for (Component comp : components) {
            if (!comp.vulnerabilities.isEmpty()) {
                vulnerableComponents++;
            }
        }
        
        report.append("存在安全漏洞的组件数: ").append(vulnerableComponents).append("\n\n");
        
        report.append("--- 组件详情 ---\n");
        for (Component comp : components) {
            report.append("组件: ").append(comp.groupId).append(":").append(comp.artifactId).append(":").append(comp.version).append("\n");
            report.append("  作用域: ").append(comp.scope != null ? comp.scope : "compile").append("\n");
            
            if (!comp.vulnerabilities.isEmpty()) {
                report.append("  已知漏洞: \n");
                for (String vuln : comp.vulnerabilities) {
                    report.append("    - ").append(vuln).append("\n");
                }
            }
            report.append("\n");
        }
        
        return report.toString();
    }

    /**
     * 保存摘要报告到文件
     */
    public static void saveSummaryReportToFile(String filePath) throws IOException {
        String summaryReport = generateSummaryReport();
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(summaryReport);
        }
    }
}