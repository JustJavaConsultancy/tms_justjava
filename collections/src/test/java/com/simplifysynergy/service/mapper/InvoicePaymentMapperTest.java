package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoicePaymentMapperTest {

    private InvoicePaymentMapper invoicePaymentMapper;

    @BeforeEach
    public void setUp() {
        invoicePaymentMapper = new InvoicePaymentMapperImpl();
    }
}
