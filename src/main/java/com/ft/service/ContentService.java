package com.ft.service;

import com.ft.domain.Content;
import com.ft.repository.ContentRepository;
import com.ft.service.dto.ContentDTO;
import com.ft.service.mapper.ContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service Implementation for managing {@link Content}.
 */
@Service
public class ContentService {

    private final Logger log = LoggerFactory.getLogger(ContentService.class);

    private final ContentRepository contentRepository;

    private final ContentMapper contentMapper;

    public ContentService(ContentRepository contentRepository, ContentMapper contentMapper) {
        this.contentRepository = contentRepository;
        this.contentMapper = contentMapper;
    }

    /**
     * Save a content.
     *
     * @param contentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContentDTO> save(ContentDTO contentDTO) {
        log.debug("Request to save Content : {}", contentDTO);
        return contentRepository.save(contentMapper.toEntity(contentDTO))
            .map(contentMapper::toDto)
;    }

    /**
     * Get all the contents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contents");
        return contentRepository.findAllBy(pageable)
            .map(contentMapper::toDto);
    }


    /**
    * Returns the number of contents available.
    *
    */
    public Mono<Long> countAll() {
        return contentRepository.count();
    }

    /**
     * Get one content by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ContentDTO> findOne(String id) {
        log.debug("Request to get Content : {}", id);
        return contentRepository.findById(id)
            .map(contentMapper::toDto);
    }

    /**
     * Delete the content by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Content : {}", id);
        return contentRepository.deleteById(id);    }
}
