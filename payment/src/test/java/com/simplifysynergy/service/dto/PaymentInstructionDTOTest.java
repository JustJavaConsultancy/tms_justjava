package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentInstructionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentInstructionDTO.class);
        PaymentInstructionDTO paymentInstructionDTO1 = new PaymentInstructionDTO();
        paymentInstructionDTO1.setId("id1");
        PaymentInstructionDTO paymentInstructionDTO2 = new PaymentInstructionDTO();
        assertThat(paymentInstructionDTO1).isNotEqualTo(paymentInstructionDTO2);
        paymentInstructionDTO2.setId(paymentInstructionDTO1.getId());
        assertThat(paymentInstructionDTO1).isEqualTo(paymentInstructionDTO2);
        paymentInstructionDTO2.setId("id2");
        assertThat(paymentInstructionDTO1).isNotEqualTo(paymentInstructionDTO2);
        paymentInstructionDTO1.setId(null);
        assertThat(paymentInstructionDTO1).isNotEqualTo(paymentInstructionDTO2);
    }
}
