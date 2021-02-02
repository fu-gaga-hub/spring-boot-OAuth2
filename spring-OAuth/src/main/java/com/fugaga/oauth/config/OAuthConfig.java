package com.fugaga.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * OAuth配置类
 * @Author fuGaga
 * @Date 2021/1/22 16:28
 * @Version 1.0
 */
@Configuration
@EnableAuthorizationServer // 激活OAuth2.0 显示的表示是一个授权服务
public class OAuthConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        RedisConnectionFactory redisConnectionFactory;


        @Autowired
        private TokenStore tokenStore;

//        @Autowired
//        private ClientDetailsService clientDetailsService;

        @Autowired
        private AuthorizationCodeServices authorizationCodeServices;

        /**
         * 用户认证管理器
         */
        @Autowired
        public AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
                //允许客户端表单提交
                security.allowFormAuthenticationForClients()
                        //客户端校验token访问许可
                        .checkTokenAccess("permitAll()")
                        //客户端token调用许可
                        .tokenKeyAccess("permitAll()");
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//                clients.inMemory()
//                        .withClient("client-a")
//                        .secret("123")
//                       //.redirectUris("http://localhost:9001/callback")
//                        //支持 授权码、密码两种授权模式，支持刷新token功能
//                        .authorizedGrantTypes("authorization_code", "password", "refresh_token");
                clients.inMemory() // 使用内存来存储客户端的信息
                        .withClient("c1") // 客户端编号
                        .secret(new BCryptPasswordEncoder().encode("secret"))
                        .resourceIds("res1")//可以访问的资源的编号
                        .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token") //该客户端允许的授权类型
                        .scopes("all") // 允许授权的范围  我们对资源操作的作用域 读 写
                        .autoApprove(false) // false的话 请求到来的时候会跳转到授权页面
                        .redirectUris("http://www.baidu.com") // 回调的地址  授权码会作为参赛绑定在重定向的地址中
                ;
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
                //配置认证管理器
                endpoints.authenticationManager(authenticationManager)
                        //配置用户服务
                       // .userDetailsService(userDetailsService)
                        //配置token存储的服务与位置
                        .authorizationCodeServices(authorizationCodeServices) // 授权码服务
                        .tokenServices(tokenService())
                        .tokenStore(tokenStore())
                        .allowedTokenEndpointRequestMethods(HttpMethod.POST,HttpMethod.GET);
        }

        @Bean
        public TokenStore tokenStore() {
                //使用redis存储token
                RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
                //设置redis token存储中的前缀
                //redisTokenStore.setPrefix("auth-token:");
                return redisTokenStore;
        }


        @Bean
        public DefaultTokenServices tokenService() {
                DefaultTokenServices tokenServices = new DefaultTokenServices();
                //配置token存储
                tokenServices.setTokenStore(tokenStore());
                //开启支持refresh_token，此处如果之前没有配置，启动服务后再配置重启服务，可能会导致不返回token的问题，解决方式：清除redis对应token存储
                tokenServices.setSupportRefreshToken(true);
                //复用refresh_token
                tokenServices.setReuseRefreshToken(true);
                //token有效期，设置12小时
                tokenServices.setAccessTokenValiditySeconds(12 * 60 * 60);
                //refresh_token有效期，设置一周
                tokenServices.setRefreshTokenValiditySeconds(7 * 24 * 60 * 60);
                return tokenServices;
        }

        @Bean
        public AuthorizationCodeServices authorizationCodeServices(){
                return new InMemoryAuthorizationCodeServices();
        }

}
