package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentInstructionMapperTest {

    private PaymentInstructionMapper paymentInstructionMapper;

    @BeforeEach
    public void setUp() {
        paymentInstructionMapper = new PaymentInstructionMapperImpl();
    }
}
