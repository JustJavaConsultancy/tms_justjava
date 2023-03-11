package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestContentMapperTest {

    private RequestContentMapper requestContentMapper;

    @BeforeEach
    public void setUp() {
        requestContentMapper = new RequestContentMapperImpl();
    }
}
