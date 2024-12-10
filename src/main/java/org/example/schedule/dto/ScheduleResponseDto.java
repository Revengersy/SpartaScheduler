package org.example.schedule.dto;

import org.example.schedule.controller.*;
import org.example.schedule.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;  // 고유 식별자
    private String task;  // 할 일
    private String writerName;  // 작성자명
    private ZonedDateTime writtenDate;  // 작성일
    private ZonedDateTime editedDate;

    // Memo class를 인자로 가지는 생성자
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.task = schedule.getTask();
        this.writerName = schedule.getWriterName();
        this.writtenDate = schedule.getWrittenTime();
        this.editedDate = schedule.getEditedTime();
    }
}
