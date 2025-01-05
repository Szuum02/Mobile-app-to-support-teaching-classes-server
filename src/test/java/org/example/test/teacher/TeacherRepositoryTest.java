package org.example.test.teacher;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.example.dtos.teacher.TeacherDTO;
import org.example.model.User;
import org.example.reopsitory.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class TeacherRepositoryTest {
    @Autowired
    TeacherRepository teacherRepository;

    @Test
    @DatabaseSetup("classpath:teacher/teachers.xml")
    void givenTeacherId_whenFindById_thenReturnTeacherDTO() {
        TeacherDTO teacherDTO = teacherRepository.getTeacherAfterLogin(1L);
        assertThat(teacherDTO).isNotNull();
        assertThat(teacherDTO.getName()).isEqualTo("Teacher");
        assertThat(teacherDTO.getLastname()).isEqualTo("1");

        TeacherDTO nullTeacher = teacherRepository.getTeacherAfterLogin(-1L);
        assertThat(nullTeacher).isNull();
    }
}
