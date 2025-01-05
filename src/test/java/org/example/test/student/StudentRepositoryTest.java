package org.example.test.student;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.example.dtos.student.StudentDTO;
import org.example.model.Lesson;
import org.example.model.Student;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class StudentRepositoryTest {
    @Autowired
    StudentRepository studentRepository;

    @Test
    @DatabaseSetup("classpath:student/students.xml")
    void givenStudentId_whenFindById_thenReturnStudent() {
        Student student = studentRepository.findById(1L);
        assertThat(student).isNotNull();
        assertThat(student.getIndex()).isEqualTo(1);
        assertThat(student.getName()).isEqualTo("name1");
        assertThat(student.getLastname()).isEqualTo("last1");
        assertThat(student.isShowInRanking()).isTrue();
        assertThat(student.getNick()).isEqualTo("s1");

        Student nullStudent = studentRepository.findById(-1L);
        assertThat(nullStudent).isNull();
    }

    @Test
    @DatabaseSetup("classpath:student/students.xml")
    void givenStudentIndex_whenFindByIndex_thenReturnStudent() {
        Student student = studentRepository.findByIndex(1);
        assertThat(student).isNotNull();
        assertThat(student.getId()).isEqualTo(1);
        assertThat(student.getName()).isEqualTo("name1");
        assertThat(student.getLastname()).isEqualTo("last1");
        assertThat(student.isShowInRanking()).isTrue();
        assertThat(student.getNick()).isEqualTo("s1");

        Student nullStudent = studentRepository.findByIndex(0);
        assertThat(nullStudent).isNull();
    }

    @Test
    @DatabaseSetup("classpath:student/students.xml")
    void givenStudentNick_whenFindByNick_thenReturnStudent() {
        Student student = studentRepository.findByNick("s1");
        assertThat(student).isNotNull();
        assertThat(student.getId()).isEqualTo(1);
        assertThat(student.getIndex()).isEqualTo(1);
        assertThat(student.getName()).isEqualTo("name1");
        assertThat(student.getLastname()).isEqualTo("last1");
        assertThat(student.isShowInRanking()).isTrue();

        Student nullStudent = studentRepository.findByNick("wrong");
        assertThat(nullStudent).isNull();
    }

    @Test
    @DatabaseSetup("classpath:student/students.xml")
    void givenStudentMail_whenStudentLogin_thenReturnStudentDTO() {
        StudentDTO studentDTO = studentRepository.getStudentAfterLogin(1L);
        assertThat(studentDTO).isNotNull();
        assertThat(studentDTO.getIndex()).isEqualTo(1);
        assertThat(studentDTO.getName()).isEqualTo("name1");
        assertThat(studentDTO.getLastname()).isEqualTo("last1");
        assertThat(studentDTO.getNick()).isEqualTo("s1");
        assertThat(studentDTO.isShowInRanking()).isTrue();

        StudentDTO nullStudent = studentRepository.getStudentAfterLogin(-1L);
        assertThat(nullStudent).isNull();
    }
}
