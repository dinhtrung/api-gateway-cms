package com.ft.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class ContentFieldDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentFieldDTO.class);
        ContentFieldDTO contentFieldDTO1 = new ContentFieldDTO();
        contentFieldDTO1.setId("id1");
        ContentFieldDTO contentFieldDTO2 = new ContentFieldDTO();
        assertThat(contentFieldDTO1).isNotEqualTo(contentFieldDTO2);
        contentFieldDTO2.setId(contentFieldDTO1.getId());
        assertThat(contentFieldDTO1).isEqualTo(contentFieldDTO2);
        contentFieldDTO2.setId("id2");
        assertThat(contentFieldDTO1).isNotEqualTo(contentFieldDTO2);
        contentFieldDTO1.setId(null);
        assertThat(contentFieldDTO1).isNotEqualTo(contentFieldDTO2);
    }
}
