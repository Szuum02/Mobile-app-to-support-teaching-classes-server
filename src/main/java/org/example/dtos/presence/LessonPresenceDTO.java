package org.example.dtos.presence;

import org.example.model.PresenceType;

public class LessonPresenceDTO {
    private final Long studentId;
    private final PresenceType presenceType;

    public LessonPresenceDTO(Long studentId, PresenceType presenceType) {
        this.studentId = studentId;
        this.presenceType = presenceType;
    }

    public Long getStudentId() {
        return studentId;
    }

    public PresenceType getPresenceType() {
        return presenceType;
    }
}
