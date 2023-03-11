package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeftItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeftItem.class);
        LeftItem leftItem1 = new LeftItem();
        leftItem1.setId("id1");
        LeftItem leftItem2 = new LeftItem();
        leftItem2.setId(leftItem1.getId());
        assertThat(leftItem1).isEqualTo(leftItem2);
        leftItem2.setId("id2");
        assertThat(leftItem1).isNotEqualTo(leftItem2);
        leftItem1.setId(null);
        assertThat(leftItem1).isNotEqualTo(leftItem2);
    }
}
