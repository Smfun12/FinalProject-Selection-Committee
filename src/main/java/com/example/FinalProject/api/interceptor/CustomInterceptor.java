package com.example.FinalProject.api.interceptor;

import com.example.FinalProject.api.dto.HeaderStorage;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class CustomInterceptor implements HandlerInterceptor {

    @Resource(name = "requestHeaderStorage")
    private HeaderStorage headerStorage;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if (name.equals("jdbc"))
                    headerStorage.setHeader("jdbc");
            }
        }
        if (headerStorage.getHeader()==null)
            headerStorage.setHeader("No header was given");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.print("");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.print("");
    }
}
