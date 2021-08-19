package com.leon.hello.es7.service;

import com.leon.hello.es7.entity.UserInfo;

/**
 * @PROJECT_NAME: hello-es7
 * @CLASS_NAME: UserInfoService
 * @AUTHOR: OceanLeonAI
 * @CREATED_DATE: 2021/8/19 16:42
 * @Version 1.0
 * @DESCRIPTION:
 **/
public interface UserInfoService {
    /**
     * 增加用户信息
     *
     * @param userInfo 用户信息
     */
    void addUserInfo(UserInfo userInfo);

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserInfo getByName(Integer id);

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 用户信息
     */
    UserInfo updateUserInfo(UserInfo userInfo);

    /**
     * 删除用户信息
     *
     * @param id 用户ID
     */
    void deleteById(Integer id);
}
