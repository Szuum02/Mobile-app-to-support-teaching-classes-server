package org.example.test.activity.repository;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.example.dtos.ActivityRankingDTO;
import org.example.reopsitory.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

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
    @DatabaseSetup("classpath:setup.xml")
    void givenSubject_whenShowPoints_thenReturnTotalRanking() {
        List<ActivityRankingDTO> ranking = activityRepository.getRanking("subject1");
        assertThat(ranking.size()).isEqualTo(4);
        assertThat(ranking.get(0).getNick()).isEqualTo("s3");
        assertThat(ranking.get(0).getTotalPoints()).isEqualTo(7);
        assertThat(ranking.get(3).getNick()).isEqualTo("s4");
        assertThat(ranking.get(3).getTotalPoints()).isEqualTo(-3);
    }

    @Test
    @DatabaseSetup("classpath:setup.xml")
    void givenGroup_whenShowPoints_thenReturnGroupRanking() {
        List<ActivityRankingDTO> ranking = activityRepository.getGroupRanking(1L);
        assertThat(ranking.size()).isEqualTo(2);
        assertThat(ranking.get(0).getTotalPoints()).isEqualTo(7);
        assertThat(ranking.get(0).getTodayPoints()).isEqualTo(4);
        assertThat(ranking.get(1).getTotalPoints()).isEqualTo(2);
        assertThat(ranking.get(1).getTodayPoints()).isNull();
    }
}
