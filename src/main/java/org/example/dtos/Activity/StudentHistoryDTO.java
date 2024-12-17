package org.example.dtos.Activity;

import org.example.dtos.ActivityDTO;

import java.util.List;

public class StudentHistoryDTO {
    private final String name;
    private final String lastname;
    private final Integer index;
    private List<ActivityDTO> activities;

    public StudentHistoryDTO(String name, String lastname, Integer index, List<ActivityDTO> activities) {
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.activities = activities;
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

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }
}
