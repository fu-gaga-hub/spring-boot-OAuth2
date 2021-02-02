package com.fugaga.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 资源管理配置类
 * @Author fuGaga
 * @Date 2021/1/25 14:21
 * @Version 1.0
 */
@Configuration
@EnableResourceServer
public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private RedisConnectionFactory redisConnectionFactory;


        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
               resources.resourceId("res1").stateless(true);
               resources.tokenServices(tokenServices());
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
                http.csrf().disable()
                        .authorizeRequests()
                        //.antMatchers("/gp/gp1").hasAnyAuthority("p1")
                        //.antMatchers("/gp/gp2").hasAnyAuthority("p2")
                        //.antMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin();
        }

        // 自定义的Token存储器，存到Redis中
        @Bean
        public TokenStore tokenStore() {
                RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
                return tokenStore;
        }

        @Bean
        public ResourceServerTokenServices tokenServices(){
                DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
                defaultTokenServices.setTokenStore(tokenStore());

                return defaultTokenServices;
        }

}
