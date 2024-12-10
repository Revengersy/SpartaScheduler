package org.example.schedule.controller;

import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.ScheduleResponseDto;
import org.example.schedule.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {

        return new ResponseEntity<>(scheduleService.saveSchedule(requestDto), HttpStatus.CREATED);
    }



    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules() {

        return scheduleService.findAllSchedules();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {

        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    @GetMapping("/writer/{writerName}")
    public ResponseEntity<ScheduleResponseDto> findScheduleByWriter(@PathVariable String writerName) {

        return new ResponseEntity<>(scheduleService.findScheduleByWriter(writerName), HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ScheduleResponseDto>> findScheduleByDate(@PathVariable String date) {
        List<ScheduleResponseDto> schedules = scheduleService.findScheduleByDate(date);

        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedulesFiltered(
            @RequestParam(required = false) String editedDate,
            @RequestParam(required = false) String writerName) {

        List<ScheduleResponseDto> schedules = scheduleService.findAllSchedulesFiltered(editedDate, writerName);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto) {

        // 비밀번호와 함께 요청을 처리
        ScheduleResponseDto updatedSchedule = scheduleService.updateSchedule(id, requestDto.getTask(), requestDto.getWriterName(), requestDto.getPassword());
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateWriterName(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {

        return new ResponseEntity<>(scheduleService.updateWriterName(id, requestDto.getWriterName()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto) {

        // 비밀번호와 함께 삭제 요청 처리
        scheduleService.deleteSchedule(id, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
