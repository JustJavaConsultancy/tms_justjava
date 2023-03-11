package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PayerMapperTest {

    private PayerMapper payerMapper;

    @BeforeEach
    public void setUp() {
        payerMapper = new PayerMapperImpl();
    }
}
