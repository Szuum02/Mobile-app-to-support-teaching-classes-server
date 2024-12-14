package org.example.test.presence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.PresenceController;
import org.example.dtos.presence.LessonPresenceDTO;
import org.example.dtos.presence.PresenceDTO;
import org.example.dtos.presence.StudentPresenceHistoryDTO;
import org.example.model.Lesson;
import org.example.model.Presence;
import org.example.model.PresenceType;
import org.example.model.Student;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.PresenceRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = PresenceController.class)
public class PresenceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PresenceRepository presenceRepository;
    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private StudentRepository studentRepository;

    @Test
    void givenLesson_whenGetPresenceInLesson_thenReturnPresenceDtos() throws Exception {
        List<LessonPresenceDTO> expectedResponse = List.of(
                new LessonPresenceDTO(1L, PresenceType.O),
                new LessonPresenceDTO(2L, PresenceType.U),
                new LessonPresenceDTO(3L, PresenceType.N),
                new LessonPresenceDTO(4L, PresenceType.S)
        );

        when(presenceRepository.getLessonPresence(1L)).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(get("/presence/get")
                        .contentType("application/json")
                        .param("lessonId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> lessonIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(presenceRepository, times(1)).getLessonPresence(lessonIdCaptor.capture());
        assertThat(lessonIdCaptor.getValue()).isEqualTo(1);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenPresenceDetails_whenPutPresence() throws Exception {
        LocalDateTime date = LocalDateTime.now();

        mockStudent();
        mockLesson();

        when(presenceRepository.findPresenceByLessonIdAndStudentId(1L, 1L)).thenReturn(null);

        mockMvc.perform(put("/presence/removeAndAdd")
                        .contentType("application/json")
                        .param("studentId", "1")
                        .param("lessonId", "1")
                        .param("date", date.toString())
                        .param("presence", PresenceType.stringToPresenceType("O").toString()))
                .andExpect(status().isOk());

        ArgumentCaptor<Long> lessonIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(presenceRepository, times(1)).findPresenceByLessonIdAndStudentId(lessonIdCaptor.capture(), studentIdCaptor.capture());
        assertThat(lessonIdCaptor.getValue()).isEqualTo(1);
        assertThat(studentIdCaptor.getValue()).isEqualTo(1);

        ArgumentCaptor<Presence> presenceCaptor = ArgumentCaptor.forClass(Presence.class);
        verify(presenceRepository, times(1)).save(presenceCaptor.capture());
        assertThat(presenceCaptor.getValue().getPresenceType()).isEqualTo(PresenceType.O);
        assertThat(presenceCaptor.getValue().getDate()).isEqualTo(date);
        assertThat(presenceCaptor.getValue().getLesson().getId()).isEqualTo(1);
        assertThat(presenceCaptor.getValue().getStudent().getId()).isEqualTo(1);
    }

    @Test
    void givenPresenceDetails_whenUpdatePresence() throws Exception {
        LocalDateTime date = LocalDateTime.now();
        mockPresence(date);

        mockMvc.perform(put("/presence/removeAndAdd")
                        .contentType("application/json")
                        .param("studentId", "1")
                        .param("lessonId", "1")
                        .param("date", date.toString())
                        .param("presence", PresenceType.stringToPresenceType("U").toString()))
                .andExpect(status().isOk());

        ArgumentCaptor<Long> lessonIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(presenceRepository, times(1)).findPresenceByLessonIdAndStudentId(lessonIdCaptor.capture(), studentIdCaptor.capture());
        assertThat(lessonIdCaptor.getValue()).isEqualTo(1);
        assertThat(studentIdCaptor.getValue()).isEqualTo(1);

        verify(studentRepository, times(0)).findById(studentIdCaptor.capture());

        verify(lessonRepository, times(0)).findById(lessonIdCaptor.capture());

        ArgumentCaptor<Presence> presenceCaptor = ArgumentCaptor.forClass(Presence.class);
        verify(presenceRepository, times(1)).save(presenceCaptor.capture());
        assertThat(presenceCaptor.getValue().getId()).isEqualTo(1);
        assertThat(presenceCaptor.getValue().getPresenceType()).isEqualTo(PresenceType.U);
        assertThat(presenceCaptor.getValue().getDate()).isEqualTo(date);
        assertThat(presenceCaptor.getValue().getLesson().getId()).isEqualTo(1);
        assertThat(presenceCaptor.getValue().getStudent().getId()).isEqualTo(1);
    }

    @Test
    void givenStudentIdAndGroupId_whenGetStudentPresenceHistory_thenReturnStudentPresenceHistoryDTO() throws Exception {
        List<PresenceDTO> presenceDTOS = List.of(
                new PresenceDTO(LocalDateTime.now(), PresenceType.O),
                new PresenceDTO(LocalDateTime.now(), PresenceType.U),
                new PresenceDTO(LocalDateTime.now(), PresenceType.N),
                new PresenceDTO(LocalDateTime.now(), PresenceType.S)
        );
        StudentPresenceHistoryDTO expectedResponse = new StudentPresenceHistoryDTO("name1", "lastname1", 1, presenceDTOS);

        mockStudent();
        when(presenceRepository.getStudentPresence(1, 1)).thenReturn(presenceDTOS);

        MvcResult mvcResult = mockMvc.perform(get("/presence/student/get")
                        .contentType("application/json")
                        .param("studentId", "1")
                        .param("groupId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> groupIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(presenceRepository, times(1)).getStudentPresence(studentIdCaptor.capture(), groupIdCaptor.capture());
        assertThat(studentIdCaptor.getValue()).isEqualTo(1);
        assertThat(groupIdCaptor.getValue()).isEqualTo(1);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    private void mockLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        when(lessonRepository.findById(1L)).thenReturn(lesson);
    }

    private void mockStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("name1");
        student.setLastname("lastname1");
        student.setIndex(1);
        when(studentRepository.findById(1L)).thenReturn(student);
    }

    private void mockPresence(LocalDateTime date) {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        Student student = new Student();
        student.setId(1L);

        Presence presence = new Presence();
        presence.setId(1L);
        presence.setLesson(lesson);
        presence.setStudent(student);
        presence.setDate(date);
        presence.setPresenceType(PresenceType.N);
        when(presenceRepository.findPresenceByLessonIdAndStudentId(1L, 1L)).thenReturn(presence);
    }

    private StudentPresenceHistoryDTO setupHistory() {
        List<PresenceDTO> presenceDTOS = List.of(
                new PresenceDTO(LocalDateTime.now(), PresenceType.O),
                new PresenceDTO(LocalDateTime.now(), PresenceType.U),
                new PresenceDTO(LocalDateTime.now(), PresenceType.N),
                new PresenceDTO(LocalDateTime.now(), PresenceType.S)
        );

        return new StudentPresenceHistoryDTO("name1", "lastname1", 1, presenceDTOS);
    }
}
