package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApprovalRouteMapperTest {

    private ApprovalRouteMapper approvalRouteMapper;

    @BeforeEach
    public void setUp() {
        approvalRouteMapper = new ApprovalRouteMapperImpl();
    }
}
