package org.example.reopsitory;

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
            "(select sum(a2.points) from Activity a2 where a2.student.id = s.id and a2.lesson.group.id = ?1 and a2.date = CURRENT_DATE)) from Activity a " +
            "inner join a.student s where a.lesson.group.id = ?1 group by s.id order by ?2")
    List<ActivityRankingDTO> getGroupRanking(Long groupId, String order);
}
