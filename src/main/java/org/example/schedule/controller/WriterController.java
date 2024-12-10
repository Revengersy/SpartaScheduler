package org.example.schedule.controller;

import org.example.schedule.dto.WriterDto;
import org.example.schedule.service.WriterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/writers")
public class WriterController {

    private final WriterService writerService;

    public WriterController(WriterService writerService) {
        this.writerService = writerService;
    }

    // 작성자 생성 엔드포인트
    @PostMapping
    public ResponseEntity<Long> createWriter(@RequestBody WriterDto writerDto) {
        Long writerId = writerService.createWriter(writerDto.getName(), writerDto.getEmail());
        return new ResponseEntity<>(writerId, HttpStatus.CREATED);
    }

    // 작성자 ID로 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<WriterDto> getWriterById(@PathVariable Long id) {
        WriterDto writer = writerService.getWriterById(id);
        return new ResponseEntity<>(writer, HttpStatus.OK);
    }
}