package com.ft.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class ContentTypeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentTypeDTO.class);
        ContentTypeDTO contentTypeDTO1 = new ContentTypeDTO();
        contentTypeDTO1.setId("id1");
        ContentTypeDTO contentTypeDTO2 = new ContentTypeDTO();
        assertThat(contentTypeDTO1).isNotEqualTo(contentTypeDTO2);
        contentTypeDTO2.setId(contentTypeDTO1.getId());
        assertThat(contentTypeDTO1).isEqualTo(contentTypeDTO2);
        contentTypeDTO2.setId("id2");
        assertThat(contentTypeDTO1).isNotEqualTo(contentTypeDTO2);
        contentTypeDTO1.setId(null);
        assertThat(contentTypeDTO1).isNotEqualTo(contentTypeDTO2);
    }
}
