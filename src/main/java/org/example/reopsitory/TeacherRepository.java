package org.example.reopsitory;

import org.example.dtos.teacher.TeacherDTO;
import org.example.model.Group;
import org.example.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("select new org.example.dtos.teacher.TeacherDTO(t.id, t.name, t.lastname) " +
            "from Teacher t where t.id = ?1")
    TeacherDTO getTeacherAfterLogin(Long teacherId);
}
