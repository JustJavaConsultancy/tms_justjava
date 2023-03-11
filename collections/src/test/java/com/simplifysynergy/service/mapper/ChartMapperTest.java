package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChartMapperTest {

    private ChartMapper chartMapper;

    @BeforeEach
    public void setUp() {
        chartMapper = new ChartMapperImpl();
    }
}
