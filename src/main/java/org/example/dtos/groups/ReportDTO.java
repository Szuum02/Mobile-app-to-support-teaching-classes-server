package org.example.dtos.groups;

import java.util.List;

public class ReportDTO {
    private final Long groupId;
    private final String subject;
    private List<StudentReportDTO> studentReports;

    public ReportDTO(Long groupId, String subject) {
        this.groupId = groupId;
        this.subject = subject;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getSubject() {
        return subject;
    }

    public List<StudentReportDTO> getStudentReports() {
        return studentReports;
    }

    public void setStudentReports(List<StudentReportDTO> studentReports) {
        this.studentReports = studentReports;
    }
}
