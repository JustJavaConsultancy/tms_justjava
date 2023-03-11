package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceTypeMapperTest {

    private ServiceTypeMapper serviceTypeMapper;

    @BeforeEach
    public void setUp() {
        serviceTypeMapper = new ServiceTypeMapperImpl();
    }
}
