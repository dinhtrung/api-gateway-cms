package com.ft.service.mapper;


import com.ft.domain.*;
import com.ft.service.dto.ContentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Content} and its DTO {@link ContentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContentMapper extends EntityMapper<ContentDTO, Content> {



    default Content fromId(String id) {
        if (id == null) {
            return null;
        }
        Content content = new Content();
        content.setId(id);
        return content;
    }
}
