package com.ft.web.rest;

import com.ft.CmsGatewayApp;
import com.ft.domain.Domain;
import com.ft.repository.DomainRepository;
import com.ft.service.DomainService;
import com.ft.service.dto.DomainDTO;
import com.ft.service.mapper.DomainMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the {@link DomainResource} REST controller.
 */
@SpringBootTest(classes = CmsGatewayApp.class)
@AutoConfigureWebTestClient
@WithMockUser
public class DomainResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DomainMapper domainMapper;

    @Autowired
    private DomainService domainService;

    @Autowired
    private WebTestClient webTestClient;

    private Domain domain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domain createEntity() {
        Domain domain = new Domain()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .state(DEFAULT_STATE)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return domain;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domain createUpdatedEntity() {
        Domain domain = new Domain()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .state(UPDATED_STATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return domain;
    }

    @BeforeEach
    public void initTest() {
        domainRepository.deleteAll().block();
        domain = createEntity();
    }

    @Test
    public void createDomain() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().collectList().block().size();
        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);
        webTestClient.post().uri("/api/domains")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(domainDTO))
            .exchange()
            .expectStatus().isCreated();

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll().collectList().block();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate + 1);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDomain.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testDomain.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testDomain.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDomain.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDomain.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testDomain.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDomain.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    public void createDomainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().collectList().block().size();

        // Create the Domain with an existing ID
        domain.setId("existing_id");
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient.post().uri("/api/domains")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(domainDTO))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll().collectList().block();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().collectList().block().size();
        // set the field null
        domain.setName(null);

        // Create the Domain, which fails.
        DomainDTO domainDTO = domainMapper.toDto(domain);


        webTestClient.post().uri("/api/domains")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(domainDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<Domain> domainList = domainRepository.findAll().collectList().block();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().collectList().block().size();
        // set the field null
        domain.setSlug(null);

        // Create the Domain, which fails.
        DomainDTO domainDTO = domainMapper.toDto(domain);


        webTestClient.post().uri("/api/domains")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(domainDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<Domain> domainList = domainRepository.findAll().collectList().block();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().collectList().block().size();
        // set the field null
        domain.setState(null);

        // Create the Domain, which fails.
        DomainDTO domainDTO = domainMapper.toDto(domain);


        webTestClient.post().uri("/api/domains")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(domainDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<Domain> domainList = domainRepository.findAll().collectList().block();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllDomains() {
        // Initialize the database
        domainRepository.save(domain).block();

        // Get all the domainList
        webTestClient.get().uri("/api/domains?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id").value(hasItem(domain.getId()))
            .jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG))
            .jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE))
            .jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY));
    }
    
    @Test
    public void getDomain() {
        // Initialize the database
        domainRepository.save(domain).block();

        // Get the domain
        webTestClient.get().uri("/api/domains/{id}", domain.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value(is(domain.getId()))
            .jsonPath("$.name").value(is(DEFAULT_NAME))
            .jsonPath("$.slug").value(is(DEFAULT_SLUG))
            .jsonPath("$.state").value(is(DEFAULT_STATE))
            .jsonPath("$.description").value(is(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.createdAt").value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt").value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.createdBy").value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.updatedBy").value(is(DEFAULT_UPDATED_BY));
    }
    @Test
    public void getNonExistingDomain() {
        // Get the domain
        webTestClient.get().uri("/api/domains/{id}", Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void updateDomain() throws Exception {
        // Initialize the database
        domainRepository.save(domain).block();

        int databaseSizeBeforeUpdate = domainRepository.findAll().collectList().block().size();

        // Update the domain
        Domain updatedDomain = domainRepository.findById(domain.getId()).block();
        updatedDomain
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .state(UPDATED_STATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        DomainDTO domainDTO = domainMapper.toDto(updatedDomain);

        webTestClient.put().uri("/api/domains")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(domainDTO))
            .exchange()
            .expectStatus().isOk();

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll().collectList().block();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDomain.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testDomain.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDomain.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDomain.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDomain.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testDomain.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDomain.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    public void updateNonExistingDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().collectList().block().size();

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient.put().uri("/api/domains")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(domainDTO))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll().collectList().block();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteDomain() {
        // Initialize the database
        domainRepository.save(domain).block();

        int databaseSizeBeforeDelete = domainRepository.findAll().collectList().block().size();

        // Delete the domain
        webTestClient.delete().uri("/api/domains/{id}", domain.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();

        // Validate the database contains one less item
        List<Domain> domainList = domainRepository.findAll().collectList().block();
        assertThat(domainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
