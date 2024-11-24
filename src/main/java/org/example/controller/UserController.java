package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.dtos.student.StudentDTO;
import org.example.dtos.teacher.TeacherDTO;
import org.example.dtos.user.UserDTO;
import org.example.model.Student;
import org.example.model.Teacher;
import org.example.model.User;
import org.example.reopsitory.StudentRepository;
import org.example.reopsitory.TeacherRepository;
import org.example.reopsitory.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public UserController(UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/get")
    public User getAllUsers(@RequestParam Long id) {
//        List<User> users = userRepository.findAll();
        return userRepository.findById(id).get();
    }

    @PostMapping("/login")
    public UserDTO findUserByMail(@RequestParam String mail, @RequestParam String password) {
        UserDTO user = userRepository.findUserByMail(mail, password);
        if (user == null) {
            user = new UserDTO(-1L, false);
        }
        return user;
    }

    @PostMapping("/add")
    public Long addUser(@RequestParam String mail, @RequestParam String password, @RequestParam boolean isStudent) {
        User user = new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setIsStudent(isStudent);
        userRepository.save(user);
        return user.getId();
    }

//    @GetMapping("/{mail}")
//    public ResponseEntity<Optional<User>> test(@PathVariable("mail") String mail) {
//        Optional<User> user = userRepository.findById(mail);
//        return ResponseEntity.ok(user);
//    }

    @GetMapping("/teacher/showAll")
    public List<User> findTeachers() {
//        List<User> users = userRepository.findAllTeacher();
        return userRepository.findAllTeacher();
    }

    @GetMapping("/students/showAll")
    public List<User> findStudents() {
//        List<User> users = userRepository.findAllStudents();
        return userRepository.findAllStudents();
    }

    @GetMapping("/checkUniqueMail")
    public boolean checkUniqueMail(@RequestParam String mail) {
        return userRepository.findUserByMail(mail) == null;
    }

    @GetMapping("/checkUniqueValues")
    public String checkUniqueMailAndIndex(@RequestParam String mail, @RequestParam Integer index, @RequestParam String nick) {
        if (userRepository.findUserByMail(mail) != null) {
            return "mail";
        }
        if (studentRepository.findByIndex(index) != null) {
            return "index";
        }
        if (studentRepository.findByNick(nick) != null) {
            return "nick";
        }
        return "ok";
    }

    @Transactional
    @PostMapping("/addTeacher")
    public TeacherDTO addTeacher(@RequestParam String name, @RequestParam String lastName,
                           @RequestParam String mail, @RequestParam String password) {
        User user = inputUser(mail, password, false);
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setLastname(lastName);
        teacher.setUser(user);
        user.setTeacher(teacher);
        teacher.setUser(user);
        teacherRepository.save(teacher);
        userRepository.save(user);

        TeacherDTO teacherDTO = new TeacherDTO(user.getId(), name, lastName);
        teacherDTO.setLessons(new HashMap<>());
        return teacherDTO;
    }

    @Transactional
    @PostMapping("/addStudent")
    public StudentDTO addStudent(@RequestParam String name, @RequestParam String lastName,
                                 @RequestParam String nick, @RequestParam Integer index,
                                 @RequestParam String mail, @RequestParam String password) {
        User user = inputUser(mail, password, true);
        Student student = new Student();
        student.setName(name);
        student.setLastname(lastName);
        student.setIndex(index);
        student.setNick(nick);
        user.setStudent(student);
        student.setUser(user);
        studentRepository.save(student);
        userRepository.save(user);
        StudentDTO studentDTO = new StudentDTO(user.getId(), name, lastName, index, nick);
        studentDTO.setGroups(new ArrayList<>());
        return studentDTO;
    }

    private User inputUser(String mail, String password, boolean isStudent) {
        User user = new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setIsStudent(isStudent);
        userRepository.save(user);
        return user;
    }
}
