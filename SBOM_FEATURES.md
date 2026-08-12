# SBOM (Software Bill of Materials) 功能说明

## 概述

本项目集成了软件物料清单（SBOM）生成功能，用于导出项目的依赖组件信息及相关的安全漏洞信息。此功能有助于进行软件供应链安全分析和合规性审计。

## 功能特点

1. **自动依赖分析**: 自动提取项目的所有第三方依赖组件
2. **漏洞识别**: 识别已知存在安全漏洞的组件
3. **多格式输出**: 支持JSON和文本格式的SBOM报告
4. **定时生成**: 支持定时自动生成SBOM报告
5. **REST API**: 提供REST API接口访问SBOM报告

## API 接口

### 获取SBOM报告

- **GET** `/api/sbom/report` - 获取JSON格式的完整SBOM报告
- **GET** `/api/sbom/summary` - 获取文本格式的SBOM摘要报告
- **GET** `/api/sbom/stats` - 获取SBOM统计信息

### 下载SBOM报告

- **GET** `/api/sbom/download/json` - 下载JSON格式的SBOM报告
- **GET** `/api/sbom/download/summary` - 下载文本格式的SBOM摘要报告

## 报告内容

### JSON格式报告 (CycloneDX标准)

- **bomFormat**: CycloneDX
- **specVersion**: 1.4
- **components**: 所有依赖组件列表
  - groupId: 组件组ID
  - name: 组件名称
  - version: 版本号
  - scope: 作用域（compile, test等）
  - vulnerabilities: 已知漏洞列表（如果有）

### 文本摘要报告

- 项目基本信息
- 依赖组件总数
- 存在安全漏洞的组件数量
- 详细的组件列表及其漏洞信息

## 定时任务

系统配置了以下定时任务：

- **每日报告**: 每天凌晨2点自动生成SBOM报告
- **每周报告**: 每周一凌晨3点生成详细的SBOM报告

生成的报告会保存在 `sbom-reports/` 目录下，文件名包含时间戳。

## 使用示例

### 通过API获取报告

```bash
# 获取JSON格式SBOM报告
curl http://localhost:8080/api/sbom/report

# 获取文本格式摘要报告
curl http://localhost:8080/api/sbom/summary

# 获取SBOM统计信息
curl http://localhost:8080/api/sbom/stats
```

### 手动生成报告

通过调用SBOMService类的方法可以手动生成报告：

```java
@Autowired
private SBOMService sbomService;

// 生成并保存SBOM报告
String reportPath = sbomService.generateAndSaveSBOMReport();

// 生成并保存摘要报告
String summaryPath = sbomService.generateAndSaveSummaryReport();
```

## 安全漏洞识别

系统预定义了一些常见的易受攻击组件及其已知漏洞：

- **Fastjson**: 多个反序列化漏洞 (CVE-2022-25845, CVE-2021-25646等)
- **Log4j**: 严重远程代码执行漏洞 (CVE-2021-44228等)
- **XStream**: 反序列化漏洞 (CVE-2021-21351等)
- **Commons Collections**: 反序列化漏洞 (CVE-2015-6420等)
- **Shiro**: 身份验证绕过漏洞 (CVE-2022-32532等)
- **Jackson Databind**: 反序列化漏洞 (CVE-2022-42003等)

## 配置

所有SBOM相关功能都已集成到应用中，无需额外配置。报告默认保存在应用运行目录下的 `sbom-reports/` 子目录中。

## 应用场景

1. **安全审计**: 定期审查项目依赖的安全状况
2. **合规性检查**: 满足软件供应链安全合规要求
3. **漏洞管理**: 跟踪和管理第三方组件的安全漏洞
4. **风险管理**: 评估和降低软件供应链风险

## 注意事项

- 生成的SBOM报告仅基于项目当前的依赖配置
- 漏洞信息基于预定义的已知漏洞数据库，可能不是最新的
- 建议定期更新依赖组件以减少安全风险