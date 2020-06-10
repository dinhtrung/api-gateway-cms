package com.ft.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class ContentTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentType.class);
        ContentType contentType1 = new ContentType();
        contentType1.setId("id1");
        ContentType contentType2 = new ContentType();
        contentType2.setId(contentType1.getId());
        assertThat(contentType1).isEqualTo(contentType2);
        contentType2.setId("id2");
        assertThat(contentType1).isNotEqualTo(contentType2);
        contentType1.setId(null);
        assertThat(contentType1).isNotEqualTo(contentType2);
    }
}
