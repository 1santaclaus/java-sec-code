package org.joychou.service;

import org.joychou.util.SBOMGenerator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SBOMService {

    private static final String SBOM_OUTPUT_DIR = "sbom-reports";

    /**
     * 生成并保存JSON格式的SBOM报告
     */
    public String generateAndSaveSBOMReport() throws IOException {
        // 确保输出目录存在
        Path outputDir = Paths.get(SBOM_OUTPUT_DIR);
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // 生成带时间戳的文件名
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String fileName = "sbom-report-" + timestamp + ".json";
        String filePath = SBOM_OUTPUT_DIR + "/" + fileName;

        // 生成并保存SBOM报告
        SBOMGenerator.saveSBOMToFile(filePath);

        return filePath;
    }

    /**
     * 生成并保存摘要报告
     */
    public String generateAndSaveSummaryReport() throws IOException {
        // 确保输出目录存在
        Path outputDir = Paths.get(SBOM_OUTPUT_DIR);
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // 生成带时间戳的文件名
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String fileName = "sbom-summary-" + timestamp + ".txt";
        String filePath = SBOM_OUTPUT_DIR + "/" + fileName;

        // 生成并保存摘要报告
        SBOMGenerator.saveSummaryReportToFile(filePath);

        return filePath;
    }

    /**
     * 获取最新的SBOM报告路径
     */
    public String getLatestSBOMReportPath() {
        try {
            Path outputDir = Paths.get(SBOM_OUTPUT_DIR);
            if (!Files.exists(outputDir)) {
                return null;
            }

            // 查找最新的JSON报告文件
            return Files.list(outputDir)
                    .filter(path -> path.toString().endsWith(".json"))
                    .filter(path -> path.toString().contains("sbom-report-"))
                    .max((p1, p2) -> {
                        String t1 = extractTimestampFromFilename(p1.getFileName().toString());
                        String t2 = extractTimestampFromFilename(p2.getFileName().toString());
                        return t2.compareTo(t1); // 降序排列，获取最新文件
                    })
                    .map(Path::toString)
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从文件名中提取时间戳
     */
    private String extractTimestampFromFilename(String filename) {
        // 文件名格式: sbom-report-YYYYMMDD-HHMMSS.json
        int startIndex = filename.indexOf('-');
        if (startIndex != -1) {
            startIndex = filename.indexOf('-', startIndex + 1);
            if (startIndex != -1) {
                startIndex += 1; // 跳过 '-'
                int endIndex = filename.lastIndexOf('.');
                if (endIndex != -1) {
                    return filename.substring(startIndex, endIndex);
                }
            }
        }
        return "";
    }

    /**
     * 获取最新的摘要报告路径
     */
    public String getLatestSummaryReportPath() {
        try {
            Path outputDir = Paths.get(SBOM_OUTPUT_DIR);
            if (!Files.exists(outputDir)) {
                return null;
            }

            // 查找最新的TXT摘要报告文件
            return Files.list(outputDir)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(path -> path.toString().contains("sbom-summary-"))
                    .max((p1, p2) -> {
                        String t1 = extractTimestampFromFilename(p1.getFileName().toString());
                        String t2 = extractTimestampFromFilename(p2.getFileName().toString());
                        return t2.compareTo(t1); // 降序排列，获取最新文件
                    })
                    .map(Path::toString)
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}