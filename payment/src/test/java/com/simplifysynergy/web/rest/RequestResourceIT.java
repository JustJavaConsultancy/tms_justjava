package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.Request;
import com.simplifysynergy.repository.RequestRepository;
import com.simplifysynergy.service.dto.RequestDTO;
import com.simplifysynergy.service.mapper.RequestMapper;
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
 * Integration tests for the {@link RequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RequestResourceIT {

    private static final Integer DEFAULT_CURRENT_LEVEL = 1;
    private static final Integer UPDATED_CURRENT_LEVEL = 2;

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Request request;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createEntity() {
        Request request = new Request().currentLevel(DEFAULT_CURRENT_LEVEL);
        return request;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createUpdatedEntity() {
        Request request = new Request().currentLevel(UPDATED_CURRENT_LEVEL);
        return request;
    }

    @BeforeEach
    public void initTest() {
        requestRepository.deleteAll().block();
        request = createEntity();
    }

    @Test
    void createRequest() throws Exception {
        int databaseSizeBeforeCreate = requestRepository.findAll().collectList().block().size();
        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate + 1);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getCurrentLevel()).isEqualTo(DEFAULT_CURRENT_LEVEL);
    }

    @Test
    void createRequestWithExistingId() throws Exception {
        // Create the Request with an existing ID
        request.setId("existing_id");
        RequestDTO requestDTO = requestMapper.toDto(request);

        int databaseSizeBeforeCreate = requestRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCurrentLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().collectList().block().size();
        // set the field null
        request.setCurrentLevel(null);

        // Create the Request, which fails.
        RequestDTO requestDTO = requestMapper.toDto(request);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRequests() {
        // Initialize the database
        requestRepository.save(request).block();

        // Get all the requestList
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
            .value(hasItem(request.getId()))
            .jsonPath("$.[*].currentLevel")
            .value(hasItem(DEFAULT_CURRENT_LEVEL));
    }

    @Test
    void getRequest() {
        // Initialize the database
        requestRepository.save(request).block();

        // Get the request
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, request.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(request.getId()))
            .jsonPath("$.currentLevel")
            .value(is(DEFAULT_CURRENT_LEVEL));
    }

    @Test
    void getNonExistingRequest() {
        // Get the request
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRequest() throws Exception {
        // Initialize the database
        requestRepository.save(request).block();

        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();

        // Update the request
        Request updatedRequest = requestRepository.findById(request.getId()).block();
        updatedRequest.currentLevel(UPDATED_CURRENT_LEVEL);
        RequestDTO requestDTO = requestMapper.toDto(updatedRequest);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getCurrentLevel()).isEqualTo(UPDATED_CURRENT_LEVEL);
    }

    @Test
    void putNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.save(request).block();

        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest.currentLevel(UPDATED_CURRENT_LEVEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getCurrentLevel()).isEqualTo(UPDATED_CURRENT_LEVEL);
    }

    @Test
    void fullUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.save(request).block();

        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest.currentLevel(UPDATED_CURRENT_LEVEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getCurrentLevel()).isEqualTo(UPDATED_CURRENT_LEVEL);
    }

    @Test
    void patchNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, requestDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().collectList().block().size();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequest() {
        // Initialize the database
        requestRepository.save(request).block();

        int databaseSizeBeforeDelete = requestRepository.findAll().collectList().block().size();

        // Delete the request
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, request.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Request> requestList = requestRepository.findAll().collectList().block();
        assertThat(requestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
