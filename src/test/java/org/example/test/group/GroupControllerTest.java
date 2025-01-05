package org.example.test.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.GroupController;
import org.example.dtos.Activity.LessonPointsDTO;
import org.example.dtos.groups.ReportDTO;
import org.example.dtos.groups.StudentReportDTO;
import org.example.dtos.presence.PresenceDTO;
import org.example.model.PresenceType;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.PresenceRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = GroupController.class)
public class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GroupRepository groupRepository;
    @MockBean
    private PresenceRepository presenceRepository;

    @Test
    void givenGroupId_whenGeneratingReport_thenReturnReportDTO() throws Exception {
        ReportDTO expectedResponse = new ReportDTO(1L, "subject1");
        when(groupRepository.generateReport(1)).thenReturn(expectedResponse);

        List<StudentReportDTO> studentReportDTOS = mockStudentReports();
        expectedResponse.setStudentReports(studentReportDTOS);

        MvcResult mvcResult = mockMvc.perform(get("/group/generateReport")
                        .contentType("application/json")
                        .param("groupId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> groupIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(groupRepository, times(1)).generateReport(groupIdCaptor.capture());
        assertThat(groupIdCaptor.getValue()).isEqualTo(1L);

        groupIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(groupRepository, times(1)).getStudentsReport(groupIdCaptor.capture());
        assertThat(groupIdCaptor.getValue()).isEqualTo(1L);

        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        groupIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(presenceRepository, times(2)).getStudentPresenceByIndex(indexCaptor.capture(), groupIdCaptor.capture());
        assertThat(indexCaptor.getValue()).isEqualTo(2);
        assertThat(groupIdCaptor.getValue()).isEqualTo(1L);

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
    }

    private List<StudentReportDTO> mockStudentReports() {
        List<StudentReportDTO> studentReportDTOS = List.of(
                new StudentReportDTO("name1", "lastname1", 1, 1L),
                new StudentReportDTO("name2", "lastname2", 2, 2L)
        );
        when(groupRepository.getStudentsReport(1)).thenReturn(studentReportDTOS);

        for (StudentReportDTO studentReportDTO : studentReportDTOS) {
            List<PresenceDTO> presenceDTOS = List.of(
                    new PresenceDTO(LocalDateTime.now(), PresenceType.O),
                    new PresenceDTO(LocalDateTime.now(), PresenceType.N)
            );
            studentReportDTO.setPresences(presenceDTOS);
            when(presenceRepository.getStudentPresenceByIndex(studentReportDTO.getIndex(), 1)).thenReturn(presenceDTOS);
        }
        return studentReportDTOS;
    }
}
