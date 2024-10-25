package org.example.dtos;

public class ActivityPlotDTO {
    private final String topic;
    private final long points;

    public ActivityPlotDTO(String topic, long points) {
        this.topic = topic;
        this.points = points;
    }

    public String getTopic() {
        return topic;
    }

    public long getPoints() {
        return points;
    }
}
