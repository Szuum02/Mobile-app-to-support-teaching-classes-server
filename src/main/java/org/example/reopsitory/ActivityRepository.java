package org.example.reopsitory;

import org.example.dtos.ActivityDTO;
import org.example.dtos.StudentActivityDTO;
import org.example.model.Activity;
import org.example.model.Lesson;
import org.example.model.Student;
import org.example.dtos.ActivityRankingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("select sum(a.points) from Activity a where a.student = ?1 and a.lesson = ?2")
    Integer getStudentsPointsInLesson(Student student, Lesson lesson);

    @Query("select new org.example.dtos.ActivityRankingDTO(s.nick, sum(a.points)) from Activity a inner join a.student s " +
            "where a.lesson.group.subject = ?1 group by s.id order by sum(a.points) desc")
    List<ActivityRankingDTO> getRanking(String subject);

    @Query("select new org.example.dtos.ActivityRankingDTO(s.nick, sum(a.points)," +
            "(select sum(a2.points) from Activity a2 where a2.student.id = s.id and a2.lesson.group.id = ?1 and cast(a2.date as date) = CURRENT_DATE)) from Activity a " +
            "inner join a.student s where a.lesson.group.id = ?1 group by s.id order by sum(a.points) desc")
    List<ActivityRankingDTO> getGroupRanking(Long groupId);

    @Query("select new org.example.dtos.ActivityDTO(a.date, a.points) from Activity a where a.student.id = ?1 and a.lesson.group.id = ?2 order by a.date")
    List<ActivityDTO> getStudentActivityHistory(long studentId, long groupId);

    @Query("select sum(a.points) from Activity a group by a.lesson having a.student.id = ?1 and a.lesson.group.id = ?2")
    List<Integer> getStudentActivities(long studentId, long groupId);
}
