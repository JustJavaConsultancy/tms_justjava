package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChartOfAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChartOfAccount.class);
        ChartOfAccount chartOfAccount1 = new ChartOfAccount();
        chartOfAccount1.setId("id1");
        ChartOfAccount chartOfAccount2 = new ChartOfAccount();
        chartOfAccount2.setId(chartOfAccount1.getId());
        assertThat(chartOfAccount1).isEqualTo(chartOfAccount2);
        chartOfAccount2.setId("id2");
        assertThat(chartOfAccount1).isNotEqualTo(chartOfAccount2);
        chartOfAccount1.setId(null);
        assertThat(chartOfAccount1).isNotEqualTo(chartOfAccount2);
    }
}
