package com.fugaga.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * security配置类
 * @Author fuGaga
 * @Date 2021/1/22 15:37
 * @Version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
               auth.inMemoryAuthentication()
                       .withUser("fugaga")
                       .password(new BCryptPasswordEncoder().encode("123"))
                       .authorities("ROOT");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
//                http.authorizeRequests()
//                        .antMatchers("/getOrder")
//                        .permitAll()
//                        .and()
//                        .csrf().disable();
                http.csrf().disable()
                        .authorizeRequests()
                        //.antMatchers("/gp/gp1").hasAnyAuthority("p1")
                        //.antMatchers("/gp/gp2").hasAnyAuthority("p2")
                        //.antMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
                //解决静态资源被拦截的问题
                web.ignoring().antMatchers("/asserts/**");
                web.ignoring().antMatchers("/favicon.ico");
        }


        //用户认证管理
        @Bean
        public AuthenticationManager authenticationManager() throws Exception {
                return super.authenticationManager();
        }
}
