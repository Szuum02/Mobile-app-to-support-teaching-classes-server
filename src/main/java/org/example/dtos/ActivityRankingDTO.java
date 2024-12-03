package org.example.dtos;

public class ActivityRankingDTO {
    private final Long id;
    private final String nick;
    private final Boolean showInRanking;
    private final long totalPoints;
    private Long todayPoints;

    public ActivityRankingDTO(Long id, String nick, Boolean showInRanking, long totalPoints, Long todayPoints) {
        this.id = 1L;
        this.nick = nick;
        this.showInRanking = showInRanking;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

    public ActivityRankingDTO(Long id, String nick, Boolean showInRanking, long totalPoints) {
        this.id = id;
        this.nick = nick;
        this.showInRanking = showInRanking;
        this.totalPoints = totalPoints;
    }

    public Long getId() {
        return id;
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
