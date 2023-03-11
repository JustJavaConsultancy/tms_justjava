package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeftItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeftItemDTO.class);
        LeftItemDTO leftItemDTO1 = new LeftItemDTO();
        leftItemDTO1.setId("id1");
        LeftItemDTO leftItemDTO2 = new LeftItemDTO();
        assertThat(leftItemDTO1).isNotEqualTo(leftItemDTO2);
        leftItemDTO2.setId(leftItemDTO1.getId());
        assertThat(leftItemDTO1).isEqualTo(leftItemDTO2);
        leftItemDTO2.setId("id2");
        assertThat(leftItemDTO1).isNotEqualTo(leftItemDTO2);
        leftItemDTO1.setId(null);
        assertThat(leftItemDTO1).isNotEqualTo(leftItemDTO2);
    }
}
