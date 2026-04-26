package com.example.meetingroom.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public class ReservationCreateForm {

    @NotBlank(message = "예약자 이름을 입력해주세요.")
    private String reserverName;

    @NotBlank(message = "예약자 이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String reserverEmail;

    @NotBlank(message = "사용 목적을 입력해주세요.")
    private String purpose;

    @NotNull(message = "시작 시간을 선택해주세요.")
    @Future(message = "시작 시간은 현재 이후여야 합니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startAt;

    @NotNull(message = "종료 시간을 선택해주세요.")
    @Future(message = "종료 시간은 현재 이후여야 합니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endAt;

    public String getReserverName() {
        return reserverName;
    }

    public void setReserverName(String reserverName) {
        this.reserverName = reserverName;
    }

    public String getReserverEmail() {
        return reserverEmail;
    }

    public void setReserverEmail(String reserverEmail) {
        this.reserverEmail = reserverEmail;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
}
