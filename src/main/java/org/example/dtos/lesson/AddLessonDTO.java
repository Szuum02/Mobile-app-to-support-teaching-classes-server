package org.example.dtos.lesson;

public class AddLessonDTO {
    private String date;
    private String classroom;

    public AddLessonDTO(String date, String classroom) {
        this.date = date;
        this.classroom = classroom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
