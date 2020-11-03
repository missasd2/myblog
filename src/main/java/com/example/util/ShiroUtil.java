package com.example.util;

import com.example.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * 用于将当前登录的用户 转型成AccountProfile 的工具类
 */
public class ShiroUtil {

    public static AccountProfile getProfile(){

        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
