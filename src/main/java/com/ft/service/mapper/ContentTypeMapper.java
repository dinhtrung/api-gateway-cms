package com.ft.service.mapper;


import com.ft.domain.*;
import com.ft.service.dto.ContentTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContentType} and its DTO {@link ContentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContentTypeMapper extends EntityMapper<ContentTypeDTO, ContentType> {



    default ContentType fromId(String id) {
        if (id == null) {
            return null;
        }
        ContentType contentType = new ContentType();
        contentType.setId(id);
        return contentType;
    }
}
