package org.example.schedule.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Writer {
    private Long id;
    private String name;
    private String email;
    private ZonedDateTime registeredDate;
    private ZonedDateTime updatedDate;

}