package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestTypeDTO.class);
        RequestTypeDTO requestTypeDTO1 = new RequestTypeDTO();
        requestTypeDTO1.setId("id1");
        RequestTypeDTO requestTypeDTO2 = new RequestTypeDTO();
        assertThat(requestTypeDTO1).isNotEqualTo(requestTypeDTO2);
        requestTypeDTO2.setId(requestTypeDTO1.getId());
        assertThat(requestTypeDTO1).isEqualTo(requestTypeDTO2);
        requestTypeDTO2.setId("id2");
        assertThat(requestTypeDTO1).isNotEqualTo(requestTypeDTO2);
        requestTypeDTO1.setId(null);
        assertThat(requestTypeDTO1).isNotEqualTo(requestTypeDTO2);
    }
}
