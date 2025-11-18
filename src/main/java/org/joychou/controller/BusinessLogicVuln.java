package org.joychou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Business Logic Vulnerabilities
 * 
 * @author JoyChou @2024-11-18
 */
@RestController
@RequestMapping("/business")
public class BusinessLogicVuln {

    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicVuln.class);

    // 模拟数据库存储
    private static Map<String, Boolean> couponUsedStatus = new ConcurrentHashMap<>();
    private static Map<String, Double> productPrices = new ConcurrentHashMap<>();
    private static int inventory = 10;

    static {
        couponUsedStatus.put("DISCOUNT50", false);
        productPrices.put("product1", 100.0);
        productPrices.put("product2", 200.0);
    }

    /**
     * 优惠券重复使用漏洞
     * 漏洞：只检查优惠券是否存在，不检查是否已使用
     */
    @GetMapping("/coupon/vuln")
    public String couponVuln(@RequestParam String couponCode) {
        if (couponUsedStatus.containsKey(couponCode)) {
            return "优惠券 " + couponCode + " 使用成功 (VULNERABLE)";
        } else {
            return "优惠券不存在";
        }
    }

    /**
     * 优惠券安全版本
     */
    @GetMapping("/coupon/sec")
    public String couponSec(@RequestParam String couponCode) {
        if (couponUsedStatus.containsKey(couponCode)) {
            if (!couponUsedStatus.get(couponCode)) {
                couponUsedStatus.put(couponCode, true);
                return "优惠券 " + couponCode + " 使用成功 (SECURE)";
        } else {
            return "优惠券已使用或不存在";
        }
    }

    /**
     * 价格篡改漏洞
     * 漏洞：直接信任前端传递的价格
     */
    @GetMapping("/price/vuln")
    public String priceVuln(@RequestParam String productId, @RequestParam Double price) {
        Double originalPrice = productPrices.get(productId);
        if (originalPrice != null) {
            return "购买产品 " + productId + " 成功，价格: " + price + " (VULNERABLE)";
        } else {
            return "产品不存在";
        }
    }

    /**
     * 价格安全版本
     */
    @GetMapping("/price/sec")
    public String priceSec(@RequestParam String productId) {
        Double price = productPrices.get(productId);
        if (price != null) {
            return "购买产品 " + productId + " 成功，价格: " + price + " (SECURE)";
        } else {
            return "产品不存在";
        }
    }

    /**
     * 库存竞争条件漏洞
     */
    @GetMapping("/inventory/vuln")
    public String inventoryVuln() {
        if (inventory > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            logger.error(e.toString());
        }
        inventory--;
        return "购买成功，剩余库存: " + inventory + " (VULNERABLE)";
        } else {
            return "库存不足";
        }
    }

    /**
     * 库存安全版本
     */
    @GetMapping("/inventory/sec")
    public synchronized String inventorySec() {
        if (inventory > 0) {
            inventory--;
            return "购买成功，剩余库存: " + inventory + " (SECURE)";
        } else {
            return "库存不足";
        }
    }

    /**
     * 重置库存
     */
    @GetMapping("/inventory/reset")
    public String inventoryReset() {
        inventory = 10;
        return "库存已重置为: " + inventory;
    }
}