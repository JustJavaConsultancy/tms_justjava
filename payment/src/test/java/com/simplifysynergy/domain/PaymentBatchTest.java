package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentBatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentBatch.class);
        PaymentBatch paymentBatch1 = new PaymentBatch();
        paymentBatch1.setId("id1");
        PaymentBatch paymentBatch2 = new PaymentBatch();
        paymentBatch2.setId(paymentBatch1.getId());
        assertThat(paymentBatch1).isEqualTo(paymentBatch2);
        paymentBatch2.setId("id2");
        assertThat(paymentBatch1).isNotEqualTo(paymentBatch2);
        paymentBatch1.setId(null);
        assertThat(paymentBatch1).isNotEqualTo(paymentBatch2);
    }
}
