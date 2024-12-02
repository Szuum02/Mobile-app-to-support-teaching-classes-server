package org.example.dtos;

public class ActivityRankingDTO {
    private final String nick;
    private final Boolean showInRanking;
    private final long totalPoints;
    private Long todayPoints;

    public ActivityRankingDTO(String nick, Boolean showInRanking, long totalPoints, Long todayPoints) {
        this.nick = nick;
        this.showInRanking = showInRanking;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

    public ActivityRankingDTO(String nick, Boolean showInRanking, long totalPoints) {
        this.nick = nick;
        this.showInRanking = showInRanking;
        this.totalPoints = totalPoints;
    }

    public String getNick() {
        return nick;
    }

    public Boolean getShowInRanking() {
        return showInRanking;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    public Long getTodayPoints() {
        return todayPoints;
    }
}
