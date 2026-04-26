package com.example.meetingroom.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminLoginForm {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
