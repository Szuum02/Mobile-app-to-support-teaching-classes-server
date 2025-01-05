package org.example.test.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.example.controller.StudentController;
import org.example.dtos.lesson.LessonDTO;
import org.example.dtos.student.ShowInRankingDTO;
import org.example.dtos.student.StudentDTO;
import org.example.model.Group;
import org.example.model.Lesson;
import org.example.model.Student;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private GroupRepository groupRepository;

    @Test
    void givenStudentId_whenStudentLogin_thenReturnStudentDTO() throws Exception {
        List<Date> dates = List.of(
                new Date(), new Date(), new Date(), new Date()
        );
        Map<LocalDate, List<LessonDTO>> lessons = setupLessons(dates);
        StudentDTO expectedResponse = new StudentDTO(1L, "name1", "lastname1", 1, "nick1", true);

        mockLessons(dates);
        when(studentRepository.getStudentAfterLogin(1)).thenReturn(expectedResponse);

        expectedResponse.setLessons(lessons);

        MvcResult mvcResult = mockMvc.perform(post("/student/login")
                        .contentType("application/json")
                        .param("studentId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(studentRepository, times(1)).getStudentAfterLogin(studentIdCaptor.capture());
        assertThat(studentIdCaptor.getValue()).isEqualTo(1L);

        verify(lessonRepository, times(1)).findLessonByStudentId(studentIdCaptor.capture());
        assertThat(studentIdCaptor.getValue()).isEqualTo(1);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenStudentIdAndShowInRanking_whenStudentChangeShowInRanking_thenReturnShowInRankingDTO() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setShowInRanking(true);

        when(studentRepository.findById(1)).thenReturn(student);

        ShowInRankingDTO expectedResponse = new ShowInRankingDTO(1L, false);

        MvcResult mvcResult = mockMvc.perform(post("/student/changeShowInRanking")
                        .contentType("application/json")
                        .param("studentId", "1")
                        .param("showInRanking", "false"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository, times(1)).save(studentCaptor.capture());
        assertThat(studentCaptor.getValue().getId()).isEqualTo(1);
        assertThat(studentCaptor.getValue().isShowInRanking()).isFalse();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    private void mockLessons(List<Date> dates) {
        List<Lesson> lessons = new ArrayList<>();
        for (int i = 1; i <= dates.size(); i++) {
            Group group = new Group();
            group.setId((long) i);

            Lesson lesson = new Lesson();
            lesson.setId((long) i);
            lesson.setClassroom(i + "a");
            lesson.setTopic("topic" + i);
            lesson.setDate(dates.get(i-1));
            lesson.setGroup(group);
            lessons.add(lesson);
        }
        when(lessonRepository.findLessonByStudentId(1)).thenReturn(lessons);
    }

    private Map<LocalDate, List<LessonDTO>> setupLessons(List<Date> dates) {
        List<LessonDTO> lessonDTOS1 = List.of(
                new LessonDTO(1L, dates.get(0), "1a", "topic1", 1L, "code1"),
                new LessonDTO(2L, dates.get(1), "2a", "topic2", 2L, "code2")
        );

        List<LessonDTO> lessonDTOS2 = List.of(
                new LessonDTO(3L, dates.get(2), "3a", "topic3", 3L, "code3"),
                new LessonDTO(4L, dates.get(3), "4a", "topic4", 4L, "code4")
        );

        Map<LocalDate, List<LessonDTO>> map = new HashMap<>();
        map.put(LocalDate.now(), lessonDTOS1);
        map.put(LocalDate.now(), lessonDTOS2);
        return map;
    }
}
