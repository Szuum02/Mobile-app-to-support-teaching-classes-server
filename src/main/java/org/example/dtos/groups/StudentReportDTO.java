package org.example.dtos.groups;

import org.example.dtos.ActivityDTO;
import org.example.dtos.presence.PresenceDTO;

import java.util.List;

public class StudentReportDTO {
    private final String name;
    private final String lastname;
    private final Integer index;
    private List<PresenceDTO> presences;
    private List<ActivityDTO> activities;

    public StudentReportDTO(String name, String lastname, Integer index) {
        this.name = name;
        this.lastname = lastname;
        this.index = index;
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

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }
}
