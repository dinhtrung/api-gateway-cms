package com.ft.service.mapper;


import com.ft.domain.*;
import com.ft.service.dto.ContentFieldDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContentField} and its DTO {@link ContentFieldDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContentFieldMapper extends EntityMapper<ContentFieldDTO, ContentField> {



    default ContentField fromId(String id) {
        if (id == null) {
            return null;
        }
        ContentField contentField = new ContentField();
        contentField.setId(id);
        return contentField;
    }
}
