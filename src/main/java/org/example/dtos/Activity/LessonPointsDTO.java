package org.example.dtos.Activity;

public class LessonPointsDTO {
    private final Long studentId;
    private final Long totalPoints;
    private final Long todayPoints;

    public LessonPointsDTO(Long studentId, Long totalPoints, Long todayPoints) {
        this.studentId = studentId;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getTodayPoints() {
        return todayPoints;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }
}
