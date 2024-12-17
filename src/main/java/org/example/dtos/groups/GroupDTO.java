package org.example.dtos.groups;


import org.example.dtos.lesson.LessonDTO;

import java.util.Set;

public class GroupDTO {
    private final Long groupId;
    private final String subject;

    public GroupDTO(Long groupId, String subject) {
        this.groupId = groupId;
        this.subject = subject;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getSubject() {
        return subject;
    }
}
