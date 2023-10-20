package com.team5.secondhand.global.config;

import com.team5.secondhand.global.auth.AuthorizationArgumentResolver;
import com.team5.secondhand.global.auth.AuthorizationContext;
import com.team5.secondhand.global.auth.AuthorityInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Value("${server.address.domain}")
    private String testDomain;
    @Value("${server.address.was2}")
    private String prodDomain;

    private final AuthorizationContext context;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorityInterceptor(context))
                .addPathPatterns(AuthorityInterceptor.LOGIN_ESSENTIAL)
                .excludePathPatterns(AuthorityInterceptor.LOGIN_INESSENTIAL);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthorizationArgumentResolver(context));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://127.0.0.1:5173", "http://localhost:5173",
                        "http://127.0.0.1:5174", "http://localhost:5174",
                        testDomain, prodDomain
                )
                .allowedMethods(
                        HttpMethod.GET.name(), HttpMethod.POST.name(),
                        HttpMethod.PATCH.name(), HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name(),
                        HttpMethod.HEAD.name())
                .allowedHeaders("*")
                .maxAge(36000);
    }

    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofNone();
    }
}
