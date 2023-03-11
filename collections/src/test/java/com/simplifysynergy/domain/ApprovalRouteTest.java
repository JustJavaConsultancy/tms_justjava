package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalRouteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalRoute.class);
        ApprovalRoute approvalRoute1 = new ApprovalRoute();
        approvalRoute1.setId("id1");
        ApprovalRoute approvalRoute2 = new ApprovalRoute();
        approvalRoute2.setId(approvalRoute1.getId());
        assertThat(approvalRoute1).isEqualTo(approvalRoute2);
        approvalRoute2.setId("id2");
        assertThat(approvalRoute1).isNotEqualTo(approvalRoute2);
        approvalRoute1.setId(null);
        assertThat(approvalRoute1).isNotEqualTo(approvalRoute2);
    }
}
