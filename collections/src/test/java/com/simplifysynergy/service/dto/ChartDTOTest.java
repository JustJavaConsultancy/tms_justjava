package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChartDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChartDTO.class);
        ChartDTO chartDTO1 = new ChartDTO();
        chartDTO1.setId("id1");
        ChartDTO chartDTO2 = new ChartDTO();
        assertThat(chartDTO1).isNotEqualTo(chartDTO2);
        chartDTO2.setId(chartDTO1.getId());
        assertThat(chartDTO1).isEqualTo(chartDTO2);
        chartDTO2.setId("id2");
        assertThat(chartDTO1).isNotEqualTo(chartDTO2);
        chartDTO1.setId(null);
        assertThat(chartDTO1).isNotEqualTo(chartDTO2);
    }
}
