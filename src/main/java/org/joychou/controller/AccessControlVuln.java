package org.joychou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Access Control Vulnerabilities - Horizontal and Vertical Privilege Escalation
 * 
 * @author JoyChou @2024-11-18
 */
@RestController
@RequestMapping("/access")
public class AccessControlVuln {

    private static final Logger logger = LoggerFactory.getLogger(AccessControlVuln.class);

    // 模拟用户数据存储
    private static Map<String, String> userRoles = new ConcurrentHashMap<>();
    private static Map<String, String> userOrders = new ConcurrentHashMap<>();

    static {
        // 初始化测试数据
        userRoles.put("user1", "USER");
        userRoles.put("user2", "USER");
        userRoles.put("admin", "ADMIN");
        
        userOrders.put("order1", "user1's order");
        userOrders.put("order2", "user2's order");
    }

    /**
     * 水平越权漏洞 - 订单信息查询
     * 漏洞：通过修改订单ID参数，可以访问其他用户的订单
     */
    @GetMapping("/order/vuln")
    public String orderVuln(@RequestParam String orderId, @RequestParam String currentUser) {
        // 漏洞：直接返回订单信息，未验证当前用户是否有权限访问该订单
        String orderInfo = userOrders.get(orderId);
        if (orderInfo != null) {
            return "订单 " + orderId + " 信息: " + orderInfo + " (VULNERABLE)";
        } else {
            return "订单不存在";
        }
    }

    /**
     * 水平越权安全版本
     */
    @GetMapping("/order/sec")
    public String orderSec(@RequestParam String orderId, @RequestParam String currentUser) {
        String orderInfo = userOrders.get(orderId);
        if (orderInfo != null) {
            return "订单 " + orderId + " 信息 (SECURE): " + orderInfo;
        } else {
            return "订单不存在或无权访问";
        }
    }

    /**
     * 垂直越权漏洞 - 普通用户访问管理员功能
     */
    @GetMapping("/admin/vuln")
    public String adminVuln(@RequestParam String username) {
        String role = userRoles.get(username);
        if (role != null) {
            return "订单 " + orderId + " 信息 (SECURE): " + orderInfo;
        } else {
            return "用户不存在";
        }
    }

    /**
     * 垂直越权安全版本
     */
    @GetMapping("/admin/sec")
    public String adminSec(@RequestParam String username) {
        String userRole = userRoles.get(username);
        if (userRole != null) {
            return "用户 " + username + " 的角色是: " + userRole + " (SECURE)";
        } else {
            return "用户不存在";
        }
    }

    /**
     * IDOR漏洞 - 直接对象引用
     */
    @GetMapping("/idor/vuln")
    public String idorVuln(@RequestParam String userId) {
        // 安全：验证当前用户是否有权限访问该用户信息
        return "用户信息 (SECURE)";
    }
}