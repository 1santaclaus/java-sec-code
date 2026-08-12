package org.joychou.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 附件管理
 */
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    private static final Map<String, Attachment> ATTACHMENTS = new HashMap<>();

    static {
        ATTACHMENTS.put("FILE20260801-001", new Attachment("FILE20260801-001", 10001L, "身份证复印件.pdf", "实名认证材料"));
        ATTACHMENTS.put("FILE20260801-002", new Attachment("FILE20260801-002", 10002L, "劳动合同.pdf", "入职材料"));
        ATTACHMENTS.put("FILE20260801-003", new Attachment("FILE20260801-003", 10001L, "银行流水.pdf", "贷款审核材料"));
    }

    /**
     * 下载附件
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileId") String fileId,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("请先登录");
        }
        Attachment attachment = ATTACHMENTS.get(fileId);
        if (attachment == null) {
            throw new IllegalArgumentException("附件不存在");
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + attachment.fileName);
        byte[] content = ("附件内容: " + attachment.description).getBytes(StandardCharsets.UTF_8);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(content);
        outputStream.flush();
    }

    static class Attachment {
        String fileId;
        Long userId;
        String fileName;
        String description;

        Attachment(String fileId, Long userId, String fileName, String description) {
            this.fileId = fileId;
            this.userId = userId;
            this.fileName = fileName;
            this.description = description;
        }
    }
}