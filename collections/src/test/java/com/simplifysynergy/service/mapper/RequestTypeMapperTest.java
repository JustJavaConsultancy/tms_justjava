package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestTypeMapperTest {

    private RequestTypeMapper requestTypeMapper;

    @BeforeEach
    public void setUp() {
        requestTypeMapper = new RequestTypeMapperImpl();
    }
}
