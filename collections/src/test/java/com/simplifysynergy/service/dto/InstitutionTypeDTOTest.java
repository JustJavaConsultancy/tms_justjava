package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstitutionTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstitutionTypeDTO.class);
        InstitutionTypeDTO institutionTypeDTO1 = new InstitutionTypeDTO();
        institutionTypeDTO1.setId("id1");
        InstitutionTypeDTO institutionTypeDTO2 = new InstitutionTypeDTO();
        assertThat(institutionTypeDTO1).isNotEqualTo(institutionTypeDTO2);
        institutionTypeDTO2.setId(institutionTypeDTO1.getId());
        assertThat(institutionTypeDTO1).isEqualTo(institutionTypeDTO2);
        institutionTypeDTO2.setId("id2");
        assertThat(institutionTypeDTO1).isNotEqualTo(institutionTypeDTO2);
        institutionTypeDTO1.setId(null);
        assertThat(institutionTypeDTO1).isNotEqualTo(institutionTypeDTO2);
    }
}
