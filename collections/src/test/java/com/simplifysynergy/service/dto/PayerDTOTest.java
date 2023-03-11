package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayerDTO.class);
        PayerDTO payerDTO1 = new PayerDTO();
        payerDTO1.setId("id1");
        PayerDTO payerDTO2 = new PayerDTO();
        assertThat(payerDTO1).isNotEqualTo(payerDTO2);
        payerDTO2.setId(payerDTO1.getId());
        assertThat(payerDTO1).isEqualTo(payerDTO2);
        payerDTO2.setId("id2");
        assertThat(payerDTO1).isNotEqualTo(payerDTO2);
        payerDTO1.setId(null);
        assertThat(payerDTO1).isNotEqualTo(payerDTO2);
    }
}
