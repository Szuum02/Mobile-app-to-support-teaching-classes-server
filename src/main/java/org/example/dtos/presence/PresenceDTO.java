package org.example.dtos.presence;

import org.example.model.PresenceType;

import java.time.LocalDateTime;

public class PresenceDTO {
    private final LocalDateTime date;
    private final PresenceType presenceType;

    public PresenceDTO(LocalDateTime date, PresenceType presenceType) {
        this.date = date;
        this.presenceType = presenceType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public PresenceType getPresenceType() {
        return presenceType;
    }
}
