package org.example.dtos;

public class ActivityRankingDTO {
    private final String nick;
    private final long totalPoints;
    private Long todayPoints;

    public ActivityRankingDTO(String nick, long totalPoints, Long todayPoints) {
        this.nick = nick;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

    public ActivityRankingDTO(String nick, long totalPoints) {
        this.nick = nick;
        this.totalPoints = totalPoints;
    }

    public String getNick() {
        return nick;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    public Long getTodayPoints() {
        return todayPoints;
    }
}
