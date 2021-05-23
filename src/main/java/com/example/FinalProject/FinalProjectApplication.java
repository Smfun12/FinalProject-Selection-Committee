package com.example.FinalProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FinalProjectApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("lang/res");

		System.out.println(messageSource.getMessage("hello", null, LocaleContextHolder.getLocale()));
		SpringApplication.run(FinalProjectApplication.class, args);
	}
}
