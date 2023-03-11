package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RightItemMapperTest {

    private RightItemMapper rightItemMapper;

    @BeforeEach
    public void setUp() {
        rightItemMapper = new RightItemMapperImpl();
    }
}
