package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InstitutionTypeMapperTest {

    private InstitutionTypeMapper institutionTypeMapper;

    @BeforeEach
    public void setUp() {
        institutionTypeMapper = new InstitutionTypeMapperImpl();
    }
}
