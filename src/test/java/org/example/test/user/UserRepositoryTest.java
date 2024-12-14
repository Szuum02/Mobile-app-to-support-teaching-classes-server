package org.example.test.user;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.example.dtos.user.UserDTO;
import org.example.model.User;
import org.example.reopsitory.UserRepository;
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
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DatabaseSetup("classpath:user/users.xml")
    void givenUserMail_whenFindByMail_thenReturnUser() {
        User teacherUser = userRepository.findUserByMail("teacher@example.com");
        assertThat(teacherUser.getId()).isEqualTo(1);
        assertThat(teacherUser.getIsStudent()).isFalse();
        assertThat(teacherUser.getPassword()).isEqualTo("p1");

        User studentUser = userRepository.findUserByMail("student2@example.com");
        assertThat(studentUser.getId()).isEqualTo(2);
        assertThat(studentUser.getIsStudent()).isTrue();
        assertThat(studentUser.getPassword()).isEqualTo("p2");

        User wrongUser = userRepository.findUserByMail("wrongEmail@example.com");
        assertThat(wrongUser).isNull();
    }

    @Test
    @DatabaseSetup("classpath:user/users.xml")
    void givenUserMail_whenFindByMail_thenReturnUserDTO() {
        UserDTO teacherUserDTO = userRepository.findUserByMailAndPassword("teacher@example.com", "p1");
        assertThat(teacherUserDTO.getId()).isEqualTo(1);
        assertThat(teacherUserDTO.isStudent()).isFalse();

        UserDTO studentUser = userRepository.findUserByMailAndPassword("student2@example.com", "p2");
        assertThat(studentUser.getId()).isEqualTo(2);
        assertThat(studentUser.isStudent()).isTrue();

        UserDTO wrongMail = userRepository.findUserByMailAndPassword("wrongEmail@example.com", "");
        assertThat(wrongMail).isNull();

        UserDTO wrongPassword = userRepository.findUserByMailAndPassword("student2@example.com", "wrong");
        assertThat(wrongPassword).isNull();
    }
}
