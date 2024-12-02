package org.example.reopsitory;

import org.example.dtos.student.StudentDTO;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    public Student findById(long id);

    @Query("select new org.example.dtos.student.StudentDTO(s.id, s.name, s.lastname, s.index, s.nick, s.showInRanking) from Student s " +
            "where s.id = ?1")
    StudentDTO getStudentAfterLogin(long studentId);

    @Query("SELECT g.id, g.subject FROM Student s join s.groups g WHERE s.id = ?1")
    List<Object[]> getStudentsGroup(long studentId);

    Student findByIndex(int index);

    Student findByNick(String nick);
}
