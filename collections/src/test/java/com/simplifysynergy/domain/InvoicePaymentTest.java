package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoicePaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoicePayment.class);
        InvoicePayment invoicePayment1 = new InvoicePayment();
        invoicePayment1.setId("id1");
        InvoicePayment invoicePayment2 = new InvoicePayment();
        invoicePayment2.setId(invoicePayment1.getId());
        assertThat(invoicePayment1).isEqualTo(invoicePayment2);
        invoicePayment2.setId("id2");
        assertThat(invoicePayment1).isNotEqualTo(invoicePayment2);
        invoicePayment1.setId(null);
        assertThat(invoicePayment1).isNotEqualTo(invoicePayment2);
    }
}
