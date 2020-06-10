package com.ft.web.rest;

import com.ft.service.ContentFieldService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.service.dto.ContentFieldDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.reactive.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ft.domain.ContentField}.
 */
@RestController
@RequestMapping("/api")
public class ContentFieldResource {

    private final Logger log = LoggerFactory.getLogger(ContentFieldResource.class);

    private static final String ENTITY_NAME = "contentField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentFieldService contentFieldService;

    public ContentFieldResource(ContentFieldService contentFieldService) {
        this.contentFieldService = contentFieldService;
    }

    /**
     * {@code POST  /content-fields} : Create a new contentField.
     *
     * @param contentFieldDTO the contentFieldDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentFieldDTO, or with status {@code 400 (Bad Request)} if the contentField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/content-fields")
    public Mono<ResponseEntity<ContentFieldDTO>> createContentField(@Valid @RequestBody ContentFieldDTO contentFieldDTO) throws URISyntaxException {
        log.debug("REST request to save ContentField : {}", contentFieldDTO);
        if (contentFieldDTO.getId() != null) {
            throw new BadRequestAlertException("A new contentField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return contentFieldService.save(contentFieldDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/content-fields/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /content-fields} : Updates an existing contentField.
     *
     * @param contentFieldDTO the contentFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentFieldDTO,
     * or with status {@code 400 (Bad Request)} if the contentFieldDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/content-fields")
    public Mono<ResponseEntity<ContentFieldDTO>> updateContentField(@Valid @RequestBody ContentFieldDTO contentFieldDTO) throws URISyntaxException {
        log.debug("REST request to update ContentField : {}", contentFieldDTO);
        if (contentFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return contentFieldService.save(contentFieldDTO)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(result -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result)
            );
    }

    /**
     * {@code GET  /content-fields} : get all the contentFields.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentFields in body.
     */
    @GetMapping("/content-fields")
    public Mono<ResponseEntity<Flux<ContentFieldDTO>>> getAllContentFields(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ContentFields");
        return contentFieldService.countAll()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(contentFieldService.findAll(pageable)));
    }

    /**
     * {@code GET  /content-fields/:id} : get the "id" contentField.
     *
     * @param id the id of the contentFieldDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentFieldDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-fields/{id}")
    public Mono<ResponseEntity<ContentFieldDTO>> getContentField(@PathVariable String id) {
        log.debug("REST request to get ContentField : {}", id);
        Mono<ContentFieldDTO> contentFieldDTO = contentFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentFieldDTO);
    }

    /**
     * {@code DELETE  /content-fields/:id} : delete the "id" contentField.
     *
     * @param id the id of the contentFieldDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/content-fields/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteContentField(@PathVariable String id) {
        log.debug("REST request to delete ContentField : {}", id);
        return contentFieldService.delete(id)            .map(result -> ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
        );
    }
}
