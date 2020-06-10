package com.ft.web.rest;

import com.ft.CmsGatewayApp;
import com.ft.domain.ContentField;
import com.ft.repository.ContentFieldRepository;
import com.ft.service.ContentFieldService;
import com.ft.service.dto.ContentFieldDTO;
import com.ft.service.mapper.ContentFieldMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the {@link ContentFieldResource} REST controller.
 */
@SpringBootTest(classes = CmsGatewayApp.class)
@AutoConfigureWebTestClient
@WithMockUser
public class ContentFieldResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private ContentFieldRepository contentFieldRepository;

    @Autowired
    private ContentFieldMapper contentFieldMapper;

    @Autowired
    private ContentFieldService contentFieldService;

    @Autowired
    private WebTestClient webTestClient;

    private ContentField contentField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentField createEntity() {
        ContentField contentField = new ContentField()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .state(DEFAULT_STATE)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return contentField;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentField createUpdatedEntity() {
        ContentField contentField = new ContentField()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .state(UPDATED_STATE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return contentField;
    }

    @BeforeEach
    public void initTest() {
        contentFieldRepository.deleteAll().block();
        contentField = createEntity();
    }

    @Test
    public void createContentField() throws Exception {
        int databaseSizeBeforeCreate = contentFieldRepository.findAll().collectList().block().size();
        // Create the ContentField
        ContentFieldDTO contentFieldDTO = contentFieldMapper.toDto(contentField);
        webTestClient.post().uri("/api/content-fields")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentFieldDTO))
            .exchange()
            .expectStatus().isCreated();

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll().collectList().block();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeCreate + 1);
        ContentField testContentField = contentFieldList.get(contentFieldList.size() - 1);
        assertThat(testContentField.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContentField.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testContentField.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testContentField.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContentField.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testContentField.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testContentField.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testContentField.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    public void createContentFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contentFieldRepository.findAll().collectList().block().size();

        // Create the ContentField with an existing ID
        contentField.setId("existing_id");
        ContentFieldDTO contentFieldDTO = contentFieldMapper.toDto(contentField);

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient.post().uri("/api/content-fields")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentFieldDTO))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll().collectList().block();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentFieldRepository.findAll().collectList().block().size();
        // set the field null
        contentField.setSlug(null);

        // Create the ContentField, which fails.
        ContentFieldDTO contentFieldDTO = contentFieldMapper.toDto(contentField);


        webTestClient.post().uri("/api/content-fields")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentFieldDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<ContentField> contentFieldList = contentFieldRepository.findAll().collectList().block();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentFieldRepository.findAll().collectList().block().size();
        // set the field null
        contentField.setState(null);

        // Create the ContentField, which fails.
        ContentFieldDTO contentFieldDTO = contentFieldMapper.toDto(contentField);


        webTestClient.post().uri("/api/content-fields")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentFieldDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<ContentField> contentFieldList = contentFieldRepository.findAll().collectList().block();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllContentFields() {
        // Initialize the database
        contentFieldRepository.save(contentField).block();

        // Get all the contentFieldList
        webTestClient.get().uri("/api/content-fields?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id").value(hasItem(contentField.getId()))
            .jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG))
            .jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE))
            .jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY));
    }
    
    @Test
    public void getContentField() {
        // Initialize the database
        contentFieldRepository.save(contentField).block();

        // Get the contentField
        webTestClient.get().uri("/api/content-fields/{id}", contentField.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value(is(contentField.getId()))
            .jsonPath("$.name").value(is(DEFAULT_NAME))
            .jsonPath("$.slug").value(is(DEFAULT_SLUG))
            .jsonPath("$.state").value(is(DEFAULT_STATE))
            .jsonPath("$.type").value(is(DEFAULT_TYPE))
            .jsonPath("$.createdAt").value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt").value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.createdBy").value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.updatedBy").value(is(DEFAULT_UPDATED_BY));
    }
    @Test
    public void getNonExistingContentField() {
        // Get the contentField
        webTestClient.get().uri("/api/content-fields/{id}", Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void updateContentField() throws Exception {
        // Initialize the database
        contentFieldRepository.save(contentField).block();

        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().collectList().block().size();

        // Update the contentField
        ContentField updatedContentField = contentFieldRepository.findById(contentField.getId()).block();
        updatedContentField
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .state(UPDATED_STATE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        ContentFieldDTO contentFieldDTO = contentFieldMapper.toDto(updatedContentField);

        webTestClient.put().uri("/api/content-fields")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentFieldDTO))
            .exchange()
            .expectStatus().isOk();

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll().collectList().block();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
        ContentField testContentField = contentFieldList.get(contentFieldList.size() - 1);
        assertThat(testContentField.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContentField.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testContentField.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testContentField.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContentField.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testContentField.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testContentField.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testContentField.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    public void updateNonExistingContentField() throws Exception {
        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().collectList().block().size();

        // Create the ContentField
        ContentFieldDTO contentFieldDTO = contentFieldMapper.toDto(contentField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient.put().uri("/api/content-fields")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentFieldDTO))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll().collectList().block();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteContentField() {
        // Initialize the database
        contentFieldRepository.save(contentField).block();

        int databaseSizeBeforeDelete = contentFieldRepository.findAll().collectList().block().size();

        // Delete the contentField
        webTestClient.delete().uri("/api/content-fields/{id}", contentField.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();

        // Validate the database contains one less item
        List<ContentField> contentFieldList = contentFieldRepository.findAll().collectList().block();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
