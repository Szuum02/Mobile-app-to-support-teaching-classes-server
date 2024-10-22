package org.example.dtos;

public class TeacherActivityRankingDTO {
    private final String name;
    private final String lastname;
    private final long totalPoints;
    private Long todayPoints;

    public TeacherActivityRankingDTO(String name, String lastname, long totalPoints, Long todayPoints) {
        this.name = name;
        this.lastname = lastname;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    public Long getTodayPoints() {
        return todayPoints;
    }

    public void setTodayPoints(Long todayPoints) {
        this.todayPoints = todayPoints;
    }
}
