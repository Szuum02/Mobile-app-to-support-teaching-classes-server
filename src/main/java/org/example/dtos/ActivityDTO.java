package org.example.dtos;

import java.time.LocalDateTime;

public class ActivityDTO {
    private final LocalDateTime date;
    private final int points;

    public ActivityDTO(LocalDateTime date, int points) {
        this.date = date;
        this.points = points;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getPoints() {
        return points;
    }
}
