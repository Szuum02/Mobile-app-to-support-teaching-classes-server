package org.example.dtos.user;

public class UserDTO {
    private final Long id;
    private final boolean isStudent;

    public UserDTO(Long id, boolean isStudent) {
        this.id = id;
        this.isStudent = isStudent;
    }

    public Long getId() {
        return id;
    }

    public boolean isStudent() {
        return isStudent;
    }
}
