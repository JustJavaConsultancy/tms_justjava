package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstitutionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstitutionType.class);
        InstitutionType institutionType1 = new InstitutionType();
        institutionType1.setId("id1");
        InstitutionType institutionType2 = new InstitutionType();
        institutionType2.setId(institutionType1.getId());
        assertThat(institutionType1).isEqualTo(institutionType2);
        institutionType2.setId("id2");
        assertThat(institutionType1).isNotEqualTo(institutionType2);
        institutionType1.setId(null);
        assertThat(institutionType1).isNotEqualTo(institutionType2);
    }
}
