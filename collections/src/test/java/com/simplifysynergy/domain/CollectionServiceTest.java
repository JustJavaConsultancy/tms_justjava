package com.simplifysynergy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionService.class);
        CollectionService collectionService1 = new CollectionService();
        collectionService1.setId("id1");
        CollectionService collectionService2 = new CollectionService();
        collectionService2.setId(collectionService1.getId());
        assertThat(collectionService1).isEqualTo(collectionService2);
        collectionService2.setId("id2");
        assertThat(collectionService1).isNotEqualTo(collectionService2);
        collectionService1.setId(null);
        assertThat(collectionService1).isNotEqualTo(collectionService2);
    }
}
