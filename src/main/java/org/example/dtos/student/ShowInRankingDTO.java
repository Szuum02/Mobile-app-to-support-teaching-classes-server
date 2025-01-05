package org.example.dtos.student;

public class ShowInRankingDTO {
    private final Long id;
    private final Boolean showInRanking;

    public ShowInRankingDTO(Long id, Boolean showInRanking) {
        this.id = id;
        this.showInRanking = showInRanking;
    }

    public Long getId() {
        return id;
    }

    public Boolean isShowInRanking() {
        return showInRanking;
    }
}
