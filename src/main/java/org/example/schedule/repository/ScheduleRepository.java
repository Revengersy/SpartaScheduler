package org.example.schedule.repository;

 // Schedule 엔티티 임포트
import org.example.schedule.dto.ScheduleResponseDto;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.Writer;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules();

    Optional<Schedule> findScheduleById(Long id);

    List<ScheduleResponseDto> findSchedulesByDate(LocalDate date);
    List<ScheduleResponseDto> findAllByFilters(LocalDate editedDate, String writerName);

    Schedule findScheduleByIdOrElseThrow(Long id);
    Schedule findScheduleByWriterOrElseThrow(String writerName);


    int updateScheduleDetails(Schedule schedule);

    int updateWriterName(Long id, String writerName);

    int deleteSchedule(Long id);


}
