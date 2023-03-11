package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeftItemMapperTest {

    private LeftItemMapper leftItemMapper;

    @BeforeEach
    public void setUp() {
        leftItemMapper = new LeftItemMapperImpl();
    }
}
