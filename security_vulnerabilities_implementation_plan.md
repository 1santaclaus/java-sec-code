# 安全漏洞示例实施计划

## 项目概述
在现有的Java安全代码项目中新增两个安全漏洞示例：
1. 业务逻辑漏洞 (Business Logic Vulnerabilities)
2. 水平/垂直越权漏洞 (Access Control Vulnerabilities)

## 1. 业务逻辑漏洞控制器 (BusinessLogicVuln.java)

### 漏洞场景设计

#### 1.1 优惠券重复使用漏洞
**漏洞描述**：系统只检查优惠券是否在有效期内，但未检查是否已被使用过。

**实现功能**：
- `/business/coupon/vuln` - 漏洞版本：只验证有效期
- `/business/coupon/sec` - 安全版本：验证有效期和使用状态

#### 1.2 价格篡改漏洞
**漏洞描述**：前端进行价格验证，但后端直接信任前端传递的价格参数。

#### 1.3 库存竞争条件漏洞
**漏洞描述**：高并发场景下，库存检查与扣减存在时间差，导致超卖。

#### 1.3 库存竞争条件漏洞
**漏洞描述**：库存检查后未立即锁定，多个用户可以同时购买同一商品。

### 2. 水平/垂直越权漏洞控制器 (AccessControlVuln.java)

#### 2.1 水平越权漏洞
**漏洞描述**：通过修改订单ID参数，可以访问其他用户的订单信息。

#### 2.2 垂直越权漏洞
**漏洞描述**：普通用户通过直接访问管理员URL或修改权限参数获得更高权限。

## 技术实现细节

### BusinessLogicVuln.java 类结构
```java
@RestController
@RequestMapping("/business")
public class BusinessLogicVuln {
    
    // 优惠券重复使用漏洞
    @GetMapping("/coupon/vuln")
    public String couponVuln(@RequestParam String couponCode) {
        // 只检查有效期，不检查使用状态
        return "Coupon used successfully (VULNERABLE)";
}
```

### AccessControlVuln.java 类结构
```java
@RestController
@RequestMapping("/access")
public class AccessControlVuln {
    
    // 水平越权：订单信息查询
    @GetMapping("/order/vuln")
    public String orderVuln(@RequestParam String orderId) {
        // 直接返回订单信息，未验证用户权限
        return "Order details (VULNERABLE)";
}
```

## 实施步骤

1. **创建BusinessLogicVuln.java** - 实现业务逻辑相关漏洞
2. **创建AccessControlVuln.java` - 实现访问控制相关漏洞
3. **验证功能可访问性**
4. **测试漏洞利用场景**

## 预期结果
- 新增两个完整的漏洞示例控制器
- 每个控制器包含漏洞版本和安全版本
- 完整的URL路径和文档说明