package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReconciliationItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReconciliationItemDTO.class);
        ReconciliationItemDTO reconciliationItemDTO1 = new ReconciliationItemDTO();
        reconciliationItemDTO1.setId("id1");
        ReconciliationItemDTO reconciliationItemDTO2 = new ReconciliationItemDTO();
        assertThat(reconciliationItemDTO1).isNotEqualTo(reconciliationItemDTO2);
        reconciliationItemDTO2.setId(reconciliationItemDTO1.getId());
        assertThat(reconciliationItemDTO1).isEqualTo(reconciliationItemDTO2);
        reconciliationItemDTO2.setId("id2");
        assertThat(reconciliationItemDTO1).isNotEqualTo(reconciliationItemDTO2);
        reconciliationItemDTO1.setId(null);
        assertThat(reconciliationItemDTO1).isNotEqualTo(reconciliationItemDTO2);
    }
}
