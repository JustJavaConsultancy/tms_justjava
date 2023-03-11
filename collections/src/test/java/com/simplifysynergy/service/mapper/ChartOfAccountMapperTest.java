package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChartOfAccountMapperTest {

    private ChartOfAccountMapper chartOfAccountMapper;

    @BeforeEach
    public void setUp() {
        chartOfAccountMapper = new ChartOfAccountMapperImpl();
    }
}
