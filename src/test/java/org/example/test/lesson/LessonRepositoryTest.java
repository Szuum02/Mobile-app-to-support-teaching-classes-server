package org.example.test.lesson;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import oracle.sql.DATE;

import java.sql.Timestamp;
import org.example.model.Group;
import org.example.model.Lesson;
import org.example.reopsitory.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class LessonRepositoryTest {
    @Autowired
    LessonRepository lessonRepository;

    @Test
    @DatabaseSetup("classpath:lesson/lessons.xml")
    void givenLessonId_whenFindById_thenReturnLesson() {
        Lesson lesson = lessonRepository.findById(1L);
        assertThat(lesson).isNotNull();
        assertThat(lesson.getTopic()).isEqualTo("test1");
        assertThat(lesson.getDate()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2024, 6, 6, 9, 0,0, 0)));
        assertThat(lesson.getClassroom()).isEqualTo("1");
        assertThat(lesson.getActivities().size()).isEqualTo(2);
    }

    @Test
    @DatabaseSetup("classpath:lesson/lessons.xml")
    void givenTeacherId_whenFindByTeacherId_thenReturnLessons() {
        List<Lesson> lessons = lessonRepository.findLessonsByTeacherId(1L);
        assertThat(lessons).isNotNull();
        assertThat(lessons.size()).isEqualTo(4);
        assertThat(lessons.get(0).getTopic()).isEqualTo("test1");
        assertThat(lessons.get(0).getDate()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2024, 6, 6, 9, 0,0, 0)));
        assertThat(lessons.get(0).getClassroom()).isEqualTo("1");
        assertThat(lessons.get(0).getActivities().size()).isEqualTo(2);
    }

    @Test
    @DatabaseSetup("classpath:lesson/lessons.xml")
    void givenStudentId_whenFindByStudentId_thenReturnLessons() {
        List<Lesson> lessons = lessonRepository.findLessonByStudentId(2L);
        assertThat(lessons).isNotNull();
        assertThat(lessons.size()).isEqualTo(4);
        assertThat(lessons.get(0).getTopic()).isEqualTo("test1");
        assertThat(lessons.get(0).getDate()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2024, 6, 6, 9, 0,0, 0)));
        assertThat(lessons.get(0).getClassroom()).isEqualTo("1");
        assertThat(lessons.get(0).getActivities().size()).isEqualTo(2);
    }
}
