package com.ft.service;

import com.ft.domain.ContentField;
import com.ft.repository.ContentFieldRepository;
import com.ft.service.dto.ContentFieldDTO;
import com.ft.service.mapper.ContentFieldMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service Implementation for managing {@link ContentField}.
 */
@Service
public class ContentFieldService {

    private final Logger log = LoggerFactory.getLogger(ContentFieldService.class);

    private final ContentFieldRepository contentFieldRepository;

    private final ContentFieldMapper contentFieldMapper;

    public ContentFieldService(ContentFieldRepository contentFieldRepository, ContentFieldMapper contentFieldMapper) {
        this.contentFieldRepository = contentFieldRepository;
        this.contentFieldMapper = contentFieldMapper;
    }

    /**
     * Save a contentField.
     *
     * @param contentFieldDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContentFieldDTO> save(ContentFieldDTO contentFieldDTO) {
        log.debug("Request to save ContentField : {}", contentFieldDTO);
        return contentFieldRepository.save(contentFieldMapper.toEntity(contentFieldDTO))
            .map(contentFieldMapper::toDto)
;    }

    /**
     * Get all the contentFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ContentFieldDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContentFields");
        return contentFieldRepository.findAllBy(pageable)
            .map(contentFieldMapper::toDto);
    }


    /**
    * Returns the number of contentFields available.
    *
    */
    public Mono<Long> countAll() {
        return contentFieldRepository.count();
    }

    /**
     * Get one contentField by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ContentFieldDTO> findOne(String id) {
        log.debug("Request to get ContentField : {}", id);
        return contentFieldRepository.findById(id)
            .map(contentFieldMapper::toDto);
    }

    /**
     * Delete the contentField by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ContentField : {}", id);
        return contentFieldRepository.deleteById(id);    }
}
