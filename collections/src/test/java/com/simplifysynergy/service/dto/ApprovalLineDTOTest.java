package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalLineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalLineDTO.class);
        ApprovalLineDTO approvalLineDTO1 = new ApprovalLineDTO();
        approvalLineDTO1.setId("id1");
        ApprovalLineDTO approvalLineDTO2 = new ApprovalLineDTO();
        assertThat(approvalLineDTO1).isNotEqualTo(approvalLineDTO2);
        approvalLineDTO2.setId(approvalLineDTO1.getId());
        assertThat(approvalLineDTO1).isEqualTo(approvalLineDTO2);
        approvalLineDTO2.setId("id2");
        assertThat(approvalLineDTO1).isNotEqualTo(approvalLineDTO2);
        approvalLineDTO1.setId(null);
        assertThat(approvalLineDTO1).isNotEqualTo(approvalLineDTO2);
    }
}
