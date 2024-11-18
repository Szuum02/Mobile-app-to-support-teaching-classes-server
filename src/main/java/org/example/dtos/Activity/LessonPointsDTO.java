package org.example.dtos.Activity;

public class LessonPointsDTO {
    private final Long studentId;
    private final long totalPoints;
    private final long todayPoints;

    public LessonPointsDTO(Long studentId, long totalPoints, long todayPoints) {
        this.studentId = studentId;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

    public Long getStudentId() {
        return studentId;
    }

    public long getTodayPoints() {
        return todayPoints;
    }

    public long getTotalPoints() {
        return totalPoints;
    }
}
