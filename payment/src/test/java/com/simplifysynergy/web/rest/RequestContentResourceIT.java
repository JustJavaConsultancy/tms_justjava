package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.RequestContent;
import com.simplifysynergy.repository.RequestContentRepository;
import com.simplifysynergy.service.dto.RequestContentDTO;
import com.simplifysynergy.service.mapper.RequestContentMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link RequestContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RequestContentResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/request-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestContentRepository requestContentRepository;

    @Autowired
    private RequestContentMapper requestContentMapper;

    @Autowired
    private WebTestClient webTestClient;

    private RequestContent requestContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestContent createEntity() {
        RequestContent requestContent = new RequestContent().content(DEFAULT_CONTENT);
        return requestContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestContent createUpdatedEntity() {
        RequestContent requestContent = new RequestContent().content(UPDATED_CONTENT);
        return requestContent;
    }

    @BeforeEach
    public void initTest() {
        requestContentRepository.deleteAll().block();
        requestContent = createEntity();
    }

    @Test
    void createRequestContent() throws Exception {
        int databaseSizeBeforeCreate = requestContentRepository.findAll().collectList().block().size();
        // Create the RequestContent
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(requestContent);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeCreate + 1);
        RequestContent testRequestContent = requestContentList.get(requestContentList.size() - 1);
        assertThat(testRequestContent.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    void createRequestContentWithExistingId() throws Exception {
        // Create the RequestContent with an existing ID
        requestContent.setId("existing_id");
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(requestContent);

        int databaseSizeBeforeCreate = requestContentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRequestContents() {
        // Initialize the database
        requestContentRepository.save(requestContent).block();

        // Get all the requestContentList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(requestContent.getId()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT));
    }

    @Test
    void getRequestContent() {
        // Initialize the database
        requestContentRepository.save(requestContent).block();

        // Get the requestContent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, requestContent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(requestContent.getId()))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT));
    }

    @Test
    void getNonExistingRequestContent() {
        // Get the requestContent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRequestContent() throws Exception {
        // Initialize the database
        requestContentRepository.save(requestContent).block();

        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();

        // Update the requestContent
        RequestContent updatedRequestContent = requestContentRepository.findById(requestContent.getId()).block();
        updatedRequestContent.content(UPDATED_CONTENT);
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(updatedRequestContent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestContentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
        RequestContent testRequestContent = requestContentList.get(requestContentList.size() - 1);
        assertThat(testRequestContent.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    void putNonExistingRequestContent() throws Exception {
        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();
        requestContent.setId(UUID.randomUUID().toString());

        // Create the RequestContent
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(requestContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestContentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequestContent() throws Exception {
        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();
        requestContent.setId(UUID.randomUUID().toString());

        // Create the RequestContent
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(requestContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequestContent() throws Exception {
        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();
        requestContent.setId(UUID.randomUUID().toString());

        // Create the RequestContent
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(requestContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestContentWithPatch() throws Exception {
        // Initialize the database
        requestContentRepository.save(requestContent).block();

        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();

        // Update the requestContent using partial update
        RequestContent partialUpdatedRequestContent = new RequestContent();
        partialUpdatedRequestContent.setId(requestContent.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequestContent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestContent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
        RequestContent testRequestContent = requestContentList.get(requestContentList.size() - 1);
        assertThat(testRequestContent.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    void fullUpdateRequestContentWithPatch() throws Exception {
        // Initialize the database
        requestContentRepository.save(requestContent).block();

        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();

        // Update the requestContent using partial update
        RequestContent partialUpdatedRequestContent = new RequestContent();
        partialUpdatedRequestContent.setId(requestContent.getId());

        partialUpdatedRequestContent.content(UPDATED_CONTENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequestContent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestContent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
        RequestContent testRequestContent = requestContentList.get(requestContentList.size() - 1);
        assertThat(testRequestContent.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    void patchNonExistingRequestContent() throws Exception {
        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();
        requestContent.setId(UUID.randomUUID().toString());

        // Create the RequestContent
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(requestContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, requestContentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequestContent() throws Exception {
        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();
        requestContent.setId(UUID.randomUUID().toString());

        // Create the RequestContent
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(requestContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequestContent() throws Exception {
        int databaseSizeBeforeUpdate = requestContentRepository.findAll().collectList().block().size();
        requestContent.setId(UUID.randomUUID().toString());

        // Create the RequestContent
        RequestContentDTO requestContentDTO = requestContentMapper.toDto(requestContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestContentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RequestContent in the database
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequestContent() {
        // Initialize the database
        requestContentRepository.save(requestContent).block();

        int databaseSizeBeforeDelete = requestContentRepository.findAll().collectList().block().size();

        // Delete the requestContent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, requestContent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RequestContent> requestContentList = requestContentRepository.findAll().collectList().block();
        assertThat(requestContentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
