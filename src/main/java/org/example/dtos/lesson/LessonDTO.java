package org.example.dtos.lesson;

import java.util.Date;

public class LessonDTO {
    private final Long lessonId;
    private final Date date;
    private final String classroom;
    private final String topic;
    private final Long groupId;
    private final String groupCode;

    public LessonDTO(Long lessonId, Date date, String classroom, String topic, Long groupId, String groupCode) {
        this.lessonId = lessonId;
        this.date = date;
        this.classroom = classroom;
        this.topic = topic;
        this.groupId = groupId;
        this.groupCode = groupCode;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public Date getDate() {
        return date;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getTopic() {
        return topic;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getGroupCode() {
        return groupCode;
    }
}
