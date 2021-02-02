package com.fugaga.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author fuGaga
 * @Date 2021/1/25 14:32
 * @Version 1.0
 */
@RestController
public class ResourceController {

        @GetMapping("/getResource")
        public String getResource(){
                return "---------------------------返回资源信息成功！--------------------------";
        }
}
