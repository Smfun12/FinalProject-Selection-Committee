package com.example.FinalProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@SpringBootApplication
public class FinalProjectApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("lang/res");

		System.out.println(messageSource.getMessage("hello", null, Locale.ENGLISH));
		SpringApplication.run(FinalProjectApplication.class, args);
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
	}
}
