package org.example.dtos.student;

import org.example.dtos.lesson.LessonDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StudentDTO {
    private final Long id;
    private final String name;
    private final String lastname;
    private final Integer index;
    private final String nick;
    private final boolean showInRanking;

    private Map<LocalDate, List<LessonDTO>> lessons;

    public StudentDTO(Long id, String name, String lastname, Integer index, String nick, boolean showInRanking) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.nick = nick;
        this.showInRanking = showInRanking;
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

    public Integer getIndex() {
        return index;
    }

    public String getNick() {
        return nick;
    }

    public boolean isShowInRanking() {
        return showInRanking;
    }

    public Map<LocalDate, List<LessonDTO>> getLessons() {
        return lessons;
    }

    public void setLessons(Map<LocalDate, List<LessonDTO>> lessons) {
        this.lessons = lessons;
    }
}
