package com.ft.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ContentFieldMapperTest {

    private ContentFieldMapper contentFieldMapper;

    @BeforeEach
    public void setUp() {
        contentFieldMapper = new ContentFieldMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(contentFieldMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contentFieldMapper.fromId(null)).isNull();
    }
}
