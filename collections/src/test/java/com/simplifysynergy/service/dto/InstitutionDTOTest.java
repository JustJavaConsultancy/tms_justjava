package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstitutionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstitutionDTO.class);
        InstitutionDTO institutionDTO1 = new InstitutionDTO();
        institutionDTO1.setId("id1");
        InstitutionDTO institutionDTO2 = new InstitutionDTO();
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
        institutionDTO2.setId(institutionDTO1.getId());
        assertThat(institutionDTO1).isEqualTo(institutionDTO2);
        institutionDTO2.setId("id2");
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
        institutionDTO1.setId(null);
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
    }
}
