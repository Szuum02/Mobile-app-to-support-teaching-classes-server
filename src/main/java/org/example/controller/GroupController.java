package org.example.controller;

import org.example.dtos.groups.ReportDTO;
import org.example.dtos.groups.StudentReportDTO;
import org.example.reopsitory.ActivityRepository;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.PresenceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    private GroupRepository groupRepository;
    private PresenceRepository presenceRepository;

    public GroupController(GroupRepository groupRepository, PresenceRepository presenceRepository) {
        this.groupRepository = groupRepository;
        this.presenceRepository = presenceRepository;
    }

    @GetMapping("/generateReport")
    public ReportDTO generateReport(@RequestParam("groupId") long groupId) {
        ReportDTO report = groupRepository.generateReport(groupId);
        List<StudentReportDTO> studentReports = groupRepository.getStudentsReport(groupId);
        for (StudentReportDTO studentReport : studentReports) {
            studentReport.setPresences(presenceRepository.getStudentPresenceByIndex(studentReport.getIndex(), groupId));
        }
        report.setStudentReports(studentReports);
        return report;
    }
}
