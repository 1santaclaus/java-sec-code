package org.joychou.controller;

import org.joychou.util.SBOMGenerator;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sbom")
@CrossOrigin(origins = "*")
public class SBOMController {

    /**
     * 获取JSON格式的SBOM报告
     */
    @RequestMapping(value = "/report", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSBOMReport() {
        try {
            String sbomJson = SBOMGenerator.generateSBOMJson();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=sbom-report.json")
                    .body(sbomJson);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to generate SBOM report: " + e.getMessage());
            return ResponseEntity.status(500).body(error.toString());
        }
    }

    /**
     * 获取文本格式的SBOM摘要报告
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSBOMSummary() {
        try {
            String summaryReport = SBOMGenerator.generateSummaryReport();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=sbom-summary.txt")
                    .body(summaryReport);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Failed to generate SBOM summary: " + e.getMessage());
        }
    }

    /**
     * 下载JSON格式的SBOM报告文件
     */
    @RequestMapping(value = "/download/json", method = RequestMethod.GET)
    public void downloadSBOMJson(HttpServletResponse response) {
        try {
            response.setContentType("application/json");
            response.setHeader("Content-Disposition", "attachment; filename=sbom-report.json");
            
            String sbomJson = SBOMGenerator.generateSBOMJson();
            response.getWriter().write(sbomJson);
            response.getWriter().flush();
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Failed to download SBOM report: " + e.getMessage());
            } catch (IOException ioException) {
                // 忽略发送错误的异常
            }
        }
    }

    /**
     * 下载文本格式的SBOM摘要报告文件
     */
    @RequestMapping(value = "/download/summary", method = RequestMethod.GET)
    public void downloadSBOMSummary(HttpServletResponse response) {
        try {
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment; filename=sbom-summary-report.txt");
            
            String summaryReport = SBOMGenerator.generateSummaryReport();
            response.getWriter().write(summaryReport);
            response.getWriter().flush();
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Failed to download SBOM summary: " + e.getMessage());
            } catch (IOException ioException) {
                // 忽略发送错误的异常
            }
        }
    }

    /**
     * 获取SBOM统计信息
     */
    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getSBOMStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            java.util.List<SBOMGenerator.Component> components = SBOMGenerator.getProjectComponents();
            
            stats.put("totalComponents", components.size());
            
            long vulnerableCount = 0;
            for (SBOMGenerator.Component c : components) {
                if (!c.vulnerabilities.isEmpty()) {
                    vulnerableCount++;
                }
           }
            stats.put("vulnerableComponents", vulnerableCount);
            
            long nonVulnerableCount = components.size() - vulnerableCount;
            stats.put("secureComponents", nonVulnerableCount);
            
            // 按作用域统计
            Map<String, Long> scopeStats = new HashMap<>();
            for (SBOMGenerator.Component component : components) {
                String scope = component.scope != null ? component.scope : "compile";
                scopeStats.put(scope, scopeStats.getOrDefault(scope, 0L) + 1);
            }
            stats.put("scopeDistribution", scopeStats);
            
            // 按组统计
            Map<String, Long> groupStats = new HashMap<>();
            for (SBOMGenerator.Component component : components) {
                String group = component.groupId;
                groupStats.put(group, groupStats.getOrDefault(group, 0L) + 1);
            }
            stats.put("groupDistribution", groupStats);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(stats);
        }
    }
}
                           