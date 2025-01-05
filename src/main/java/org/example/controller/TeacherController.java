package org.example.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.dtos.groups.AddGroupDTO;
import org.example.dtos.lesson.AddLessonDTO;
import org.example.dtos.lesson.LessonDTO;
import org.example.dtos.teacher.TeacherDTO;
import org.example.model.Group;
import org.example.model.Lesson;
import org.example.model.Teacher;
import org.example.model.User;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.TeacherRepository;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;

    public TeacherController(TeacherRepository teacherRepository, LessonRepository lessonRepository, GroupRepository groupRepository) {
        this.teacherRepository = teacherRepository;
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
    }

    @PostMapping("/login")
    public TeacherDTO teacherLogin(@RequestParam Long id) {
        TeacherDTO teacher = teacherRepository.getTeacherAfterLogin(id);

        List<Lesson> lessons = lessonRepository.findLessonsByTeacherId(id);

        teacher.setLessons(convertLessonsToMap(lessons));
        return teacher;
    }

//    @PostMapping("/add")
//    public TeacherDTO createTeacher(@RequestParam Long id, @RequestParam String name, @RequestParam String lastName) {
//        Teacher teacher = new Teacher();
//        teacher.setName(name);
//        teacher.setLastname(lastName);
//        User user = userRepository.findById(id).get();
//        user.setTeacher(teacher);
//        teacher.setUser(user);
//        teacherRepository.save(teacher);
//
//        TeacherDTO teacherDTO = new TeacherDTO(id, name, lastName);
//        teacherDTO.setLessons(new HashMap<>());
//        return teacherDTO;
//    }

    @PutMapping("/addLessons")
    public void addLessons(@RequestBody List<AddGroupDTO> groups, @RequestParam Long teacherId) {
        String code;

        Teacher teacher = teacherRepository.findById(teacherId).get();

        for (AddGroupDTO groupDTO : groups) {
            do {
                code = RandomStringUtils.random(8, true, true);
            } while (groupRepository.findGroupByGroupCode(code) != null);

            Group group = setUpGroup(groupDTO, code, teacher);
            groupRepository.save(group);

            for (AddLessonDTO lessonDTO : groupDTO.getLessons()) {
                Lesson lesson = setUpLesson(lessonDTO, group);
                lessonRepository.save(lesson);
                group.getLessons().add(lesson);
            }
        }

    }

    private Group setUpGroup(AddGroupDTO groupDTO, String code, Teacher teacher) {
        Group group = new Group();
        group.setSubjectCode(groupDTO.getSubjectCode());
        group.setSubject(groupDTO.getSubject());
        group.setGroupNumber(groupDTO.getGroupNumber());
        group.setGroupCode(code);
        group.setTeacher(teacher);
        group.setLessons(new HashSet<>());
        group.setStudents(new HashSet<>());
        return group;
    }

    private Lesson setUpLesson(AddLessonDTO lessonDTO, Group group) {
        Lesson lesson = new Lesson();
        lesson.setDate(Date.from(LocalDateTime.parse(lessonDTO.getDate()).atZone(ZoneId.systemDefault()).toInstant()));
        lesson.setClassroom(lessonDTO.getClassroom());
        lesson.setGroup(group);
        lesson.setTopic(group.getSubject());
        lesson.setActivities(new HashSet<>());
        lesson.setPresences(new HashSet<>());
        return lesson;
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
