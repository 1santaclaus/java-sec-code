package org.joychou.task;

import org.joychou.service.SBOMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SBOMReportTask {

    private static final Logger logger = LoggerFactory.getLogger(SBOMReportTask.class);

    @Autowired
    private SBOMService sbomService;

    /**
     * 定时生成SBOM报告 - 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void generateDailySBOMReport() {
        try {
            logger.info("开始生成每日SBOM报告...");
            
            String jsonReportPath = sbomService.generateAndSaveSBOMReport();
            String summaryReportPath = sbomService.generateAndSaveSummaryReport();
            
            logger.info("SBOM JSON报告已生成: {}", jsonReportPath);
            logger.info("SBOM摘要报告已生成: {}", summaryReportPath);
            
        } catch (Exception e) {
            logger.error("生成SBOM报告时发生错误", e);
        }
    }

    /**
     * 定时生成SBOM报告 - 每周一凌晨3点执行（更详细的报告）
     */
    @Scheduled(cron = "0 0 3 ? * MON")
    public void generateWeeklySBOMReport() {
        try {
            logger.info("开始生成每周SBOM详细报告...");
            
            String jsonReportPath = sbomService.generateAndSaveSBOMReport();
            String summaryReportPath = sbomService.generateAndSaveSummaryReport();
            
            logger.info("每周SBOM JSON报告已生成: {}", jsonReportPath);
            logger.info("每周SBOM摘要报告已生成: {}", summaryReportPath);
            
        } catch (Exception e) {
            logger.error("生成每周SBOM报告时发生错误", e);
        }
    }
}