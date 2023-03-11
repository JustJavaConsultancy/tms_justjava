package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalLine.class);
        ApprovalLine approvalLine1 = new ApprovalLine();
        approvalLine1.setId("id1");
        ApprovalLine approvalLine2 = new ApprovalLine();
        approvalLine2.setId(approvalLine1.getId());
        assertThat(approvalLine1).isEqualTo(approvalLine2);
        approvalLine2.setId("id2");
        assertThat(approvalLine1).isNotEqualTo(approvalLine2);
        approvalLine1.setId(null);
        assertThat(approvalLine1).isNotEqualTo(approvalLine2);
    }
}
