package com.ft.service;

import com.ft.domain.ContentType;
import com.ft.repository.ContentTypeRepository;
import com.ft.service.dto.ContentTypeDTO;
import com.ft.service.mapper.ContentTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service Implementation for managing {@link ContentType}.
 */
@Service
public class ContentTypeService {

    private final Logger log = LoggerFactory.getLogger(ContentTypeService.class);

    private final ContentTypeRepository contentTypeRepository;

    private final ContentTypeMapper contentTypeMapper;

    public ContentTypeService(ContentTypeRepository contentTypeRepository, ContentTypeMapper contentTypeMapper) {
        this.contentTypeRepository = contentTypeRepository;
        this.contentTypeMapper = contentTypeMapper;
    }

    /**
     * Save a contentType.
     *
     * @param contentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContentTypeDTO> save(ContentTypeDTO contentTypeDTO) {
        log.debug("Request to save ContentType : {}", contentTypeDTO);
        return contentTypeRepository.save(contentTypeMapper.toEntity(contentTypeDTO))
            .map(contentTypeMapper::toDto)
;    }

    /**
     * Get all the contentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ContentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContentTypes");
        return contentTypeRepository.findAllBy(pageable)
            .map(contentTypeMapper::toDto);
    }


    /**
    * Returns the number of contentTypes available.
    *
    */
    public Mono<Long> countAll() {
        return contentTypeRepository.count();
    }

    /**
     * Get one contentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ContentTypeDTO> findOne(String id) {
        log.debug("Request to get ContentType : {}", id);
        return contentTypeRepository.findById(id)
            .map(contentTypeMapper::toDto);
    }

    /**
     * Delete the contentType by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ContentType : {}", id);
        return contentTypeRepository.deleteById(id);    }
}
