package com.ft.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ContentTypeMapperTest {

    private ContentTypeMapper contentTypeMapper;

    @BeforeEach
    public void setUp() {
        contentTypeMapper = new ContentTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(contentTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contentTypeMapper.fromId(null)).isNull();
    }
}
