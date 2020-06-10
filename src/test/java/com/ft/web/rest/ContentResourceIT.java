package com.ft.web.rest;

import com.ft.CmsGatewayApp;
import com.ft.domain.Content;
import com.ft.repository.ContentRepository;
import com.ft.service.ContentService;
import com.ft.service.dto.ContentDTO;
import com.ft.service.mapper.ContentMapper;

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
 * Integration tests for the {@link ContentResource} REST controller.
 */
@SpringBootTest(classes = CmsGatewayApp.class)
@AutoConfigureWebTestClient
@WithMockUser
public class ContentResourceIT {

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
    private ContentRepository contentRepository;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private ContentService contentService;

    @Autowired
    private WebTestClient webTestClient;

    private Content content;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createEntity() {
        Content content = new Content()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .state(DEFAULT_STATE)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return content;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createUpdatedEntity() {
        Content content = new Content()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .state(UPDATED_STATE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return content;
    }

    @BeforeEach
    public void initTest() {
        contentRepository.deleteAll().block();
        content = createEntity();
    }

    @Test
    public void createContent() throws Exception {
        int databaseSizeBeforeCreate = contentRepository.findAll().collectList().block().size();
        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);
        webTestClient.post().uri("/api/contents")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentDTO))
            .exchange()
            .expectStatus().isCreated();

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll().collectList().block();
        assertThat(contentList).hasSize(databaseSizeBeforeCreate + 1);
        Content testContent = contentList.get(contentList.size() - 1);
        assertThat(testContent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContent.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testContent.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testContent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContent.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testContent.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testContent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testContent.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    public void createContentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contentRepository.findAll().collectList().block().size();

        // Create the Content with an existing ID
        content.setId("existing_id");
        ContentDTO contentDTO = contentMapper.toDto(content);

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient.post().uri("/api/contents")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentDTO))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll().collectList().block();
        assertThat(contentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentRepository.findAll().collectList().block().size();
        // set the field null
        content.setSlug(null);

        // Create the Content, which fails.
        ContentDTO contentDTO = contentMapper.toDto(content);


        webTestClient.post().uri("/api/contents")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<Content> contentList = contentRepository.findAll().collectList().block();
        assertThat(contentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentRepository.findAll().collectList().block().size();
        // set the field null
        content.setState(null);

        // Create the Content, which fails.
        ContentDTO contentDTO = contentMapper.toDto(content);


        webTestClient.post().uri("/api/contents")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentDTO))
            .exchange()
            .expectStatus().isBadRequest();

        List<Content> contentList = contentRepository.findAll().collectList().block();
        assertThat(contentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllContents() {
        // Initialize the database
        contentRepository.save(content).block();

        // Get all the contentList
        webTestClient.get().uri("/api/contents?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id").value(hasItem(content.getId()))
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
    public void getContent() {
        // Initialize the database
        contentRepository.save(content).block();

        // Get the content
        webTestClient.get().uri("/api/contents/{id}", content.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value(is(content.getId()))
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
    public void getNonExistingContent() {
        // Get the content
        webTestClient.get().uri("/api/contents/{id}", Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void updateContent() throws Exception {
        // Initialize the database
        contentRepository.save(content).block();

        int databaseSizeBeforeUpdate = contentRepository.findAll().collectList().block().size();

        // Update the content
        Content updatedContent = contentRepository.findById(content.getId()).block();
        updatedContent
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .state(UPDATED_STATE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        ContentDTO contentDTO = contentMapper.toDto(updatedContent);

        webTestClient.put().uri("/api/contents")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentDTO))
            .exchange()
            .expectStatus().isOk();

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll().collectList().block();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
        Content testContent = contentList.get(contentList.size() - 1);
        assertThat(testContent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContent.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testContent.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testContent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testContent.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testContent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testContent.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    public void updateNonExistingContent() throws Exception {
        int databaseSizeBeforeUpdate = contentRepository.findAll().collectList().block().size();

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient.put().uri("/api/contents")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentDTO))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll().collectList().block();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteContent() {
        // Initialize the database
        contentRepository.save(content).block();

        int databaseSizeBeforeDelete = contentRepository.findAll().collectList().block().size();

        // Delete the content
        webTestClient.delete().uri("/api/contents/{id}", content.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();

        // Validate the database contains one less item
        List<Content> contentList = contentRepository.findAll().collectList().block();
        assertThat(contentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
