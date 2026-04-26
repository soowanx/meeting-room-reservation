package com.example.meetingroom.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    private final String adminPassword;

    public AdminAuthService(@Value("${app.admin.password}") String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public boolean matches(String inputPassword) {
        return adminPassword.equals(inputPassword);
    }
}
