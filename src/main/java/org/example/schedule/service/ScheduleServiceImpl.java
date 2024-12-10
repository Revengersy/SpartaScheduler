package org.example.schedule.service;

import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.ScheduleResponseDto;
import org.example.schedule.entity.Schedule;
import org.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        // 요청받은 데이터로 Schedule 객체 생성 ID 없음
        Schedule schedule = new Schedule(requestDto.getTask(), requestDto.getWriterName(), requestDto.getPassword());

        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        // 전체 조회
        return scheduleRepository.findAllSchedules();
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        // 식별자의 Schedule이 없다면?
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto findScheduleByWriter(String writerName) {
        // 식별자의 Schedule이 없다면?
        Schedule schedule = scheduleRepository.findScheduleByWriterOrElseThrow(writerName);

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findScheduleByDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);

            // Call the repository method
            return scheduleRepository.findSchedulesByDate(localDate);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format.");
        }
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedulesFiltered(String editedDate, String writerName) {
        LocalDate dateFilter = null;
        if (editedDate != null && !editedDate.isEmpty()) {
            dateFilter = LocalDate.parse(editedDate);
        }
        return scheduleRepository.findAllByFilters(dateFilter, writerName);
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String task, String writerName, String password) {
        // 기존 일정 조회
        Schedule existingSchedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        // 비밀번호 확인
        if (!existingSchedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password.");
        }

        // 일정 수정
        existingSchedule.updateTask(task);
        existingSchedule.updateWriterName(writerName);

        // 데이터베이스 업데이트
        scheduleRepository.updateScheduleDetails(existingSchedule);

        return new ScheduleResponseDto(existingSchedule);
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateWriterName(Long id, String writerName) {
        // 필수값 검증
        if (writerName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The writerName is a required value.");
        }

        int updatedRow = scheduleRepository.updateWriterName(id, writerName);

        // NPE 방지
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    @Override
    public void deleteSchedule(Long id, String password) {
        // 기존 일정 조회
        Schedule existingSchedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        // 비밀번호 확인
        if (!existingSchedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password.");
        }

        // 일정 삭제
        scheduleRepository.deleteSchedule(id);
    }
}
