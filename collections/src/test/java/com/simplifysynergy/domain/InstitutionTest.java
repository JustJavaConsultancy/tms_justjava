package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstitutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Institution.class);
        Institution institution1 = new Institution();
        institution1.setId("id1");
        Institution institution2 = new Institution();
        institution2.setId(institution1.getId());
        assertThat(institution1).isEqualTo(institution2);
        institution2.setId("id2");
        assertThat(institution1).isNotEqualTo(institution2);
        institution1.setId(null);
        assertThat(institution1).isNotEqualTo(institution2);
    }
}
