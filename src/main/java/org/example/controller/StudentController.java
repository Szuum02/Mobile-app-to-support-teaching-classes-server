package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.dtos.lesson.LessonDTO;
import org.example.dtos.student.ShowInRankingDTO;
import org.example.dtos.student.StudentDTO;
import org.example.model.Lesson;
import org.example.model.Student;
import org.example.model.User;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.StudentRepository;
import org.example.reopsitory.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    public StudentController(UserRepository userRepository, StudentRepository studentRepository, LessonRepository lessonRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
    }

    @PostMapping("/login")
    public StudentDTO getStudentAfterLogin(@RequestParam Long studentId) {
        StudentDTO studentDTO = studentRepository.getStudentAfterLogin(studentId);
        List<Lesson> lessons = lessonRepository.findLessonByStudentId(studentId);

        studentDTO.setLessons(convertLessonsToMap(lessons));
        return studentDTO;
    }

    @PostMapping("/add")
    public StudentDTO createStudent(@RequestParam Long id, @RequestParam String name,
                                    @RequestParam String lastName, @RequestParam Integer index, @RequestParam String nick) {
        Student student = new Student();
        student.setName(name);
        student.setLastname(lastName);
        student.setIndex(index);
        student.setNick(nick);
        student.setShowInRanking(true);
        User user = userRepository.findById(id).get();
        user.setStudent(student);
        student.setUser(user);
        studentRepository.save(student);

        StudentDTO studentDTO = new StudentDTO(id, name, lastName, index, nick, true);
        studentDTO.setLessons(new HashMap<>());
        return studentDTO;
    }

    @PostMapping("/changeShowInRanking")
    public ShowInRankingDTO showInRanking(@RequestParam("studentId") long studentId,
                                          @RequestParam("showInRanking") boolean showInRanking) {
        Student student = studentRepository.findById(studentId);
        student.setShowInRanking(showInRanking);
        studentRepository.save(student);
        return new ShowInRankingDTO(student.getId(), student.isShowInRanking());
    }

    private Map<LocalDate, List<LessonDTO>> convertLessonsToMap(List<Lesson> lessons) {
        Map<LocalDate, List<LessonDTO>> lessonsMap = new HashMap<>();

        for (Lesson lesson : lessons) {
            LocalDate date = lesson.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (!lessonsMap.containsKey(date)) {
                lessonsMap.put(date, new ArrayList<>());
            }

            lessonsMap.get(date).add(
                    new LessonDTO(lesson.getId(), lesson.getDate(), lesson.getClassroom(), lesson.getTopic(), lesson.getGroup().getId())
            );
        }
        return lessonsMap;
    }
}
