package org.example.dtos.groups;

import org.example.dtos.lesson.AddLessonDTO;

import java.util.Set;

public class AddGroupDTO {
    private String subjectCode;
    private Integer groupNumber;
    private String subject;
    private Set<AddLessonDTO> lessons;

    public AddGroupDTO(String subjectCode, String subject, Integer groupNumber) {
        this.subjectCode = subjectCode;
        this.subject = subject;
        this.groupNumber = groupNumber;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Set<AddLessonDTO> getLessons() {
        return lessons;
    }

    public void setLessons(Set<AddLessonDTO> lessons) {
        this.lessons = lessons;
    }
}
