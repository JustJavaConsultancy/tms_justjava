package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReconciliationItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReconciliationItem.class);
        ReconciliationItem reconciliationItem1 = new ReconciliationItem();
        reconciliationItem1.setId("id1");
        ReconciliationItem reconciliationItem2 = new ReconciliationItem();
        reconciliationItem2.setId(reconciliationItem1.getId());
        assertThat(reconciliationItem1).isEqualTo(reconciliationItem2);
        reconciliationItem2.setId("id2");
        assertThat(reconciliationItem1).isNotEqualTo(reconciliationItem2);
        reconciliationItem1.setId(null);
        assertThat(reconciliationItem1).isNotEqualTo(reconciliationItem2);
    }
}
