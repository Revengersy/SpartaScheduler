package org.example.schedule.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private String task;  // 할 일
    private String writerName;  // 작성자명
    private String password;  // 비밀번호
}