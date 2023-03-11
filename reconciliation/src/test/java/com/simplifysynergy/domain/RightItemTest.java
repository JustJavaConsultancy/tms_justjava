package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RightItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RightItem.class);
        RightItem rightItem1 = new RightItem();
        rightItem1.setId("id1");
        RightItem rightItem2 = new RightItem();
        rightItem2.setId(rightItem1.getId());
        assertThat(rightItem1).isEqualTo(rightItem2);
        rightItem2.setId("id2");
        assertThat(rightItem1).isNotEqualTo(rightItem2);
        rightItem1.setId(null);
        assertThat(rightItem1).isNotEqualTo(rightItem2);
    }
}
