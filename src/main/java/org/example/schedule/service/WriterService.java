package org.example.schedule.service;

import org.example.schedule.dto.WriterDto;

public interface WriterService {
    Long createWriter(String name, String email);
    WriterDto getWriterById(Long id);
}
