package com.example.meetingroom.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    public static final String ADMIN_SESSION_KEY = "ADMIN_AUTHENTICATED";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Boolean authenticated = (Boolean) request.getSession().getAttribute(ADMIN_SESSION_KEY);
        if (Boolean.TRUE.equals(authenticated)) {
            return true;
        }

        response.sendRedirect("/admin/login");
        return false;
    }
}
