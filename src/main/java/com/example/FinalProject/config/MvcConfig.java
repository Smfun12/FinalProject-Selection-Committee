package com.example.FinalProject.config;

import com.example.FinalProject.api.dto.HeaderStorage;
import com.example.FinalProject.api.interceptor.CustomInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("mainPage");
    }

    @Bean // <--- 1
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver(); // <--- 2
        Locale defaultLocale = new Locale("ua");
        localeResolver.setDefaultLocale(defaultLocale); // <--- 3

        return localeResolver;
    }
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        // Defaults to "locale" if not set
        localeChangeInterceptor.setParamName("localeData");

        return localeChangeInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(localeChangeInterceptor());
        interceptorRegistry.addInterceptor(customInterceptor());
    }

    @Bean
    public HandlerInterceptor customInterceptor() {
        return new CustomInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    @RequestScope
    public HeaderStorage requestHeaderStorage(){
        return new HeaderStorage();
    }
}
