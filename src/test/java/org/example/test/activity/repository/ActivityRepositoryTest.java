package org.example.test.activity.repository;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.example.dtos.ActivityDTO;
import org.example.dtos.ActivityPlotDTO;
import org.example.dtos.ActivityRankingDTO;
import org.example.reopsitory.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class ActivityRepositoryTest {
    @Autowired
    ActivityRepository activityRepository;

    @Test
    @DatabaseSetup("classpath:activity/studentHistory.xml")
    void givenStudentAndLesson_whenShowTodayPoints_thenReturnPoints() {
        int points = activityRepository.getStudentsPointsInLesson(2L, 1L);
        assertThat(points).isEqualTo(2);

        points = activityRepository.getStudentsPointsInLesson(2L, 2L);
        assertThat(points).isEqualTo(3);
    }

    @Test
    @DatabaseSetup("classpath:activity/rankingSetup.xml")
    void givenSubject_whenShowPoints_thenReturnTotalRanking() {
        List<ActivityRankingDTO> ranking = activityRepository.getRanking("subject1");
        assertThat(ranking.size()).isEqualTo(4);
        assertThat(ranking.get(0).getNick()).isEqualTo("s3");
        assertThat(ranking.get(0).getTotalPoints()).isEqualTo(7);
        assertThat(ranking.get(3).getNick()).isEqualTo("s4");
        assertThat(ranking.get(3).getTotalPoints()).isEqualTo(-3);
    }

    @Test
    @DatabaseSetup("classpath:activity/rankingSetup.xml")
    void givenGroup_whenShowPoints_thenReturnGroupRanking() {
        List<ActivityRankingDTO> ranking = activityRepository.getGroupRanking(1L);
        assertThat(ranking.size()).isEqualTo(2);
        assertThat(ranking.get(0).getTotalPoints()).isEqualTo(7);
        assertThat(ranking.get(0).getTodayPoints()).isEqualTo(4);
        assertThat(ranking.get(1).getTotalPoints()).isEqualTo(2);
        assertThat(ranking.get(1).getTodayPoints()).isNull();
    }

    @Test
    @DatabaseSetup("classpath:activity/studentHistory.xml")
    void givenStudentAndGroup_whenShowPoints_thenReturnActivityHistory() {
        List<ActivityDTO> activityHistory = activityRepository.getStudentActivityHistory(2L, 1L);
        assertThat(activityHistory.size()).isEqualTo(4);
        assertThat(activityHistory.get(0).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 6, 9, 10, 0));
        assertThat(activityHistory.get(1).getPoints()).isEqualTo(4);
    }

    @Test
    @DatabaseSetup("classpath:activity/studentHistory.xml")
    void givenStudentAndGroup_whenShowActivityPlot_thenReturnPointsHistory() {
        List<ActivityPlotDTO> activities = activityRepository.getStudentActivities(2L, 1L);
        assertThat(activities.size()).isEqualTo(3);
        assertThat(activities.get(0).getPoints()).isEqualTo(2);
        assertThat(activities.get(0).getTopic()).isEqualTo("test1");
        assertThat(activities.get(1).getPoints()).isEqualTo(3);
    }
}
