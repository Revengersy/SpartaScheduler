package org.example.schedule.repository;

import org.example.schedule.entity.Writer;

import java.util.Optional;

public interface WriterRepository {

    Long saveWriter(Writer writer);
    Optional<Writer> findWriterById(Long id);
}
