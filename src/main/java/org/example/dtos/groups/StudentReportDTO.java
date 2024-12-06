package org.example.dtos.groups;

import org.example.dtos.ActivityDTO;
import org.example.dtos.presence.PresenceDTO;

import java.util.List;

public class StudentReportDTO {
    private final String name;
    private final String lastname;
    private final Integer index;
    private List<PresenceDTO> presences;
    private final Long totalPoints;

    public StudentReportDTO(String name, String lastname, Integer index, Long totalPoints) {
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.totalPoints = totalPoints;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public Integer getIndex() {
        return index;
    }

    public List<PresenceDTO> getPresences() {
        return presences;
    }

    public void setPresences(List<PresenceDTO> presences) {
        this.presences = presences;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }
}
