package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payer.class);
        Payer payer1 = new Payer();
        payer1.setId("id1");
        Payer payer2 = new Payer();
        payer2.setId(payer1.getId());
        assertThat(payer1).isEqualTo(payer2);
        payer2.setId("id2");
        assertThat(payer1).isNotEqualTo(payer2);
        payer1.setId(null);
        assertThat(payer1).isNotEqualTo(payer2);
    }
}
