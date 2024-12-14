package org.example.test.group;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.example.dtos.groups.GroupDTO;
import org.example.dtos.groups.ReportDTO;
import org.example.dtos.groups.StudentReportDTO;
import org.example.model.Group;
import org.example.reopsitory.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class GroupRepositoryTest {
    @Autowired
    GroupRepository groupRepository;

    @Test
    @DatabaseSetup("classpath:group/group.xml")
    void givenGroupId_whenFindById_thenReturnGroup() {
        Group group = groupRepository.findById(1L);
        assertThat(group).isNotNull();
        assertThat(group.getSubject()).isEqualTo("subject1");
    }

    @Test
    @DatabaseSetup("classpath:group/group.xml")
    void givenGroupId_whenGenerateReport_thenReturnReportDTO() {
        ReportDTO reportDTO = groupRepository.generateReport(1L);
        assertThat(reportDTO).isNotNull();
        assertThat(reportDTO.getGroupId()).isEqualTo(1L);
        assertThat(reportDTO.getSubject()).isEqualTo("subject1");
    }

    @Test
    @DatabaseSetup("classpath:group/studentReport.xml")
    void givenGroupId_whenGenerateStudentsReport_thenReturnStudentReportDTO() {
        List<StudentReportDTO> studentReportDTO = groupRepository.getStudentsReport(1L);
        assertThat(studentReportDTO).isNotNull();
        assertThat(studentReportDTO.size()).isEqualTo(2);
        assertThat(studentReportDTO.get(0).getName()).isEqualTo("name2");
        assertThat(studentReportDTO.get(0).getLastname()).isEqualTo("last2");
        assertThat(studentReportDTO.get(0).getIndex()).isEqualTo(2);
        assertThat(studentReportDTO.get(0).getTotalPoints()).isEqualTo(5);
        assertThat(studentReportDTO.get(1).getTotalPoints()).isEqualTo(-1);
    }
}
