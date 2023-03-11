package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceTypeDTO.class);
        ServiceTypeDTO serviceTypeDTO1 = new ServiceTypeDTO();
        serviceTypeDTO1.setId("id1");
        ServiceTypeDTO serviceTypeDTO2 = new ServiceTypeDTO();
        assertThat(serviceTypeDTO1).isNotEqualTo(serviceTypeDTO2);
        serviceTypeDTO2.setId(serviceTypeDTO1.getId());
        assertThat(serviceTypeDTO1).isEqualTo(serviceTypeDTO2);
        serviceTypeDTO2.setId("id2");
        assertThat(serviceTypeDTO1).isNotEqualTo(serviceTypeDTO2);
        serviceTypeDTO1.setId(null);
        assertThat(serviceTypeDTO1).isNotEqualTo(serviceTypeDTO2);
    }
}
