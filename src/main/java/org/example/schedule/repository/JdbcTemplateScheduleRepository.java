package org.example.schedule.repository;


import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.ScheduleResponseDto;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.Writer;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", schedule.getTask());
        parameters.put("writer_id", schedule.getWriterName());
        parameters.put("password", schedule.getPassword());
        parameters.put("written_time", schedule.getWrittenTime());
        parameters.put("edited_time", schedule.getEditedTime());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        schedule.setId(key.longValue());

        return new ScheduleResponseDto(key.longValue(), schedule.getTask(), schedule.getWriterName(), schedule.getEditedTime(), schedule.getWrittenTime());
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return jdbcTemplate.query("select * from schedule", scheduleRowMapper());
    }

    @Override
    public Optional<Schedule> findScheduleById(Long id) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where id=?", scheduleRowMapperV2(), id);
        return result.stream().findAny();
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByDate(LocalDate date) {
        return jdbcTemplate.query(
                "SELECT * FROM schedule WHERE DATE(written_time) = ?",
                scheduleRowMapper(),
                java.sql.Date.valueOf(date)  // LocalDate를 java.sql.Date로 변환하여 사용
        );
    }

    @Override
    public List<ScheduleResponseDto> findAllByFilters(LocalDate editedDate, String writerName) {
        String query = "SELECT * FROM schedule WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (editedDate != null) {
            query += " AND DATE(edited_time) = ?";
            params.add(java.sql.Date.valueOf(editedDate));
        }

        if (writerName != null && !writerName.isEmpty()) {
            query += " AND writer_id = ?";
            params.add(writerName);
        }

        query += " ORDER BY edited_time DESC";

        return jdbcTemplate.query(query, scheduleRowMapper(), params.toArray());
    }

    @Override
    public Schedule findScheduleByWriterOrElseThrow(String writerName) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where writer_id=?", scheduleRowMapperV2(), writerName);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist writerName " + writerName));
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where id=?", scheduleRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id " + id));
    }



    public int updateScheduleDetails(Schedule schedule) {
        return jdbcTemplate.update(
                "UPDATE schedule SET task = ?, writer_name = ?, edited_time = ? WHERE id = ?",
                schedule.getTask(), schedule.getWriterName(), schedule.getEditedTime(), schedule.getId()
        );
    }

    @Override
    public int updateWriterName(Long id, String writerName) {
        return jdbcTemplate.update("update schedule set writer_name = ? where id = ?", writerName, id);
    }

    @Override
    public int deleteSchedule(Long id) {
        return jdbcTemplate.update("delete from schedule where id = ?", id);
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),                         // id
                        rs.getString("task"),                     // task
                        rs.getString("writer_id"),              // writer_name
                        rs.getObject("edited_time", ZonedDateTime.class), // edited_time
                        rs.getObject("written_time", ZonedDateTime.class) // written_time
                );
            }
        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),                         // id
                        rs.getString("task"),                     // task
                        rs.getString("writer_id"),              // writer_name
                        rs.getString("password")                 // password
                );
            }
        };
    }


}
