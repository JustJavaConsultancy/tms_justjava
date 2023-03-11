package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentInstructionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentInstruction.class);
        PaymentInstruction paymentInstruction1 = new PaymentInstruction();
        paymentInstruction1.setId("id1");
        PaymentInstruction paymentInstruction2 = new PaymentInstruction();
        paymentInstruction2.setId(paymentInstruction1.getId());
        assertThat(paymentInstruction1).isEqualTo(paymentInstruction2);
        paymentInstruction2.setId("id2");
        assertThat(paymentInstruction1).isNotEqualTo(paymentInstruction2);
        paymentInstruction1.setId(null);
        assertThat(paymentInstruction1).isNotEqualTo(paymentInstruction2);
    }
}
