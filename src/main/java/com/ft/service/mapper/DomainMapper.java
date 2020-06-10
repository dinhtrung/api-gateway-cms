package com.ft.service.mapper;


import com.ft.domain.*;
import com.ft.service.dto.DomainDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Domain} and its DTO {@link DomainDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DomainMapper extends EntityMapper<DomainDTO, Domain> {



    default Domain fromId(String id) {
        if (id == null) {
            return null;
        }
        Domain domain = new Domain();
        domain.setId(id);
        return domain;
    }
}
