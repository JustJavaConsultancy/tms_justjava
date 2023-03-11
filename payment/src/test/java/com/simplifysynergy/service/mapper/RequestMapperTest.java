package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestMapperTest {

    private RequestMapper requestMapper;

    @BeforeEach
    public void setUp() {
        requestMapper = new RequestMapperImpl();
    }
}
