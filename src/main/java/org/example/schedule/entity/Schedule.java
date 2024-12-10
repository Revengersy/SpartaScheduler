package org.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Schedule {

    private long id;
    private String task;
    private String writerName;
    private String password;
    private ZonedDateTime writtenTime;
    private ZonedDateTime editedTime;

    public Schedule(Long id, String task, String writerName, String password) {
        this.id = id;
        this.task = task;
        this.writerName = writerName;
        this.password = password;
        this.writtenTime = ZonedDateTime.now();
        this.editedTime = this.writtenTime;
    }


    public Schedule(String task, String writerName, String password) {
        this.task = task;
        this.writerName = writerName;
        this.password = password;
        this.writtenTime = ZonedDateTime.now();
        this.editedTime = this.writtenTime;
    }

    public void update(String task, String writerName) {
        this.task = task;
        this.writerName = writerName;
        this.editedTime = getCurrentTime();
    }

    public void updateTask(String task) {
        this.task = task;
        this.editedTime = getCurrentTime();
    }

    public void updateWriterName(String writerName) {
        this.writerName = writerName;
        this.editedTime = getCurrentTime();
    }

    private ZonedDateTime getCurrentTime() {
        return ZonedDateTime.now();
    }
}
