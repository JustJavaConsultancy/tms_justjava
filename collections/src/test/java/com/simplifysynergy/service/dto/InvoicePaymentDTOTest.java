package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoicePaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoicePaymentDTO.class);
        InvoicePaymentDTO invoicePaymentDTO1 = new InvoicePaymentDTO();
        invoicePaymentDTO1.setId("id1");
        InvoicePaymentDTO invoicePaymentDTO2 = new InvoicePaymentDTO();
        assertThat(invoicePaymentDTO1).isNotEqualTo(invoicePaymentDTO2);
        invoicePaymentDTO2.setId(invoicePaymentDTO1.getId());
        assertThat(invoicePaymentDTO1).isEqualTo(invoicePaymentDTO2);
        invoicePaymentDTO2.setId("id2");
        assertThat(invoicePaymentDTO1).isNotEqualTo(invoicePaymentDTO2);
        invoicePaymentDTO1.setId(null);
        assertThat(invoicePaymentDTO1).isNotEqualTo(invoicePaymentDTO2);
    }
}
