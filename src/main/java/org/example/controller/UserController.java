package org.example.controller;

import org.example.dtos.user.UserDTO;
import org.example.model.User;
import org.example.reopsitory.StudentRepository;
import org.example.reopsitory.TeacherRepository;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/showAll")
    public List<User> getAllUsers() {
//        List<User> users = userRepository.findAll();
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public UserDTO findUserByMail(@RequestParam String mail, @RequestParam String password) {
        UserDTO user = userRepository.findUserByMail(mail, password);
        if (user == null) {
            // TODO -> handle wrong email password
            return null;
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
}
