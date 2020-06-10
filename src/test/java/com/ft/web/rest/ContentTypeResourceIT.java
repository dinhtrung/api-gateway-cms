package com.ft.web.rest;

import com.ft.CmsGatewayApp;
import com.ft.domain.ContentType;
import com.ft.repository.ContentTypeRepository;
import com.ft.service.ContentTypeService;
import com.ft.service.dto.ContentTypeDTO;
import com.ft.service.mapper.ContentTypeMapper;

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
 * Integration tests for the {@link ContentTypeResource} REST controller.
 */
@SpringBootTest(classes = CmsGatewayApp.class)
@AutoConfigureWebTestClient
@WithMockUser
public class ContentTypeResourceIT {

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
    private ContentTypeRepository contentTypeRepository;

    @Autowired
    private ContentTypeMapper contentTypeMapper;

    @Autowired
    private ContentTypeService contentTypeService;

    @Autowired
    private WebTestClient webTestClient;

    private ContentType contentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentType createEntity() {
        ContentType contentType = new ContentType()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .state(DEFAULT_STATE)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return contentType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentType createUpdatedEntity() {
        ContentType contentType = new ContentType()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .state(UPDATED_STATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return contentType;
    }

    @BeforeEach
    public void initTest() {
        contentTypeRepository.deleteAll().block();
        contentType = createEntity();
    }

    @Test
    public void createContentType() throws Exception {
        int databaseSizeBeforeCreate = contentTypeRepository.findAll().collectList().block().size();
        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);
        webTestClient.post().uri("/api/content-types")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            .exchange()
            .expectStatus().isCreated();

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll().collectList().block();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ContentType testContentType = contentTypeList.get(contentTypeList.size() - 1);
        assertThat(testContentType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContentType.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testContentType.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testContentType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContentType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testContentType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testContentType.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testContentType.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    public void createContentTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contentTypeRepository.findAll().collectList().block().size();

        // Create the ContentType with an existing ID
        contentType.setId("existing_id");
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient.post().uri("/api/content-types")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll().collectList().block();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentTypeRepository.findAll().collectList().block().size();
        // set the field null
        contentType.setSlug(null);

        // Create the ContentType, which fails.
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);


        webTestClient.post().uri("/api/content-types")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<ContentType> contentTypeList = contentTypeRepository.findAll().collectList().block();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentTypeRepository.findAll().collectList().block().size();
        // set the field null
        contentType.setState(null);

        // Create the ContentType, which fails.
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);


        webTestClient.post().uri("/api/content-types")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<ContentType> contentTypeList = contentTypeRepository.findAll().collectList().block();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllContentTypes() {
        // Initialize the database
        contentTypeRepository.save(contentType).block();

        // Get all the contentTypeList
        webTestClient.get().uri("/api/content-types?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id").value(hasItem(contentType.getId()))
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
    public void getContentType() {
        // Initialize the database
        contentTypeRepository.save(contentType).block();

        // Get the contentType
        webTestClient.get().uri("/api/content-types/{id}", contentType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value(is(contentType.getId()))
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
    public void getNonExistingContentType() {
        // Get the contentType
        webTestClient.get().uri("/api/content-types/{id}", Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void updateContentType() throws Exception {
        // Initialize the database
        contentTypeRepository.save(contentType).block();

        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().collectList().block().size();

        // Update the contentType
        ContentType updatedContentType = contentTypeRepository.findById(contentType.getId()).block();
        updatedContentType
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .state(UPDATED_STATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(updatedContentType);

        webTestClient.put().uri("/api/content-types")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            .exchange()
            .expectStatus().isOk();

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll().collectList().block();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
        ContentType testContentType = contentTypeList.get(contentTypeList.size() - 1);
        assertThat(testContentType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContentType.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testContentType.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testContentType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContentType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testContentType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testContentType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testContentType.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    public void updateNonExistingContentType() throws Exception {
        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().collectList().block().size();

        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient.put().uri("/api/content-types")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll().collectList().block();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteContentType() {
        // Initialize the database
        contentTypeRepository.save(contentType).block();

        int databaseSizeBeforeDelete = contentTypeRepository.findAll().collectList().block().size();

        // Delete the contentType
        webTestClient.delete().uri("/api/content-types/{id}", contentType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();

        // Validate the database contains one less item
        List<ContentType> contentTypeList = contentTypeRepository.findAll().collectList().block();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
