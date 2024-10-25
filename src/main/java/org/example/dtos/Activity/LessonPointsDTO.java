package org.example.dtos.Activity;

public class LessonPointsDTO {
    private final Long studentId;
    private final long points;

    public LessonPointsDTO(Long studentId, long points) {
        this.studentId = studentId;
        this.points = points;
    }

    public Long getStudentId() {
        return studentId;
    }

    public long getPoints() {
        return points;
    }
}
