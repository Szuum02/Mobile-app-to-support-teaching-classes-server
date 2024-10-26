package org.example.dtos.student;

import org.example.dtos.groups.GroupDTO;

import java.util.List;

public class StudentDTO {
    private final Long id;
    private final String name;
    private final String lastname;
    private final Integer index;
    private final String nick;
    private List<GroupDTO> groups;

    public StudentDTO(Long id, String name, String lastname, Integer index, String nick) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.nick = nick;
    }

    public Long getId() {
        return id;
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

    public String getNick() {
        return nick;
    }

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }
}
