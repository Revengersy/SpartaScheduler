package org.example.schedule.repository;

import org.example.schedule.entity.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateWriterRepository implements WriterRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateWriterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long saveWriter(Writer writer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("writer").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("writer_name", writer.getName()); // Fix the column name to match the DB schema
        parameters.put("email", writer.getEmail());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        writer.setId(key.longValue());
        return key.longValue();  // Return generated ID
    }

    public Optional<Writer> findWriterById(Long id) {
        List<Writer> result = jdbcTemplate.query(
                "SELECT * FROM writer WHERE id = ?",
                writerRowMapper(),
                id
        );
        return result.stream().findAny();
    }


    private RowMapper<Writer> writerRowMapper() {
        return (rs, rowNum) -> {
            Writer writer = new Writer();
            writer.setId(rs.getLong("id"));
            writer.setName(rs.getString("writer_name"));
            writer.setEmail(rs.getString("email"));  // 새로운 필드

            // 변환: Timestamp -> ZonedDateTime
            Timestamp registeredTimestamp = rs.getTimestamp("registered_date");
            if (registeredTimestamp != null) {
                writer.setRegisteredDate(registeredTimestamp.toInstant().atZone(ZoneId.systemDefault()));
            }

            Timestamp updatedTimestamp = rs.getTimestamp("updated_date");
            if (updatedTimestamp != null) {
                writer.setUpdatedDate(updatedTimestamp.toInstant().atZone(ZoneId.systemDefault()));
            }

            return writer;
        };
    }
}
