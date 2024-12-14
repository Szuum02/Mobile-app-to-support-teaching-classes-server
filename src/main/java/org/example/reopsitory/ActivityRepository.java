package org.example.reopsitory;

import org.example.dtos.Activity.LessonPointsDTO;
import org.example.dtos.ActivityDTO;
import org.example.dtos.ActivityPlotDTO;
import org.example.model.Activity;
import org.example.dtos.ActivityRankingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("select new org.example.dtos.Activity.LessonPointsDTO(s.id, sum(a.points), " +
            "(select sum(a2.points) from Activity a2 where a2.student.id = s.id and a2.lesson.group.id = ?1 and cast(a2.date as date) = CURRENT_DATE)) " +
            "from Activity a inner join a.student s where a.lesson.group.id = ?1 group by s.id order by s.lastname")
    List<LessonPointsDTO> getLessonPoints(Long groupId);

    @Query("select sum(a.points) from Activity a where a.student.id = ?1 and a.lesson.id = ?2 and cast(a.date as date) = CURRENT_DATE ")
    Integer getStudentsPointsInLesson(Long studentId, Long lessonId);

    @Query("select new org.example.dtos.ActivityRankingDTO(s.id, s.nick, s.showInRanking, sum(a.points)) from Activity a inner join a.student s " +
            "where a.lesson.group.subject = ?1 group by s.id order by sum(a.points) desc")
    List<ActivityRankingDTO> getRanking(String subject);

    @Query("select new org.example.dtos.ActivityRankingDTO(s.id, s.nick, s.showInRanking, sum(a.points)," +
            "(select sum(a2.points) from Activity a2 where a2.student.id = s.id and a2.lesson.group.id = ?1 and cast(a2.date as date) = CURRENT_DATE)) from Activity a " +
            "inner join a.student s where a.lesson.group.id = ?1 group by s.id order by sum(a.points) desc")
    List<ActivityRankingDTO> getGroupRanking(Long groupId);

    @Query("select new org.example.dtos.ActivityDTO(a.date, a.points) from Activity a where a.student.id = ?1 and a.lesson.group.id = ?2 order by a.date desc")
    List<ActivityDTO> getStudentActivityHistory(long studentId, long groupId);

    @Query("select new org.example.dtos.ActivityPlotDTO(a.lesson.topic, sum(a.points)) from Activity a where a.student.id = ?1 and a.lesson.group.id = ?2 group by a.lesson")
    List<ActivityPlotDTO> getStudentActivities(long studentId, long groupId);
}
