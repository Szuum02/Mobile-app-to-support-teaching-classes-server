package org.example.test.activity.repository;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.transaction.Transactional;
import org.example.ServerApp;
import org.example.dtos.ActivityRankingDTO;
import org.example.model.User;
import org.example.reopsitory.ActivityRepository;
import org.example.reopsitory.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@TestExecutionListeners({
//        DependencyInjectionTestExecutionListener.class,
//        TransactionDbUnitTestExecutionListener.class
//})
//@SpringBootTest(classes = ServerApp.class)
//@Transactional
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class ActivityRepositoryTest {
    @Autowired
    ActivityRepository activityRepository;

//    @BeforeEach
//    public void setUpData() {
//        for (int i = 0; i < 5; i++) {
//            Student s = new Student();
//            entityManager.persist(s);
//        }
//    }

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
}
