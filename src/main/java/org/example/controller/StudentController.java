package org.example.controller;

import org.example.dtos.lesson.LessonDTO;
import org.example.dtos.student.ShowInRankingDTO;
import org.example.dtos.student.StudentDTO;
import org.example.model.Group;
import org.example.model.Lesson;
import org.example.model.Student;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;

    public StudentController(StudentRepository studentRepository, LessonRepository lessonRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
    }

    @PostMapping("/login")
    public StudentDTO getStudentAfterLogin(@RequestParam Long studentId) {
        StudentDTO studentDTO = studentRepository.getStudentAfterLogin(studentId);
        List<Lesson> lessons = lessonRepository.findLessonByStudentId(studentId);

        studentDTO.setLessons(convertLessonsToMap(lessons));
        return studentDTO;
    }

//    @PostMapping("/add")
//    public StudentDTO createStudent(@RequestParam Long id, @RequestParam String name,
//                                    @RequestParam String lastName, @RequestParam Integer index, @RequestParam String nick) {
//        Student student = new Student();
//        student.setName(name);
//        student.setLastname(lastName);
//        student.setIndex(index);
//        student.setNick(nick);
//        student.setShowInRanking(true);
//        User user = userRepository.findById(id).get();
//        user.setStudent(student);
//        student.setUser(user);
//        studentRepository.save(student);
//
//        StudentDTO studentDTO = new StudentDTO(id, name, lastName, index, nick, true);
//        studentDTO.setLessons(new HashMap<>());
//        return studentDTO;
//    }

    @PostMapping("/changeShowInRanking")
    public ShowInRankingDTO showInRanking(@RequestParam("studentId") long studentId,
                                          @RequestParam("showInRanking") boolean showInRanking) {
        Student student = studentRepository.findById(studentId);
        student.setShowInRanking(showInRanking);
        studentRepository.save(student);
        return new ShowInRankingDTO(student.getId(), student.isShowInRanking());
    }

    @PostMapping("addGroup")
    public List<LessonDTO> addStudentToGroup(@RequestParam("studentId") Long studentId, @RequestParam("groupCode") String groupCode) {
        Group group = groupRepository.findGroupByGroupCode(groupCode);
        if (group == null) {
            return new ArrayList<>();
        }
        Student student = studentRepository.findById(studentId).get();
        group.getStudents().add(student);
        student.getGroups().add(group);
        studentRepository.save(student);
        groupRepository.save(group);
        return lessonRepository.findLessonByGroupId(group.getId());
    }

    private Map<LocalDate, List<LessonDTO>> convertLessonsToMap(List<Lesson> lessons) {
        Map<LocalDate, List<LessonDTO>> lessonsMap = new HashMap<>();

        for (Lesson lesson : lessons) {
            LocalDate date = lesson.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (!lessonsMap.containsKey(date)) {
                lessonsMap.put(date, new ArrayList<>());
            }

            lessonsMap.get(date).add(
                    new LessonDTO(lesson.getId(), lesson.getDate(), lesson.getClassroom(), lesson.getTopic(), lesson.getGroup().getId(), lesson.getGroup().getGroupCode())
            );
        }
        return lessonsMap;
    }
}
