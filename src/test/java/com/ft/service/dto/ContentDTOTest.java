package com.ft.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class ContentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentDTO.class);
        ContentDTO contentDTO1 = new ContentDTO();
        contentDTO1.setId("id1");
        ContentDTO contentDTO2 = new ContentDTO();
        assertThat(contentDTO1).isNotEqualTo(contentDTO2);
        contentDTO2.setId(contentDTO1.getId());
        assertThat(contentDTO1).isEqualTo(contentDTO2);
        contentDTO2.setId("id2");
        assertThat(contentDTO1).isNotEqualTo(contentDTO2);
        contentDTO1.setId(null);
        assertThat(contentDTO1).isNotEqualTo(contentDTO2);
    }
}