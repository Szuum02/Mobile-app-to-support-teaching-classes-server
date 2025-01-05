package org.example.test.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.example.controller.UserController;
import org.example.dtos.student.StudentDTO;
import org.example.dtos.teacher.TeacherDTO;
import org.example.dtos.user.UserDTO;
import org.example.model.Student;
import org.example.model.Teacher;
import org.example.model.User;
import org.example.reopsitory.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    void givenMailAndPassword_whenUserLogin_thenReturnUserDTO() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setIsStudent(true);
        user.setMail("mail@example.com");
        user.setPassword("password");
        when(userRepository.findUserByMail("mail@example.com")).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(post("/user/login")
                .contentType("application/json")
                .param("mail", "mail@example.com")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("mail@example.com");

        UserDTO expectedResponse = new UserDTO(1L, true);
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenMailAndPassword_whenWrongMailAndPassword_thenReturnErrorUserDTO() throws Exception {
        UserDTO expectedResponse = new UserDTO(-1L, false);
        when(userRepository.findUserByMailAndPassword("wrong@example.com", "password")).thenReturn(expectedResponse);
        when(userRepository.findUserByMailAndPassword("mail@example.com", "wrong")).thenReturn(expectedResponse);

        // wrong mail
        MvcResult mvcResult = mockMvc.perform(post("/user/login")
                        .contentType("application/json")
                        .param("mail", "wrong@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("wrong@example.com");

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));

        // wrong password
        mvcResult = mockMvc.perform(post("/user/login")
                        .contentType("application/json")
                        .param("mail", "mail@example.com")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andReturn();

        mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(2)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("mail@example.com");

        actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenMailPasswordAndIsStudent_whenAddUser_thenReturnUserId() throws Exception {
        Long expectedResponse = 1L;

        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        MvcResult mvcResult = mockMvc.perform(post("/user/add")
                        .contentType("application/json")
                        .param("mail", "mail@example.com")
                        .param("password", "password")
                        .param("isStudent", "true"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getMail()).isEqualTo("mail@example.com");
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("password");
        assertThat(userCaptor.getValue().getIsStudent()).isTrue();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenMail_whenCheckUniqueMail_thenReturnIsUnique() throws Exception {
        when(userRepository.findUserByMail("unique@example.com")).thenReturn(null);
        when(userRepository.findUserByMail("notUnique@example.com")).thenReturn(new User());

        // unique mail
        MvcResult mvcResult = mockMvc.perform(get("/user/checkUniqueMail")
                        .contentType("application/json")
                        .param("mail", "unique@example.com"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("unique@example.com");

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(true));

        // not unique mail
        mvcResult = mockMvc.perform(get("/user/checkUniqueMail")
                        .contentType("application/json")
                        .param("mail", "notUnique@example.com"))
                .andExpect(status().isOk())
                .andReturn();

        mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(2)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("notUnique@example.com");

        actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(false));
    }

    @Test
    void givenMailIndexAndNick_whenCheckUniqueValues_thenReturnOk() throws Exception {
        when(userRepository.findUserByMail("unique@example.com")).thenReturn(null);
        when(studentRepository.findByIndex(1)).thenReturn(null);
        when(studentRepository.findByNick("unique")).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/user/checkUniqueValues")
                        .contentType("application/json")
                        .param("mail", "unique@example.com")
                        .param("index", "1")
                        .param("nick", "unique"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("unique@example.com");

        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(studentRepository, times(1)).findByIndex(indexCaptor.capture());
        assertThat(indexCaptor.getValue()).isEqualTo(1);

        ArgumentCaptor<String> nickCaptor = ArgumentCaptor.forClass(String.class);
        verify(studentRepository, times(1)).findByNick(nickCaptor.capture());
        assertThat(nickCaptor.getValue()).isEqualTo("unique");

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace("ok");
    }

    @Test
    void givenMailIndexAndNick_whenCheckUniqueValues_thenReturnNotUniqueMail() throws Exception {
        when(userRepository.findUserByMail("notUnique@example.com")).thenReturn(new User());
        when(studentRepository.findByIndex(1)).thenReturn(null);
        when(studentRepository.findByNick("unique")).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/user/checkUniqueValues")
                        .contentType("application/json")
                        .param("mail", "notUnique@example.com")
                        .param("index", "1")
                        .param("nick", "unique"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("notUnique@example.com");

        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(studentRepository,times(0)).findByIndex(indexCaptor.capture());

        ArgumentCaptor<String> nickCaptor = ArgumentCaptor.forClass(String.class);
        verify(studentRepository, times(0)).findByNick(nickCaptor.capture());

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace("mail");
    }

    @Test
    void givenMailIndexAndNick_whenCheckUniqueValues_thenReturnNotUniqueIndex() throws Exception {
        when(userRepository.findUserByMail("unique@example.com")).thenReturn(null);
        when(studentRepository.findByIndex(1)).thenReturn(new Student());
        when(studentRepository.findByNick("unique")).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/user/checkUniqueValues")
                        .contentType("application/json")
                        .param("mail", "unique@example.com")
                        .param("index", "1")
                        .param("nick", "unique"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("unique@example.com");

        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(studentRepository,times(1)).findByIndex(indexCaptor.capture());
        assertThat(indexCaptor.getValue()).isEqualTo(1);

        ArgumentCaptor<String> nickCaptor = ArgumentCaptor.forClass(String.class);
        verify(studentRepository, times(0)).findByNick(nickCaptor.capture());

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace("index");
    }

    @Test
    void givenMailIndexAndNick_whenCheckUniqueValues_thenReturnNick() throws Exception {
        when(userRepository.findUserByMail("unique@example.com")).thenReturn(null);
        when(studentRepository.findByIndex(1)).thenReturn(null);
        when(studentRepository.findByNick("notUnique")).thenReturn(new Student());

        MvcResult mvcResult = mockMvc.perform(get("/user/checkUniqueValues")
                        .contentType("application/json")
                        .param("mail", "unique@example.com")
                        .param("index", "1")
                        .param("nick", "notUnique"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> mailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1)).findUserByMail(mailCaptor.capture());
        assertThat(mailCaptor.getValue()).isEqualTo("unique@example.com");

        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(studentRepository, times(1)).findByIndex(indexCaptor.capture());
        assertThat(indexCaptor.getValue()).isEqualTo(1);

        ArgumentCaptor<String> nickCaptor = ArgumentCaptor.forClass(String.class);
        verify(studentRepository, times(1)).findByNick(nickCaptor.capture());
        assertThat(nickCaptor.getValue()).isEqualTo("notUnique");

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace("nick");
    }

    @Test
    void givenTeacherDetails_whenAddTeacher_thenReturnTeacherDto() throws Exception {
        TeacherDTO expectedResponse = new TeacherDTO(1L, "name1", "lastname1");
        expectedResponse.setLessons(new HashMap<>());

        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(teacherRepository.save(Mockito.any(Teacher.class))).thenAnswer(invocation -> {
            Teacher teacher = invocation.getArgument(0);
            teacher.setId(1L);
            return teacher;
        });

        MvcResult mvcResult = mockMvc.perform(post("/user/addTeacher")
                        .contentType("application/json")
                        .param("mail", "mail@example.com")
                        .param("password", "password")
                        .param("name", "name1")
                        .param("lastName", "lastname1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getMail()).isEqualTo("mail@example.com");
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("password");
        assertThat(userCaptor.getValue().getIsStudent()).isFalse();
        assertThat(userCaptor.getValue().getTeacher().getId()).isEqualTo(1);

        ArgumentCaptor<Teacher> teacherCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository, times(1)).save(teacherCaptor.capture());
        assertThat(teacherCaptor.getValue().getName()).isEqualTo("name1");
        assertThat(teacherCaptor.getValue().getLastname()).isEqualTo("lastname1");
        assertThat(teacherCaptor.getValue().getUser().getId()).isEqualTo(1);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenStudentDetails_whenAddTeacher_thenReturnStudentDto() throws Exception {
        StudentDTO expectedResponse = new StudentDTO(1L, "name1", "lastname1", 1, "nick1", true);
        expectedResponse.setLessons(new HashMap<>());

        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(studentRepository.save(Mockito.any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(1L);
            return student;
        });

        MvcResult mvcResult = mockMvc.perform(post("/user/addStudent")
                        .contentType("application/json")
                        .param("mail", "mail@example.com")
                        .param("password", "password")
                        .param("name", "name1")
                        .param("lastName", "lastname1")
                        .param("index", "1")
                        .param("nick", "nick1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getMail()).isEqualTo("mail@example.com");
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("password");
        assertThat(userCaptor.getValue().getIsStudent()).isTrue();
        assertThat(userCaptor.getValue().getStudent().getId()).isEqualTo(1);

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository, times(1)).save(studentCaptor.capture());
        assertThat(studentCaptor.getValue().getName()).isEqualTo("name1");
        assertThat(studentCaptor.getValue().getLastname()).isEqualTo("lastname1");
        assertThat(studentCaptor.getValue().getIndex()).isEqualTo(1);
        assertThat(studentCaptor.getValue().getNick()).isEqualTo("nick1");
        assertThat(studentCaptor.getValue().isShowInRanking()).isTrue();
        assertThat(studentCaptor.getValue().getUser().getId()).isEqualTo(1);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }
}
