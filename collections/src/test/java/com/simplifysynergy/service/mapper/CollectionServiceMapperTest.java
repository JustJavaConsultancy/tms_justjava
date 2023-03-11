package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionServiceMapperTest {

    private CollectionServiceMapper collectionServiceMapper;

    @BeforeEach
    public void setUp() {
        collectionServiceMapper = new CollectionServiceMapperImpl();
    }
}
