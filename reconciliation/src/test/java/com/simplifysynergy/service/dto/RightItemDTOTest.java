package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RightItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RightItemDTO.class);
        RightItemDTO rightItemDTO1 = new RightItemDTO();
        rightItemDTO1.setId("id1");
        RightItemDTO rightItemDTO2 = new RightItemDTO();
        assertThat(rightItemDTO1).isNotEqualTo(rightItemDTO2);
        rightItemDTO2.setId(rightItemDTO1.getId());
        assertThat(rightItemDTO1).isEqualTo(rightItemDTO2);
        rightItemDTO2.setId("id2");
        assertThat(rightItemDTO1).isNotEqualTo(rightItemDTO2);
        rightItemDTO1.setId(null);
        assertThat(rightItemDTO1).isNotEqualTo(rightItemDTO2);
    }
}
