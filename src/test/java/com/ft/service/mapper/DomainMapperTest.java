package com.ft.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DomainMapperTest {

    private DomainMapper domainMapper;

    @BeforeEach
    public void setUp() {
        domainMapper = new DomainMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(domainMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(domainMapper.fromId(null)).isNull();
    }
}
