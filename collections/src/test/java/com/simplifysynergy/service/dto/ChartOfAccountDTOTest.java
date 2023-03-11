package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChartOfAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChartOfAccountDTO.class);
        ChartOfAccountDTO chartOfAccountDTO1 = new ChartOfAccountDTO();
        chartOfAccountDTO1.setId("id1");
        ChartOfAccountDTO chartOfAccountDTO2 = new ChartOfAccountDTO();
        assertThat(chartOfAccountDTO1).isNotEqualTo(chartOfAccountDTO2);
        chartOfAccountDTO2.setId(chartOfAccountDTO1.getId());
        assertThat(chartOfAccountDTO1).isEqualTo(chartOfAccountDTO2);
        chartOfAccountDTO2.setId("id2");
        assertThat(chartOfAccountDTO1).isNotEqualTo(chartOfAccountDTO2);
        chartOfAccountDTO1.setId(null);
        assertThat(chartOfAccountDTO1).isNotEqualTo(chartOfAccountDTO2);
    }
}
