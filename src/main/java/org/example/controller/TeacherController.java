package org.example.controller;

import org.example.dtos.lesson.LessonDTO;
import org.example.dtos.teacher.TeacherDTO;
import org.example.model.Lesson;
import org.example.model.Teacher;
import org.example.model.User;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.TeacherRepository;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    public TeacherController(TeacherRepository teacherRepository, UserRepository userRepository, LessonRepository lessonRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @PostMapping("/login")
    public TeacherDTO teacherLogin(@RequestParam Long id) {
        TeacherDTO teacher = teacherRepository.getTeacherAfterLogin(id);

        List<Lesson> lessons = lessonRepository.findLessonsByTeacherId(id);

        teacher.setLessons(convertLessonsToMap(lessons));
        return teacher;
    }

    @PostMapping("/add")
    public TeacherDTO createTeacher(@RequestParam Long id, @RequestParam String name, @RequestParam String lastName) {
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setLastname(lastName);
        User user = userRepository.findById(id).get();
        user.setTeacher(teacher);
        teacher.setUser(user);
        teacherRepository.save(teacher);

        TeacherDTO teacherDTO = new TeacherDTO(id, name, lastName);
        teacherDTO.setLessons(new HashMap<>());
        return teacherDTO;
    }

    @GetMapping("/showGroups")

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
