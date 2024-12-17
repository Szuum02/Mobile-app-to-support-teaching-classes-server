package org.example.test.presence;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.EntityManager;
import org.example.dtos.presence.LessonPresenceDTO;
import org.example.dtos.presence.PresenceDTO;
import org.example.model.Lesson;
import org.example.model.PresenceType;
import org.example.model.Student;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.PresenceRepository;
import org.example.reopsitory.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class PresenceRepositoryTest {
    @Autowired
    PresenceRepository presenceRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DatabaseSetup("classpath:presence/lessonPresence.xml")
    void givenLessonId_whenFindStudentsPresence_thenReturnPresences() {
        List<LessonPresenceDTO> presenceDTOS = presenceRepository.getLessonPresence(1L);
        assertThat(presenceDTOS).isNotNull();
        assertThat(presenceDTOS.size()).isEqualTo(4);
        assertThat(presenceDTOS.get(0).getStudentId()).isEqualTo(2L);
        assertThat(presenceDTOS.get(0).getPresenceType()).isEqualTo(PresenceType.O);
        assertThat(presenceDTOS.get(1).getStudentId()).isEqualTo(3L);
        assertThat(presenceDTOS.get(1).getPresenceType()).isEqualTo(PresenceType.U);
        assertThat(presenceDTOS.get(2).getStudentId()).isEqualTo(4L);
        assertThat(presenceDTOS.get(2).getPresenceType()).isEqualTo(PresenceType.S);
        assertThat(presenceDTOS.get(3).getStudentId()).isEqualTo(5L);
        assertThat(presenceDTOS.get(3).getPresenceType()).isEqualTo(PresenceType.N);

        List<LessonPresenceDTO> emptyPresences = presenceRepository.getLessonPresence(2L);
        assertThat(emptyPresences).isNotNull();
        assertThat(emptyPresences).isEmpty();
    }

    @Test
    @DatabaseSetup("classpath:presence/lessonPresence.xml")
    void givenLessonAndStudent_whenRemovingPresence() {
        List<LessonPresenceDTO> presenceDTOS = presenceRepository.getLessonPresence(1L);
        assertThat(presenceDTOS).isNotNull();
        assertThat(presenceDTOS.size()).isEqualTo(4);

        Student student = studentRepository.findByIndex(2);
        Lesson lesson = lessonRepository.findById(1L);
        presenceRepository.deletePresenceByLessonAndStudent(lesson, student);
        entityManager.flush();

        presenceDTOS = presenceRepository.getLessonPresence(1L);
        assertThat(presenceDTOS).isNotNull();
        assertThat(presenceDTOS.size()).isEqualTo(3);
        assertThat(presenceDTOS.get(0).getStudentId()).isEqualTo(3L);
        assertThat(presenceDTOS.get(0).getPresenceType()).isEqualTo(PresenceType.U);
    }

    @Test
    @DatabaseSetup("classpath:presence/studentPresence.xml")
    void givenStudentIdAndGroupId_whenFindStudentPresences_thenReturnPresenceDTOS() {
        List<PresenceDTO> presenceDTOS = presenceRepository.getStudentPresence(2L, 1L);
        assertThat(presenceDTOS).isNotNull();
        assertThat(presenceDTOS.size()).isEqualTo(4);
        assertThat(presenceDTOS.get(3).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 6, 9, 0,0, 0));
        assertThat(presenceDTOS.get(3).getPresenceType()).isEqualTo(PresenceType.O);
        assertThat(presenceDTOS.get(2).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 7, 9, 0,0, 0));
        assertThat(presenceDTOS.get(2).getPresenceType()).isEqualTo(PresenceType.U);
        assertThat(presenceDTOS.get(1).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 8, 9, 0,0, 0));
        assertThat(presenceDTOS.get(1).getPresenceType()).isEqualTo(PresenceType.S);
        assertThat(presenceDTOS.get(0).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 9, 9, 0,0, 0));
        assertThat(presenceDTOS.get(0).getPresenceType()).isEqualTo(PresenceType.N);
    }

    @Test
    @DatabaseSetup("classpath:presence/studentPresence.xml")
    void givenStudentIndexAndGroupId_whenFindStudentPresences_thenReturnPresenceDTOS() {
        List<PresenceDTO> presenceDTOS = presenceRepository.getStudentPresenceByIndex(2, 1L);
        assertThat(presenceDTOS).isNotNull();
        assertThat(presenceDTOS.size()).isEqualTo(4);
        assertThat(presenceDTOS.get(3).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 6, 9, 0,0, 0));
        assertThat(presenceDTOS.get(3).getPresenceType()).isEqualTo(PresenceType.O);
        assertThat(presenceDTOS.get(2).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 7, 9, 0,0, 0));
        assertThat(presenceDTOS.get(2).getPresenceType()).isEqualTo(PresenceType.U);
        assertThat(presenceDTOS.get(1).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 8, 9, 0,0, 0));
        assertThat(presenceDTOS.get(1).getPresenceType()).isEqualTo(PresenceType.S);
        assertThat(presenceDTOS.get(0).getDate()).isEqualTo(LocalDateTime.of(2024, 6, 9, 9, 0,0, 0));
        assertThat(presenceDTOS.get(0).getPresenceType()).isEqualTo(PresenceType.N);
    }
}
