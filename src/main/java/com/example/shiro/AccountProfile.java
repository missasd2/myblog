package com.example.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装一个VO
 */
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String avatar;

    private String email;




}