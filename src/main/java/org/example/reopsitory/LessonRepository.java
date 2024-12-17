package org.example.reopsitory;

import org.example.dtos.lesson.LessonDTO;
import org.example.model.Group;
import org.example.model.Lesson;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson findById(long lessonId);

    @Query("select l from Lesson l where l.group.teacher.id = ?1")
    List<Lesson> findLessonsByTeacherId(long teacherId);

    @Query("select l from Lesson l join l.group.students s where s.id = ?1")
    List<Lesson> findLessonByStudentId(long studentId);

    @Query("select s.id, s.name, s.lastname, s.index from Lesson l inner join l.group g inner join g.students s " +
            "where l.id = ?1 order by s.lastname")
    List<Object[]> findStudentsByLesson(long lessonId);

    @Query("select new org.example.dtos.lesson.LessonDTO(l.id, l.date, l.classroom, l.topic, l.group.id, l.group.groupCode) from Lesson l where l.group.id = ?1")
    List<LessonDTO> findLessonByGroupId(Long groupId);
}
