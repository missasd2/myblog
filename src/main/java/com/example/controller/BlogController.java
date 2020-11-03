package com.example.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.Blog;
import com.example.service.BlogService;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SunWay
 * @since 2020-11-01
 */
@RestController

public class BlogController {

    @Autowired
    BlogService blogService;

    // 定义详情页
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){

        // 支持分页
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));


        return Result.succ(pageData);

    }

    // 定义index
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id){
        Blog blog = blogService.getById(id);
        // 一旦为空，则抛出异常，且提示"该博客已被删除"
        Assert.notNull(blog, "该博客已被删除");


        return Result.succ(blog);
    }

    // 定义edit, 编辑和添加是同一体的; 如果传过来没有id说明是添加状态，如果有id说明是编辑状态
    @RequiresAuthentication  // 该注解，表明该接口需要认证才能访问
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){

        Blog tmp = null;

        // 编辑状态
        if (blog.getId() != null){
            tmp = blogService.getById(blog.getId());
            // 只能编辑自己的文章; 因此加一个断言进行逻辑判断;
            // 如果id不一致，即断言为false会抛出IllegalArgumentException被GlobalExceptionHandler捕获
            Assert.isTrue(tmp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        }else {
            // 添加状态
            tmp = new Blog();
            tmp.setId(ShiroUtil.getProfile().getId());
            tmp.setCreated(LocalDateTime.now());
            tmp.setStatus(0);


        }

        BeanUtil.copyProperties(blog, tmp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(tmp);

        return Result.succ(null);
    }

}
