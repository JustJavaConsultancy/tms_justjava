package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentBatchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentBatchDTO.class);
        PaymentBatchDTO paymentBatchDTO1 = new PaymentBatchDTO();
        paymentBatchDTO1.setId("id1");
        PaymentBatchDTO paymentBatchDTO2 = new PaymentBatchDTO();
        assertThat(paymentBatchDTO1).isNotEqualTo(paymentBatchDTO2);
        paymentBatchDTO2.setId(paymentBatchDTO1.getId());
        assertThat(paymentBatchDTO1).isEqualTo(paymentBatchDTO2);
        paymentBatchDTO2.setId("id2");
        assertThat(paymentBatchDTO1).isNotEqualTo(paymentBatchDTO2);
        paymentBatchDTO1.setId(null);
        assertThat(paymentBatchDTO1).isNotEqualTo(paymentBatchDTO2);
    }
}
