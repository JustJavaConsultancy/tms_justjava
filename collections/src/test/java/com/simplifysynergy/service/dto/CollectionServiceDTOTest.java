package com.simplifysynergy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplifysynergy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionServiceDTO.class);
        CollectionServiceDTO collectionServiceDTO1 = new CollectionServiceDTO();
        collectionServiceDTO1.setId("id1");
        CollectionServiceDTO collectionServiceDTO2 = new CollectionServiceDTO();
        assertThat(collectionServiceDTO1).isNotEqualTo(collectionServiceDTO2);
        collectionServiceDTO2.setId(collectionServiceDTO1.getId());
        assertThat(collectionServiceDTO1).isEqualTo(collectionServiceDTO2);
        collectionServiceDTO2.setId("id2");
        assertThat(collectionServiceDTO1).isNotEqualTo(collectionServiceDTO2);
        collectionServiceDTO1.setId(null);
        assertThat(collectionServiceDTO1).isNotEqualTo(collectionServiceDTO2);
    }
}
