# 第三方组件清单

基于SBOM生成器的第三方组件分析

## 组件统计

- 总组件数: 65
- 含已知漏洞组件数: 6

## 组件详情

### 核心框架
- org.springframework.boot:spring-boot-starter-web:1.5.1.RELEASE
- org.springframework.boot:spring-boot-starter-thymeleaf:1.5.1.RELEASE
- org.springframework.boot:spring-boot-starter-actuator:1.5.1.RELEASE
- org.springframework.boot:spring-boot-starter-security:2.1.5.RELEASE

### 日志组件
- org.slf4j:slf4j-api:1.7.36
- ch.qos.logback:logback-classic:1.4.14
- ch.qos.logback:logback-core:1.4.14
- org.apache.logging.log4j:log4j-core:2.9.1 (含漏洞: CVE-2021-44228等)
- org.apache.logging.log4j:log4j-api:2.9.1

### 数据库相关
- mysql:mysql-connector-java:8.0.12
- org.mybatis.spring.boot:mybatis-spring-boot-starter:1.3.2
- com.h2database:h2:1.4.199

### JSON/XML处理
- com.alibaba:fastjson:1.2.24 (含漏洞: CVE-2022-25845等)
- com.fasterxml.jackson.core:jackson-databind:2.16.0 (含漏洞: CVE-2022-42003等)
- com.fasterxml.jackson.core:jackson-annotations:2.16.0
- com.fasterxml.jackson.core:jackson-core:2.16.0
- org.jdom:jdom2:2.0.6
- org.dom4j:dom4j:2.1.0
- com.thoughtworks.xstream:xstream:1.4.20 (含漏洞: CVE-2021-21351等)

### HTTP客户端
- org.apache.httpcomponents:httpclient:4.5.12
- org.apache.httpcomponents:fluent-hc:4.3.6
- org.apache.httpcomponents:httpasyncclient:4.1.4
- commons-httpclient:commons-httpclient:3.1
- com.squareup.okhttp:okhttp:2.5.0

### Apache Commons
- commons-collections:commons-collections:3.1 (含漏洞: CVE-2015-6420等)
- commons-lang:commons-lang:2.4
- commons-net:commons-net:3.6
- commons-io:commons-io:2.5
- commons-beanutils:commons-beanutils:1.9.4

### 安全相关
- org.springframework.security:spring-security-web:4.2.12.RELEASE
- org.springframework.security:spring-security-config:4.2.12.RELEASE
- org.apache.shiro:shiro-core:1.2.4 (含漏洞: CVE-2022-32532等)
- org.jsecurity:jsecurity:0.9.0
- io.jsonwebtoken:jjwt:0.9.1
- com.auth0:java-jwt:4.0.0

### 其他重要组件
- org.apache.poi:poi:3.10-FINAL
- org.apache.poi:poi-ooxml:3.9
- com.monitorjbl:xlsx-streamer:2.0.0
- org.jsoup:jsoup:1.10.2
- com.google.guava:guava:23.0
- cn.hutool:hutool-all:5.8.10
- org.yaml:snakeyaml:1.21
- io.springfox:springfox-swagger2:2.9.2
- org.projectlombok:lombok:1.18.20

## 已识别的安全漏洞

以下组件包含已知的安全漏洞：

1. **com.alibaba:fastjson:1.2.24**
   - CVE-2022-25845
   - CVE-2022-25847
   - CVE-2021-25646

2. **org.apache.logging.log4j:log4j-core:2.9.1**
   - CVE-2021-44228 (Log4Shell)
   - CVE-2021-45046
   - CVE-2021-45105

3. **com.thoughtworks.xstream:xstream:1.4.20**
   - CVE-2021-21351
   - CVE-2021-29505

4. **commons-collections:commons-collections:3.1**
   - CVE-2015-6420
   - CVE-2015-7501

5. **org.apache.shiro:shiro-core:1.2.4**
   - CVE-2022-32532
   - CVE-2021-41303

6. **com.fasterxml.jackson.core:jackson-databind:2.16.0**
   - CVE-2022-42003
   - CVE-2022-42004