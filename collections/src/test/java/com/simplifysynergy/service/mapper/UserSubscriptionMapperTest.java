package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserSubscriptionMapperTest {

    private UserSubscriptionMapper userSubscriptionMapper;

    @BeforeEach
    public void setUp() {
        userSubscriptionMapper = new UserSubscriptionMapperImpl();
    }
}
