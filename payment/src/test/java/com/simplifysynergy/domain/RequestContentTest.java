package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestContent.class);
        RequestContent requestContent1 = new RequestContent();
        requestContent1.setId("id1");
        RequestContent requestContent2 = new RequestContent();
        requestContent2.setId(requestContent1.getId());
        assertThat(requestContent1).isEqualTo(requestContent2);
        requestContent2.setId("id2");
        assertThat(requestContent1).isNotEqualTo(requestContent2);
        requestContent1.setId(null);
        assertThat(requestContent1).isNotEqualTo(requestContent2);
    }
}
