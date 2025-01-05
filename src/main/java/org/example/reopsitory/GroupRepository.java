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
    public Group findById(long id);
    public Group findGroupByGroupCode(String groupCode);

    @Query("select new org.example.dtos.groups.ReportDTO(g.id, g.subject)" +
            " from Group g where g.id = ?1")
    public ReportDTO generateReport(long groupId);

    @Query("select new org.example.dtos.groups.StudentReportDTO(s.name, s.lastname, s.index, sum(a.points))" +
            " from Group g join g.students s join s.activities a where g.id = ?1 and a.lesson.group.id = ?1 group by s")
    public List<StudentReportDTO> getStudentsReport(long groupId);
}
