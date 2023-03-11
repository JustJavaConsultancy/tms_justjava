package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalRouteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalRouteDTO.class);
        ApprovalRouteDTO approvalRouteDTO1 = new ApprovalRouteDTO();
        approvalRouteDTO1.setId("id1");
        ApprovalRouteDTO approvalRouteDTO2 = new ApprovalRouteDTO();
        assertThat(approvalRouteDTO1).isNotEqualTo(approvalRouteDTO2);
        approvalRouteDTO2.setId(approvalRouteDTO1.getId());
        assertThat(approvalRouteDTO1).isEqualTo(approvalRouteDTO2);
        approvalRouteDTO2.setId("id2");
        assertThat(approvalRouteDTO1).isNotEqualTo(approvalRouteDTO2);
        approvalRouteDTO1.setId(null);
        assertThat(approvalRouteDTO1).isNotEqualTo(approvalRouteDTO2);
    }
}
