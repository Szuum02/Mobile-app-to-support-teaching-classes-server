package org.example.dtos.teacher;

import org.example.dtos.lesson.LessonDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeacherDTO {
    private final Long id;
    private final String name;
    private final String lastname;

    private Map<LocalDate, List<LessonDTO>> lessons;

    public TeacherDTO(Long id, String name, String lastname) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public Map<LocalDate, List<LessonDTO>> getLessons() {
        return lessons;
    }

    public void setLessons(Map<LocalDate, List<LessonDTO>> lessons) {
        this.lessons = lessons;
    }
}
