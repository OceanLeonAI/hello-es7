package com.leon.hello.es7.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.leon.hello.es7.entity.UserInfo;
import com.leon.hello.es7.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @PROJECT_NAME: hello-es7
 * @CLASS_NAME: UserInfoController
 * @AUTHOR: OceanLeonAI
 * @CREATED_DATE: 2021/8/19 16:43
 * @Version 1.0
 * @DESCRIPTION:
 **/
@RestController
@RequestMapping
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/userInfo/{id}")
    public Object getUserInfo(@PathVariable Integer id) {
        UserInfo userInfo = userInfoService.getByName(id);
        if (userInfo == null) {
            return "没有该用户";
        }
        return userInfo;
    }

    @PostMapping("/userInfo")
    public Object createUserInfo(@RequestBody UserInfo userInfo) {
        userInfoService.addUserInfo(userInfo);
        return "SUCCESS";
    }

    @PutMapping("/userInfo")
    public Object updateUserInfo(@RequestBody UserInfo userInfo) {
        UserInfo newUserInfo = userInfoService.updateUserInfo(userInfo);
        if (newUserInfo == null) {
            return "不存在该用户";
        }
        return newUserInfo;
    }

    @DeleteMapping("/userInfo/{id}")
    public Object deleteUserInfo(@PathVariable Integer id) {
        userInfoService.deleteById(id);
        return "SUCCESS";
    }

    // --------------- 测试删除缓存 begin ---------------
    @Autowired
    Cache<String, Object> caffeineCache;

    @DeleteMapping("/testDelete")
    public Object testDelete() {
        String name = "leon";

        String cachedKey = "name";

        // 加入缓存
        caffeineCache.put(cachedKey, name);

        // 获取缓存
        // 先从缓存读取
        caffeineCache.getIfPresent(cachedKey);
        String nameCached = (String) caffeineCache.asMap().get(cachedKey);
        System.out.println("删除缓存前 name ---> " + nameCached);

        // 删除缓存再读取
        if (caffeineCache.asMap().containsKey(cachedKey)) {

            // 删除一个不存在的 key 会不会报错
            caffeineCache.asMap().remove("address");

            caffeineCache.asMap().remove(cachedKey);
            name = (String) caffeineCache.asMap().get(name);
            System.out.println("删除缓存后 name ---> " + name);
        }


        return "SUCCESS";
    }
    // --------------- 测试删除缓存   end ---------------

}
