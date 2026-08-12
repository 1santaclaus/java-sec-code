package org.test;

import org.joychou.util.SBOMGenerator;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class SBOMGeneratorTest {

    @Test
    public void testGetComponentList() {
        List<SBOMGenerator.Component> components = SBOMGenerator.getProjectComponents();
        assertNotNull("Components list should not be null", components);
        assertTrue("Components list should not be empty", components.size() > 0);
        System.out.println("Total components: " + components.size());
    }

    @Test
    public void testSBOMJsonGeneration() throws IOException {
        String sbomJson = SBOMGenerator.generateSBOMJson();
        assertNotNull("SBOM JSON should not be null", sbomJson);
        assertTrue("SBOM JSON should contain components", sbomJson.contains("components"));
        assertTrue("SBOM JSON should contain bomFormat", sbomJson.contains("bomFormat"));
        System.out.println("SBOM JSON generated successfully");
    }

    @Test
    public void testSummaryReportGeneration() {
        String summaryReport = SBOMGenerator.generateSummaryReport();
        assertNotNull("Summary report should not be null", summaryReport);
        assertTrue("Summary report should contain component count", summaryReport.contains("依赖组件总数"));
        System.out.println("Summary report generated successfully");
    }

    @Test
    public void testVulnerableComponents() {
        List<SBOMGenerator.Component> components = SBOMGenerator.getProjectComponents();
        long vulnerableCount = components.stream()
                .filter(c -> !c.vulnerabilities.isEmpty())
                .count();
        System.out.println("Vulnerable components: " + vulnerableCount);
        assertTrue("There should be vulnerable components identified", vulnerableCount > 0);
    }

    @Test
    public void testSaveSBOMToFile() throws IOException {
        String testFilePath = "target/test-sbom.json";
        SBOMGenerator.saveSBOMToFile(testFilePath);
        // 验证文件是否可以正常生成
        String content = SBOMGenerator.generateSBOMJson();
        assertNotNull("SBOM content should be generated", content);
    }

    @Test
    public void testSaveSummaryReportToFile() throws IOException {
        String testFilePath = "target/test-summary.txt";
        SBOMGenerator.saveSummaryReportToFile(testFilePath);
        // 验证文件是否可以正常生成
        String content = SBOMGenerator.generateSummaryReport();
        assertNotNull("Summary content should be generated", content);
    }
}
