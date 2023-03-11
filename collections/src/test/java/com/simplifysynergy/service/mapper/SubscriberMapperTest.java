package com.simplifysynergy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriberMapperTest {

    private SubscriberMapper subscriberMapper;

    @BeforeEach
    public void setUp() {
        subscriberMapper = new SubscriberMapperImpl();
    }
}
