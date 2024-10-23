package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.dtos.ActivityDTO;
import org.example.dtos.ActivityPlotDTO;
import org.example.dtos.ActivityRankingDTO;
import org.example.model.Activity;
import org.example.model.Group;
import org.example.model.Lesson;
import org.example.model.Student;
import org.example.reopsitory.ActivityRepository;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityRepository activityRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public ActivityController(ActivityRepository activityRepository, LessonRepository lessonRepository,
                              StudentRepository studentRepository, GroupRepository groupRepository) {
        this.activityRepository = activityRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @PostMapping("/add")
    @Transactional
//    @Modifying
    public int addActivity(@RequestParam("lessonId") long lessonId, @RequestParam("studentId") long studentId,
                           @RequestParam("date") String dateString, @RequestParam("points") int points) {
        Lesson lesson = lessonRepository.findById(lessonId);
        Student student = studentRepository.findById(studentId);
        LocalDateTime dateTime = LocalDateTime.parse(dateString);

        Activity activity = new Activity();
        activity.setLesson(lesson);
        activity.setStudent(student);
        activity.setDate(dateTime);
        activity.setPoints(points);
        activityRepository.save(activity);
        return activityRepository.getStudentsPointsInLesson(studentId, lessonId);
    }

    // TODO -> POST or GET (sensitive data)
    @GetMapping("/ranking")
    @Transactional
    public List<ActivityRankingDTO> getRanking(@RequestParam("groupId") long groupId) {
        Group group = groupRepository.findById(groupId);
        if (group == null) {
            return new ArrayList<>();
        }
        String subject = group.getSubject();
        return activityRepository.getRanking(subject);
    }

    @GetMapping("/groupRanking")
    @Transactional
    public List<ActivityRankingDTO> getGroupRanking(@RequestParam("groupId") long groupId) {
        return activityRepository.getGroupRanking(groupId);
    }

    @PostMapping("/studentHistory")
    @Transactional
    public List<ActivityDTO> getStudentHistory(@RequestParam("studentId") long studentId, @RequestParam("groupId") long groupId) {
        return activityRepository.getStudentActivityHistory(studentId, groupId);
    }

    @PostMapping("/plot")
    @Transactional
    public List<ActivityPlotDTO> getPlot(@RequestParam("studentId") long studentId, @RequestParam("groupId") long groupId) {
        return activityRepository.getStudentActivities(studentId, groupId);
    }
}
