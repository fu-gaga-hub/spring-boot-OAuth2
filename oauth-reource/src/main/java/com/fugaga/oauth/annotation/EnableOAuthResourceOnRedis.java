package com.fugaga.oauth.annotation;

import com.fugaga.oauth.config.ResourcesServerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动装配资源服务器注解类
 * @Author fuGaga
 * @Date 2021/1/26 9:21
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ResourcesServerConfig.class)
public @interface EnableOAuthResourceOnRedis {
}
