package com.team5.secondhand.global.config;

import com.team5.secondhand.global.auth.interceptor.AuthorityInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${server.address.frontend}")
    private String FRONTEND_SERVER;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorityInterceptor())
                .addPathPatterns(AuthorityInterceptor.LOGIN_ESSENTIAL)
                .excludePathPatterns(AuthorityInterceptor.LOGIN_INESSENTIAL);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://127.0.0.1:5173", "http://localhost:5173",
                        "http://3.37.51.148:81/",
                        FRONTEND_SERVER
                )
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .maxAge(36000);
    }

    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofNone();
    }
}
