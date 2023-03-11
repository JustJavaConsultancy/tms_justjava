package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.ServiceType;
import com.simplifysynergy.repository.ServiceTypeRepository;
import com.simplifysynergy.service.dto.ServiceTypeDTO;
import com.simplifysynergy.service.mapper.ServiceTypeMapper;
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
 * Integration tests for the {@link ServiceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ServiceTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ServiceTypeMapper serviceTypeMapper;

    @Autowired
    private WebTestClient webTestClient;

    private ServiceType serviceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceType createEntity() {
        ServiceType serviceType = new ServiceType().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION);
        return serviceType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceType createUpdatedEntity() {
        ServiceType serviceType = new ServiceType().code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        return serviceType;
    }

    @BeforeEach
    public void initTest() {
        serviceTypeRepository.deleteAll().block();
        serviceType = createEntity();
    }

    @Test
    void createServiceType() throws Exception {
        int databaseSizeBeforeCreate = serviceTypeRepository.findAll().collectList().block().size();
        // Create the ServiceType
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(serviceType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceType testServiceType = serviceTypeList.get(serviceTypeList.size() - 1);
        assertThat(testServiceType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testServiceType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createServiceTypeWithExistingId() throws Exception {
        // Create the ServiceType with an existing ID
        serviceType.setId("existing_id");
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(serviceType);

        int databaseSizeBeforeCreate = serviceTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllServiceTypes() {
        // Initialize the database
        serviceTypeRepository.save(serviceType).block();

        // Get all the serviceTypeList
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
            .value(hasItem(serviceType.getId()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getServiceType() {
        // Initialize the database
        serviceTypeRepository.save(serviceType).block();

        // Get the serviceType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, serviceType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(serviceType.getId()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingServiceType() {
        // Get the serviceType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.save(serviceType).block();

        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();

        // Update the serviceType
        ServiceType updatedServiceType = serviceTypeRepository.findById(serviceType.getId()).block();
        updatedServiceType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(updatedServiceType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, serviceTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
        ServiceType testServiceType = serviceTypeList.get(serviceTypeList.size() - 1);
        assertThat(testServiceType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testServiceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingServiceType() throws Exception {
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();
        serviceType.setId(UUID.randomUUID().toString());

        // Create the ServiceType
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(serviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, serviceTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchServiceType() throws Exception {
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();
        serviceType.setId(UUID.randomUUID().toString());

        // Create the ServiceType
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(serviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamServiceType() throws Exception {
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();
        serviceType.setId(UUID.randomUUID().toString());

        // Create the ServiceType
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(serviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateServiceTypeWithPatch() throws Exception {
        // Initialize the database
        serviceTypeRepository.save(serviceType).block();

        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();

        // Update the serviceType using partial update
        ServiceType partialUpdatedServiceType = new ServiceType();
        partialUpdatedServiceType.setId(serviceType.getId());

        partialUpdatedServiceType.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedServiceType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
        ServiceType testServiceType = serviceTypeList.get(serviceTypeList.size() - 1);
        assertThat(testServiceType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testServiceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateServiceTypeWithPatch() throws Exception {
        // Initialize the database
        serviceTypeRepository.save(serviceType).block();

        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();

        // Update the serviceType using partial update
        ServiceType partialUpdatedServiceType = new ServiceType();
        partialUpdatedServiceType.setId(serviceType.getId());

        partialUpdatedServiceType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedServiceType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
        ServiceType testServiceType = serviceTypeList.get(serviceTypeList.size() - 1);
        assertThat(testServiceType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testServiceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingServiceType() throws Exception {
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();
        serviceType.setId(UUID.randomUUID().toString());

        // Create the ServiceType
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(serviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, serviceTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchServiceType() throws Exception {
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();
        serviceType.setId(UUID.randomUUID().toString());

        // Create the ServiceType
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(serviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamServiceType() throws Exception {
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().collectList().block().size();
        serviceType.setId(UUID.randomUUID().toString());

        // Create the ServiceType
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.toDto(serviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteServiceType() {
        // Initialize the database
        serviceTypeRepository.save(serviceType).block();

        int databaseSizeBeforeDelete = serviceTypeRepository.findAll().collectList().block().size();

        // Delete the serviceType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, serviceType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll().collectList().block();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
