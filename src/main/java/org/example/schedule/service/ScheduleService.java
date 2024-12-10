package org.example.schedule.service;

import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto);

    List<ScheduleResponseDto> findAllSchedules();

    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto findScheduleByWriter(String writerName);

    List<ScheduleResponseDto> findScheduleByDate(String date);
    List<ScheduleResponseDto> findAllSchedulesFiltered(String editedDate, String writerName);

    ScheduleResponseDto updateSchedule(Long id, String task, String writerName, String password);

    ScheduleResponseDto updateWriterName(Long id, String writerName);

    void deleteSchedule(Long id, String password);
}