/**
 * 8.12-test.java
 * 业务逻辑漏洞演示示例（仅用于安全测试与教学）
 *
 * 编译: javac -encoding UTF-8 8.12-test.java
 * 运行: java LogicVulnDemo812
 */
class LogicVulnDemo812 {

    /** 模拟订单数据: orderId -> 订单 */
    private static final java.util.Map<String, Order> ORDERS = new java.util.HashMap<>();
    /** 模拟优惠券使用状态: couponCode -> 是否已使用 */
    private static final java.util.Map<String, Boolean> COUPON_USED = new java.util.concurrent.ConcurrentHashMap<>();
    /** 模拟账户余额: userId -> 余额 */
    private static final java.util.Map<String, Double> BALANCE = new java.util.HashMap<>();

    static class Order {
        String orderId;
        String ownerId;
        double amount;

        Order(String orderId, String ownerId, double amount) {
            this.orderId = orderId;
            this.ownerId = ownerId;
            this.amount = amount;
        }
    }

    static {
        ORDERS.put("O1001", new Order("O1001", "user_a", 500.0));
        COUPON_USED.put("DISCOUNT50", false);
        BALANCE.put("user_a", 100.0);
        BALANCE.put("user_b", 50.0);
    }

    /**
     * 漏洞一: 越权访问 (IDOR / 水平越权)
     * 漏洞点: 直接使用前端传入的 orderId 查询订单, 未校验订单归属,
     *         任意登录用户都可以查看甚至操作他人的订单。
     */
    static String queryOrder(String currentUser, String orderId) {
        Order order = ORDERS.get(orderId); // VULNERABLE: 缺少 order.ownerId.equals(currentUser) 校验
        if (order == null) {
            return "订单不存在";
        }
        return "订单 " + orderId + " 属于 " + order.ownerId + ", 金额 " + order.amount + " 元 (VULNERABLE)";
    }

    /**
     * 漏洞二: 优惠券重复使用
     * 漏洞点: 只判断优惠券是否存在, 未判断是否已被使用, 也未在使用后置为已用,
     *         同一张优惠券可以被反复使用。
     */
    static String useCoupon(String couponCode) {
        if (COUPON_USED.containsKey(couponCode)) { // VULNERABLE: 应同时检查 get(couponCode) 为 false 并 put(couponCode, true)
            return "优惠券 " + couponCode + " 使用成功, 抵扣 50 元 (VULNERABLE)";
        }
        return "优惠券不存在";
    }

    /**
     * 漏洞三: 价格篡改
     * 漏洞点: 完全信任客户端提交的价格, 攻击者可以把价格改成 0.01 甚至负数,
     *         应以服务端配置的商品价格为准。
     */
    static String buyProduct(String productId, double price) {
        double payPrice = price; // VULNERABLE: 应使用服务端价格, 而非前端传入的 price
        return "购买 " + productId + " 成功, 实付 " + payPrice + " 元 (VULNERABLE)";
    }

    /**
     * 漏洞四: 余额校验缺失 / 负数转账
     * 漏洞点: 转账时未校验余额是否充足, 也未限制金额必须为正数,
     *         负数转账或超额转账都会导致资金逻辑错误。
     */
    static String transfer(String fromUser, double amount) {
        // VULNERABLE: 缺少 amount > 0 以及 fromUser 余额充足性校验
        double newBalance = BALANCE.get(fromUser) - amount;
        BALANCE.put(fromUser, newBalance);
        return fromUser + " 转账 " + amount + " 元成功, 剩余余额 " + newBalance + " 元 (VULNERABLE)";
    }

    /**
     * 漏洞五: 验证码绕过 (校验与状态分离)
     * 漏洞点: 仅校验了验证码正确性, 未对"校验通过"做一次性标记,
     *         同一个校验结果可在短时间内被并发/重复利用。
     */
    static String loginWithCaptcha(String username, String password, String captcha) {
        if ("123456".equals(password) && "8888".equals(captcha)) {
            // VULNERABLE: 校验通过后未使 captcha 失效, 可被重放
            return username + " 登录成功 (VULNERABLE)";
        }
        return "用户名密码或验证码错误";
    }

    public static void main(String[] args) {
        System.out.println("===== 业务逻辑漏洞演示 (8.12-test.java) =====");
        System.out.println("[越权访问]   " + queryOrder("user_b", "O1001"));
        System.out.println("[优惠券复用] " + useCoupon("DISCOUNT50"));
        System.out.println("[价格篡改]   " + buyProduct("product1", 0.01));
        System.out.println("[负数转账]   " + transfer("user_a", -100.0));
        System.out.println("[验证码重放] " + loginWithCaptcha("user_a", "123456", "8888"));
    }
}