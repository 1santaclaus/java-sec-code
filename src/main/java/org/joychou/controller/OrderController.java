package org.joychou.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单相关接口
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Map<String, Order> ORDERS = new HashMap<>();

    static {
        ORDERS.put("NO20260801001", new Order("NO20260801001", 10001L, 299.0, "张三", "北京市朝阳区望京街道1号", "待发货"));
        ORDERS.put("NO20260801002", new Order("NO20260801002", 10002L, 59.9, "李四", "上海市浦东新区张江路88号", "已签收"));
        ORDERS.put("NO20260801003", new Order("NO20260801003", 10001L, 1299.0, "张三", "北京市朝阳区望京街道1号", "配送中"));
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/detail")
    public Map<String, Object> detail(@RequestParam("orderNo") String orderNo, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("请先登录");
        }
        Order order = ORDERS.get(orderNo);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", order.orderNo);
        result.put("amount", order.amount);
        result.put("receiver", order.receiver);
        result.put("address", order.address);
        result.put("status", order.status);
        return result;
    }

    /**
     * 修改订单收货地址
     */
    @PostMapping("/updateAddress")
    public String updateAddress(@RequestParam("orderNo") String orderNo,
                                @RequestParam("address") String address,
                                HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("请先登录");
        }
        Order order = ORDERS.get(orderNo);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        order.address = address;
        return "修改成功";
    }

    static class Order {
        String orderNo;
        Long userId;
        double amount;
        String receiver;
        String address;
        String status;

        Order(String orderNo, Long userId, double amount, String receiver, String address, String status) {
            this.orderNo = orderNo;
            this.userId = userId;
            this.amount = amount;
            this.receiver = receiver;
            this.address = address;
            this.status = status;
        }
    }
}