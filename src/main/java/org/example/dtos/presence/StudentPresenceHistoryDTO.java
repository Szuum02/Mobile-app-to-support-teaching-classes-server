package org.example.dtos.presence;

import java.util.List;

public class StudentPresenceHistoryDTO {
    private final String name;
    private final String lastname;
    private final Integer index;
    private List<PresenceDTO> presences;

    public StudentPresenceHistoryDTO(String name, String lastname, Integer index, List<PresenceDTO> presences) {
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.presences = presences;
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
}
