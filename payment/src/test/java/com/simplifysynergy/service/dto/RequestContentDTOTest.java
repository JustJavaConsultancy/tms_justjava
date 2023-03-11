package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestContentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestContentDTO.class);
        RequestContentDTO requestContentDTO1 = new RequestContentDTO();
        requestContentDTO1.setId("id1");
        RequestContentDTO requestContentDTO2 = new RequestContentDTO();
        assertThat(requestContentDTO1).isNotEqualTo(requestContentDTO2);
        requestContentDTO2.setId(requestContentDTO1.getId());
        assertThat(requestContentDTO1).isEqualTo(requestContentDTO2);
        requestContentDTO2.setId("id2");
        assertThat(requestContentDTO1).isNotEqualTo(requestContentDTO2);
        requestContentDTO1.setId(null);
        assertThat(requestContentDTO1).isNotEqualTo(requestContentDTO2);
    }
}
