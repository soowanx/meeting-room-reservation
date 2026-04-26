package com.example.meetingroom.domain;

public enum RoomType {
    MEETING_ROOM("미팅룸"),
    IR_ROOM("IR룸"),
    SEMINAR_EVENT_HALL("세미나룸·이벤트홀"),
    STUDIO("스튜디오");

    private final String label;

    RoomType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
