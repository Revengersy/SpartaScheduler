package org.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WriterDto {
    private Long id;
    private String name;
    private String email;  // 새로운 필드 추가

}