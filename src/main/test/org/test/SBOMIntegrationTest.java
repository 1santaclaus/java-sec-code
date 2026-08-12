package org.test;

import org.joychou.util.SBOMGenerator;
import org.joychou.service.SBOMService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SBOMIntegrationTest {

    @Autowired
    private SBOMService sbomService;

    @Test
    public void testSBOMServiceGeneration() throws IOException {
        // 测试SBOM服务生成报告
        String jsonReportPath = sbomService.generateAndSaveSBOMReport();
        String summaryReportPath = sbomService.generateAndSaveSummaryReport();

        assertNotNull("JSON报告路径不应为空", jsonReportPath);
        assertNotNull("摘要报告路径不应为空", summaryReportPath);
        assertTrue("JSON报告路径应包含正确的扩展名", jsonReportPath.endsWith(".json"));
        assertTrue("摘要报告路径应包含正确的扩展名", summaryReportPath.endsWith(".txt"));

        System.out.println("JSON报告路径: " + jsonReportPath);
        System.out.println("摘要报告路径: " + summaryReportPath);
    }

    @Test
    public void testSBOMGeneratorDirectly() throws IOException {
        // 直接测试SBOM生成器
        String sbomJson = SBOMGenerator.generateSBOMJson();
        assertNotNull("SBOM JSON不应为空", sbomJson);
        assertTrue("SBOM JSON应包含组件信息", sbomJson.contains("components"));
        assertTrue("SBOM JSON应包含格式信息", sbomJson.contains("bomFormat"));

        String summaryReport = SBOMGenerator.generateSummaryReport();
        assertNotNull("摘要报告不应为空", summaryReport);
        assertTrue("摘要报告应包含组件计数", summaryReport.contains("依赖组件总数"));

        System.out.println("直接生成的SBOM JSON长度: " + sbomJson.length());
        System.out.println("直接生成的摘要报告长度: " + summaryReport.length());
    }

    @Test
    public void testGetLatestReports() {
        // 测试获取最新报告路径
        String latestJsonPath = sbomService.getLatestSBOMReportPath();
        String latestSummaryPath = sbomService.getLatestSummaryReportPath();

        // 可能还没有生成任何报告，所以这些可能为null，这是正常的
        System.out.println("最新JSON报告路径: " + latestJsonPath);
        System.out.println("最新摘要报告路径: " + latestSummaryPath);
    }

    @Test
    public void testComponentVulnerabilityDetection() {
        // 测试漏洞检测功能
        var components = SBOMGenerator.getProjectComponents();
        long vulnerableCount = components.stream()
                .filter(c -> !c.vulnerabilities.isEmpty())
                .count();

        System.out.println("总组件数: " + components.size());
        System.out.println("含漏洞组件数: " + vulnerableCount);

        assertTrue("应该至少识别出一些已知的易受攻击组件", vulnerableCount > 0);
    }
}
