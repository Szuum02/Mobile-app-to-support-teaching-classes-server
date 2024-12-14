package org.example.test.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.ActivityController;
import org.example.dtos.Activity.LessonPointsDTO;
import org.example.dtos.Activity.StudentHistoryDTO;
import org.example.dtos.ActivityDTO;
import org.example.dtos.ActivityRankingDTO;
import org.example.model.Activity;
import org.example.model.Group;
import org.example.model.Lesson;
import org.example.model.Student;
import org.example.reopsitory.ActivityRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ActivityController.class)
public class ActivityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActivityRepository activityRepository;
    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private GroupRepository groupRepository;

    @Test
    void givenGroupId_whenShowLessonActivity_thenReturnLessonPointsDTOList() throws Exception {
        List<LessonPointsDTO> expectedResponse = List.of(
                new LessonPointsDTO(1L, 1L, 0L),
                new LessonPointsDTO(2L, 0L, 1L),
                new LessonPointsDTO(3L, 2L, 1L)
        );
        when(activityRepository.getLessonPoints(1L)).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(get("/activity/lessonPoints")
                .contentType("application/json")
                .param("groupId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> groupCaptor = ArgumentCaptor.forClass(Long.class);
        verify(activityRepository, times(1)).getLessonPoints(groupCaptor.capture());
        assertThat(groupCaptor.getValue()).isEqualTo(1L);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenActivityDetails_whenAddingActivity_thenReturnTodayPoints() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("lessonId", "1");
        params.add("studentId", "1");
        params.add("date", time.toString());
        params.add("points", "1");

        mockLesson();
        mockStudent();

        when(activityRepository.getStudentsPointsInLesson(1L, 1L)).thenReturn(1);

        MvcResult mvcResult = mockMvc.perform(post("/activity/add")
                .contentType("application/json")
                .params(params))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
        verify(activityRepository, times(1)).save(activityCaptor.capture());
        assertThat(activityCaptor.getValue().getDate()).isEqualTo(time);
        assertThat(activityCaptor.getValue().getLesson().getId()).isEqualTo(1);
        assertThat(activityCaptor.getValue().getStudent().getId()).isEqualTo(1);

        Integer expectedResponse = 1;
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenGroupId_whenShowRanking_thenReturnActivityRankingDTOList() throws Exception {
        List<ActivityRankingDTO> expectedResponse = List.of(
                new ActivityRankingDTO(1L, "nick1", true, 3L),
                new ActivityRankingDTO(2L, "nick2", true, 2L),
                new ActivityRankingDTO(3L, "nick3", true, -1L)
        );

        mockGroup();
        when(activityRepository.getRanking("subject1")).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(get("/activity/ranking")
                        .contentType("application/json")
                        .param("groupId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        verify(activityRepository, times(1)).getRanking(subjectCaptor.capture());
        assertThat(subjectCaptor.getValue()).isEqualTo("subject1");

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenGroupId_whenShowGroupRanking_thenReturnActivityRankingDTOList() throws Exception {
        List<ActivityRankingDTO> expectedResponse = List.of(
                new ActivityRankingDTO(1L, "nick1", true, 3L, 2L),
                new ActivityRankingDTO(2L, "nick2", true, 2L, 0L),
                new ActivityRankingDTO(3L, "nick3", true, -1L, -1L)
        );

        when(activityRepository.getGroupRanking(1L)).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(get("/activity/groupRanking")
                        .contentType("application/json")
                        .param("groupId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> groupIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(activityRepository, times(1)).getGroupRanking(groupIdCaptor.capture());
        assertThat(groupIdCaptor.getValue()).isEqualTo(1L);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void givenStudentIdAndGroupId_whenShowStudentActivityHistory_thenReturnStudentHistoryDTO() throws Exception {
        List<ActivityDTO> studentActivities = mockStudentActivities();
        Student student = mockStudent();
        StudentHistoryDTO expectedResponse = new StudentHistoryDTO(student.getName(), student.getLastname(), student.getIndex(), studentActivities);

        MvcResult mvcResult = mockMvc.perform(get("/activity/studentHistory")
                        .contentType("application/json")
                        .param("studentId", "1")
                        .param("groupId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> groupIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(activityRepository, times(1)).getStudentActivityHistory(groupIdCaptor.capture(), studentIdCaptor.capture());
        assertThat(groupIdCaptor.getValue()).isEqualTo(1L);
        assertThat(studentIdCaptor.getValue()).isEqualTo(1L);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    private void mockLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        when(lessonRepository.findById(1)).thenReturn(lesson);
    }

    private Student mockStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("name1");
        student.setLastname("lastname1");
        student.setIndex(1);
        when(studentRepository.findById(1)).thenReturn(student);
        return student;
    }

    private void mockGroup() {
        Group group = new Group();
        group.setId(1L);
        group.setSubject("subject1");
        when(groupRepository.findById(1)).thenReturn(group);
    }

    private List<ActivityDTO> mockStudentActivities() {
        List<ActivityDTO> studentActivities = List.of(
                new ActivityDTO(LocalDateTime.now(), 1),
                new ActivityDTO(LocalDateTime.now(), -1)
        );
        when(activityRepository.getStudentActivityHistory(1L, 1L)).thenReturn(studentActivities);
        return studentActivities;
    }
}
