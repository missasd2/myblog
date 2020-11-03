package com.example.controller;


import com.example.common.lang.Result;
import com.example.entity.User;
import com.example.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SunWay
 * @since 2020-11-01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    //
    @RequiresAuthentication // 这个注解告诉它需要认证之后才能访问
    @GetMapping("/index")
    public Result index(){
        // return userService.getById(1L);
        User user = userService.getById(1L);
        // return Result.succ(200, "操作成功", user);

        return Result.succ(user);
    }

    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user){

        return Result.succ(user);

    }

}
