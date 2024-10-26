package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.dtos.student.StudentDTO;
import org.example.model.Student;
import org.example.model.Teacher;
import org.example.model.User;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.StudentRepository;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public StudentController(UserRepository userRepository, StudentRepository studentRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @PostMapping("/login")
    public StudentDTO getStudentAfterLogin(@RequestParam Long studentId) {
        Student student = studentRepository.findById(studentId).get();
        StudentDTO studentDTO = studentRepository.getStudentAfterLogin(studentId);
        studentDTO.setGroups(groupRepository.getGroupsByStudent(student));
        return studentDTO;
    }

    @GetMapping("/add")
    public ResponseEntity<Student> createTeacher(@RequestParam Long id) {
        Student student = new Student();
        student.setName("Kacper");
        student.setLastname("Urbański");
        student.setIndex(123456);
        student.setNick("My_nick");
        User user = userRepository.findById(id).get();
        user.setStudent(student);
        student.setUser(user);
        studentRepository.save(student);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/showGroups")
    @Transactional
    public List<Object[]> getGroups(@RequestParam("studentId") long studentId) {
        return studentRepository.getStudentsGroup(studentId);
    }
}
