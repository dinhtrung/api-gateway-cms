package com.ft.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class ContentFieldTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentField.class);
        ContentField contentField1 = new ContentField();
        contentField1.setId("id1");
        ContentField contentField2 = new ContentField();
        contentField2.setId(contentField1.getId());
        assertThat(contentField1).isEqualTo(contentField2);
        contentField2.setId("id2");
        assertThat(contentField1).isNotEqualTo(contentField2);
        contentField1.setId(null);
        assertThat(contentField1).isNotEqualTo(contentField2);
    }
}
