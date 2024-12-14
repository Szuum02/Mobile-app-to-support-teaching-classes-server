package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.dtos.presence.LessonPresenceDTO;
import org.example.dtos.presence.PresenceDTO;
import org.example.dtos.presence.StudentPresenceHistoryDTO;
import org.example.model.*;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.PresenceRepository;
import org.example.reopsitory.StudentRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/presence")
public class PresenceController {
    private PresenceRepository presenceRepository;
    private LessonRepository lessonRepository;
    private StudentRepository studentRepository;

    public PresenceController(PresenceRepository presenceRepository, LessonRepository lessonRepository, StudentRepository studentRepository) {
        this.presenceRepository = presenceRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/get")
    public List<LessonPresenceDTO> getLessonPresence(@RequestParam Long lessonId) {
        return presenceRepository.getLessonPresence(lessonId);
    }

    @PutMapping("/removeAndAdd")
    @Transactional
    @Modifying
    public void updatePresence(@RequestParam("studentId") long studentId, @RequestParam("lessonId") long lessonId,
                        @RequestParam("date") String date, @RequestParam("presence") String presenceType) {
//        Student student = studentRepository.findById(studentId);
//        Lesson lesson = lessonRepository.findById(lessonId);
//
//        presenceRepository.deletePresenceByLessonAndStudent(lesson, student);
//
//        Presence presence = new Presence();
//        presence.setDate(LocalDateTime.parse(date));
//        presence.setLesson(lesson);
//        presence.setStudent(student);
//        presence.setPresenceType(Presence.getById(Integer.parseInt(presenceType)));
//        presenceRepository.save(presence);

        Presence presence = presenceRepository.findPresenceByLessonIdAndStudentId(lessonId, studentId);
        if (presence == null) {
            Student student = studentRepository.findById(studentId);
            Lesson lesson = lessonRepository.findById(lessonId);
            presence = new Presence();
            presence.setStudent(student);
            presence.setLesson(lesson);
        }
        presence.setDate(LocalDateTime.parse(date));
        presence.setPresenceType(Presence.getById(Integer.parseInt(presenceType)));
        presenceRepository.save(presence);
    }

    @GetMapping("/student/get")
    public StudentPresenceHistoryDTO getStudentsPresences(@RequestParam("studentId") long studentId, @RequestParam("groupId") long groupId) {
        List<PresenceDTO> presences = presenceRepository.getStudentPresence(studentId, groupId);
        Student student = studentRepository.findById(studentId);
        return new StudentPresenceHistoryDTO(student.getName(), student.getLastname(), student.getIndex(), presences);
    }
}
