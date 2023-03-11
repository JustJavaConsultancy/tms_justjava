package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentBatchMapperTest {

    private PaymentBatchMapper paymentBatchMapper;

    @BeforeEach
    public void setUp() {
        paymentBatchMapper = new PaymentBatchMapperImpl();
    }
}
