package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReconciliationItemMapperTest {

    private ReconciliationItemMapper reconciliationItemMapper;

    @BeforeEach
    public void setUp() {
        reconciliationItemMapper = new ReconciliationItemMapperImpl();
    }
}
