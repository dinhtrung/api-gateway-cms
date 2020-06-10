package com.ft.service;

import com.ft.domain.Domain;
import com.ft.repository.DomainRepository;
import com.ft.service.dto.DomainDTO;
import com.ft.service.mapper.DomainMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service Implementation for managing {@link Domain}.
 */
@Service
public class DomainService {

    private final Logger log = LoggerFactory.getLogger(DomainService.class);

    private final DomainRepository domainRepository;

    private final DomainMapper domainMapper;

    public DomainService(DomainRepository domainRepository, DomainMapper domainMapper) {
        this.domainRepository = domainRepository;
        this.domainMapper = domainMapper;
    }

    /**
     * Save a domain.
     *
     * @param domainDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DomainDTO> save(DomainDTO domainDTO) {
        log.debug("Request to save Domain : {}", domainDTO);
        return domainRepository.save(domainMapper.toEntity(domainDTO))
            .map(domainMapper::toDto)
;    }

    /**
     * Get all the domains.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<DomainDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Domains");
        return domainRepository.findAllBy(pageable)
            .map(domainMapper::toDto);
    }


    /**
    * Returns the number of domains available.
    *
    */
    public Mono<Long> countAll() {
        return domainRepository.count();
    }

    /**
     * Get one domain by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<DomainDTO> findOne(String id) {
        log.debug("Request to get Domain : {}", id);
        return domainRepository.findById(id)
            .map(domainMapper::toDto);
    }

    /**
     * Delete the domain by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Domain : {}", id);
        return domainRepository.deleteById(id);    }
}
