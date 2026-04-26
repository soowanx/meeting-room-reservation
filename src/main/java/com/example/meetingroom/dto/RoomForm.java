package com.example.meetingroom.dto;

import com.example.meetingroom.domain.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoomForm {

    @NotBlank(message = "회의실 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "위치를 입력해주세요.")
    private String location;

    @Min(value = 1, message = "수용 인원은 1명 이상이어야 합니다.")
    private int capacity;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    @NotNull(message = "공간 유형을 선택해주세요.")
    private RoomType roomType;

    @NotBlank(message = "지역을 입력해주세요.")
    private String region;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
