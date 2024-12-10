package org.example.schedule.service;
import org.example.schedule.dto.WriterDto;
import org.example.schedule.entity.Writer;
import org.example.schedule.repository.WriterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WriterServiceImpl implements WriterService {

    private final WriterRepository writerRepository;

    public WriterServiceImpl(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    @Transactional
    @Override
    public Long createWriter(String name, String email) {
        Writer writer = new Writer();
        writer.setName(name);
        writer.setEmail(email);  // 새로운 필드 설정

        return writerRepository.saveWriter(writer);
    }

    @Override
    public WriterDto getWriterById(Long id) {
        Writer writer = writerRepository.findWriterById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Writer not found"));

        return new WriterDto(writer.getId(), writer.getName(), writer.getEmail());
    }
}