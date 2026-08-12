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
 * 用户中心
 */
@RestController
@RequestMapping("/user")
public class UserCenterController {

    private static final Map<Long, UserProfile> PROFILES = new HashMap<>();

    static {
        PROFILES.put(10001L, new UserProfile(10001L, "张三", "13800000001", "zhangsan@example.com"));
        PROFILES.put(10002L, new UserProfile(10002L, "李四", "13900000002", "lisi@example.com"));
        PROFILES.put(10003L, new UserProfile(10003L, "王五", "13700000003", "wangwu@example.com"));
    }

    /**
     * 查看用户资料
     */
    @GetMapping("/profile")
    public Map<String, Object> profile(@RequestParam("userId") Long userId, HttpServletRequest request) {
        Long loginUserId = (Long) request.getSession().getAttribute("userId");
        if (loginUserId == null) {
            throw new IllegalStateException("请先登录");
        }
        UserProfile profile = PROFILES.get(userId);
        if (profile == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("userId", profile.userId);
        result.put("name", profile.name);
        result.put("mobile", profile.mobile);
        result.put("email", profile.email);
        return result;
    }

    /**
     * 修改用户资料
     */
    @PostMapping("/update")
    public String update(@RequestParam("userId") Long userId,
                         @RequestParam(value = "mobile", required = false) String mobile,
                         @RequestParam(value = "email", required = false) String email,
                         HttpServletRequest request) {
        Long loginUserId = (Long) request.getSession().getAttribute("userId");
        if (loginUserId == null) {
            throw new IllegalStateException("请先登录");
        }
        UserProfile profile = PROFILES.get(userId);
        if (profile == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (mobile != null) {
            profile.mobile = mobile;
        }
        if (email != null) {
            profile.email = email;
        }
        return "修改成功";
    }

    static class UserProfile {
        Long userId;
        String name;
        String mobile;
        String email;

        UserProfile(Long userId, String name, String mobile, String email) {
            this.userId = userId;
            this.name = name;
            this.mobile = mobile;
            this.email = email;
        }
    }
}