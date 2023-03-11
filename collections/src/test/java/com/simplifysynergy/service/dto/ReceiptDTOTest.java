package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceiptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceiptDTO.class);
        ReceiptDTO receiptDTO1 = new ReceiptDTO();
        receiptDTO1.setId("id1");
        ReceiptDTO receiptDTO2 = new ReceiptDTO();
        assertThat(receiptDTO1).isNotEqualTo(receiptDTO2);
        receiptDTO2.setId(receiptDTO1.getId());
        assertThat(receiptDTO1).isEqualTo(receiptDTO2);
        receiptDTO2.setId("id2");
        assertThat(receiptDTO1).isNotEqualTo(receiptDTO2);
        receiptDTO1.setId(null);
        assertThat(receiptDTO1).isNotEqualTo(receiptDTO2);
    }
}
