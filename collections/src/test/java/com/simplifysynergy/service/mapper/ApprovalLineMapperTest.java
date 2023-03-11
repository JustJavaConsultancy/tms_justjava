package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApprovalLineMapperTest {

    private ApprovalLineMapper approvalLineMapper;

    @BeforeEach
    public void setUp() {
        approvalLineMapper = new ApprovalLineMapperImpl();
    }
}
