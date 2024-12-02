package org.example.reopsitory;

import org.example.dtos.groups.GroupDTO;
import org.example.dtos.groups.ReportDTO;
import org.example.dtos.groups.StudentReportDTO;
import org.example.model.Group;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select new org.example.dtos.groups.GroupDTO(g.id, g.subject) from Group g " +
            "where ?1 member of g.students order by g.subject")
    List<GroupDTO> getGroupsByStudent(Student student);

    @Query("select s.id, s.name, s.lastname, s.index from Group g inner join g.students s where g.id = ?1" +
            " order by s.lastname")
    public List<Object[]> getStudentsByGroup(Long groupId);

    public Group findById(long id);

    @Query("select l.id, l.topic from Group g inner join g.lessons l where g.id = ?1 order by l.date")
    public List<Object[]> getLessons(long groupId);

    @Query("select new org.example.dtos.groups.ReportDTO(g.id, g.subject)" +
            " from Group g where g.id = ?1")
    public ReportDTO generateReport(long groupId);

    @Query("select new org.example.dtos.groups.StudentReportDTO(s.name, s.lastname, s.index)" +
            " from Group g join g.students s where g.id = ?1")
    public List<StudentReportDTO> getStudentsReport(long groupId);
}
