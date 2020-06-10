package com.ft.web.rest;

import com.ft.service.ContentTypeService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.service.dto.ContentTypeDTO;

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
 * REST controller for managing {@link com.ft.domain.ContentType}.
 */
@RestController
@RequestMapping("/api")
public class ContentTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContentTypeResource.class);

    private static final String ENTITY_NAME = "contentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentTypeService contentTypeService;

    public ContentTypeResource(ContentTypeService contentTypeService) {
        this.contentTypeService = contentTypeService;
    }

    /**
     * {@code POST  /content-types} : Create a new contentType.
     *
     * @param contentTypeDTO the contentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentTypeDTO, or with status {@code 400 (Bad Request)} if the contentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/content-types")
    public Mono<ResponseEntity<ContentTypeDTO>> createContentType(@Valid @RequestBody ContentTypeDTO contentTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ContentType : {}", contentTypeDTO);
        if (contentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new contentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return contentTypeService.save(contentTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/content-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /content-types} : Updates an existing contentType.
     *
     * @param contentTypeDTO the contentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the contentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/content-types")
    public Mono<ResponseEntity<ContentTypeDTO>> updateContentType(@Valid @RequestBody ContentTypeDTO contentTypeDTO) throws URISyntaxException {
        log.debug("REST request to update ContentType : {}", contentTypeDTO);
        if (contentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return contentTypeService.save(contentTypeDTO)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(result -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result)
            );
    }

    /**
     * {@code GET  /content-types} : get all the contentTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentTypes in body.
     */
    @GetMapping("/content-types")
    public Mono<ResponseEntity<Flux<ContentTypeDTO>>> getAllContentTypes(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ContentTypes");
        return contentTypeService.countAll()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(contentTypeService.findAll(pageable)));
    }

    /**
     * {@code GET  /content-types/:id} : get the "id" contentType.
     *
     * @param id the id of the contentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-types/{id}")
    public Mono<ResponseEntity<ContentTypeDTO>> getContentType(@PathVariable String id) {
        log.debug("REST request to get ContentType : {}", id);
        Mono<ContentTypeDTO> contentTypeDTO = contentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentTypeDTO);
    }

    /**
     * {@code DELETE  /content-types/:id} : delete the "id" contentType.
     *
     * @param id the id of the contentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/content-types/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteContentType(@PathVariable String id) {
        log.debug("REST request to delete ContentType : {}", id);
        return contentTypeService.delete(id)            .map(result -> ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
        );
    }
}
