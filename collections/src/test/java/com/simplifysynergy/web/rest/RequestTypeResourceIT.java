package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.RequestType;
import com.simplifysynergy.repository.RequestTypeRepository;
import com.simplifysynergy.service.dto.RequestTypeDTO;
import com.simplifysynergy.service.mapper.RequestTypeMapper;
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
 * Integration tests for the {@link RequestTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RequestTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/request-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestTypeRepository requestTypeRepository;

    @Autowired
    private RequestTypeMapper requestTypeMapper;

    @Autowired
    private WebTestClient webTestClient;

    private RequestType requestType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestType createEntity() {
        RequestType requestType = new RequestType().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return requestType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestType createUpdatedEntity() {
        RequestType requestType = new RequestType().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return requestType;
    }

    @BeforeEach
    public void initTest() {
        requestTypeRepository.deleteAll().block();
        requestType = createEntity();
    }

    @Test
    void createRequestType() throws Exception {
        int databaseSizeBeforeCreate = requestTypeRepository.findAll().collectList().block().size();
        // Create the RequestType
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequestType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequestType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    void createRequestTypeWithExistingId() throws Exception {
        // Create the RequestType with an existing ID
        requestType.setId("existing_id");
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        int databaseSizeBeforeCreate = requestTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestTypeRepository.findAll().collectList().block().size();
        // set the field null
        requestType.setType(null);

        // Create the RequestType, which fails.
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestTypeRepository.findAll().collectList().block().size();
        // set the field null
        requestType.setCode(null);

        // Create the RequestType, which fails.
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRequestTypes() {
        // Initialize the database
        requestTypeRepository.save(requestType).block();

        // Get all the requestTypeList
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
            .value(hasItem(requestType.getId()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE));
    }

    @Test
    void getRequestType() {
        // Initialize the database
        requestTypeRepository.save(requestType).block();

        // Get the requestType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, requestType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(requestType.getId()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE));
    }

    @Test
    void getNonExistingRequestType() {
        // Get the requestType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRequestType() throws Exception {
        // Initialize the database
        requestTypeRepository.save(requestType).block();

        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();

        // Update the requestType
        RequestType updatedRequestType = requestTypeRepository.findById(requestType.getId()).block();
        updatedRequestType.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(updatedRequestType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequestType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequestType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    void putNonExistingRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();
        requestType.setId(UUID.randomUUID().toString());

        // Create the RequestType
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();
        requestType.setId(UUID.randomUUID().toString());

        // Create the RequestType
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();
        requestType.setId(UUID.randomUUID().toString());

        // Create the RequestType
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestTypeWithPatch() throws Exception {
        // Initialize the database
        requestTypeRepository.save(requestType).block();

        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();

        // Update the requestType using partial update
        RequestType partialUpdatedRequestType = new RequestType();
        partialUpdatedRequestType.setId(requestType.getId());

        partialUpdatedRequestType.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequestType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequestType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequestType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    void fullUpdateRequestTypeWithPatch() throws Exception {
        // Initialize the database
        requestTypeRepository.save(requestType).block();

        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();

        // Update the requestType using partial update
        RequestType partialUpdatedRequestType = new RequestType();
        partialUpdatedRequestType.setId(requestType.getId());

        partialUpdatedRequestType.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequestType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequestType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequestType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    void patchNonExistingRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();
        requestType.setId(UUID.randomUUID().toString());

        // Create the RequestType
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, requestTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();
        requestType.setId(UUID.randomUUID().toString());

        // Create the RequestType
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().collectList().block().size();
        requestType.setId(UUID.randomUUID().toString());

        // Create the RequestType
        RequestTypeDTO requestTypeDTO = requestTypeMapper.toDto(requestType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequestType() {
        // Initialize the database
        requestTypeRepository.save(requestType).block();

        int databaseSizeBeforeDelete = requestTypeRepository.findAll().collectList().block().size();

        // Delete the requestType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, requestType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RequestType> requestTypeList = requestTypeRepository.findAll().collectList().block();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
