package org.example.reopsitory;

import org.example.dtos.student.StudentDTO;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findById(long id);
    Student findByIndex(int index);
    Student findByNick(String nick);

    @Query("select new org.example.dtos.student.StudentDTO(s.id, s.name, s.lastname, s.index, s.nick, s.showInRanking) from Student s " +
            "where s.id = ?1")
    StudentDTO getStudentAfterLogin(long studentId);
}
