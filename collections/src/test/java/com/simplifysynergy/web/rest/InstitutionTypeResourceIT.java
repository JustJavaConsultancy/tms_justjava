package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.InstitutionType;
import com.simplifysynergy.repository.InstitutionTypeRepository;
import com.simplifysynergy.service.dto.InstitutionTypeDTO;
import com.simplifysynergy.service.mapper.InstitutionTypeMapper;
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
 * Integration tests for the {@link InstitutionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InstitutionTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/institution-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InstitutionTypeRepository institutionTypeRepository;

    @Autowired
    private InstitutionTypeMapper institutionTypeMapper;

    @Autowired
    private WebTestClient webTestClient;

    private InstitutionType institutionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionType createEntity() {
        InstitutionType institutionType = new InstitutionType().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION);
        return institutionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionType createUpdatedEntity() {
        InstitutionType institutionType = new InstitutionType().code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        return institutionType;
    }

    @BeforeEach
    public void initTest() {
        institutionTypeRepository.deleteAll().block();
        institutionType = createEntity();
    }

    @Test
    void createInstitutionType() throws Exception {
        int databaseSizeBeforeCreate = institutionTypeRepository.findAll().collectList().block().size();
        // Create the InstitutionType
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InstitutionType testInstitutionType = institutionTypeList.get(institutionTypeList.size() - 1);
        assertThat(testInstitutionType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInstitutionType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createInstitutionTypeWithExistingId() throws Exception {
        // Create the InstitutionType with an existing ID
        institutionType.setId("existing_id");
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);

        int databaseSizeBeforeCreate = institutionTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionTypeRepository.findAll().collectList().block().size();
        // set the field null
        institutionType.setCode(null);

        // Create the InstitutionType, which fails.
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllInstitutionTypes() {
        // Initialize the database
        institutionTypeRepository.save(institutionType).block();

        // Get all the institutionTypeList
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
            .value(hasItem(institutionType.getId()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getInstitutionType() {
        // Initialize the database
        institutionTypeRepository.save(institutionType).block();

        // Get the institutionType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, institutionType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(institutionType.getId()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingInstitutionType() {
        // Get the institutionType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInstitutionType() throws Exception {
        // Initialize the database
        institutionTypeRepository.save(institutionType).block();

        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();

        // Update the institutionType
        InstitutionType updatedInstitutionType = institutionTypeRepository.findById(institutionType.getId()).block();
        updatedInstitutionType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(updatedInstitutionType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, institutionTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionType testInstitutionType = institutionTypeList.get(institutionTypeList.size() - 1);
        assertThat(testInstitutionType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInstitutionType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingInstitutionType() throws Exception {
        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();
        institutionType.setId(UUID.randomUUID().toString());

        // Create the InstitutionType
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, institutionTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInstitutionType() throws Exception {
        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();
        institutionType.setId(UUID.randomUUID().toString());

        // Create the InstitutionType
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInstitutionType() throws Exception {
        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();
        institutionType.setId(UUID.randomUUID().toString());

        // Create the InstitutionType
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInstitutionTypeWithPatch() throws Exception {
        // Initialize the database
        institutionTypeRepository.save(institutionType).block();

        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();

        // Update the institutionType using partial update
        InstitutionType partialUpdatedInstitutionType = new InstitutionType();
        partialUpdatedInstitutionType.setId(institutionType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstitutionType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitutionType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionType testInstitutionType = institutionTypeList.get(institutionTypeList.size() - 1);
        assertThat(testInstitutionType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInstitutionType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateInstitutionTypeWithPatch() throws Exception {
        // Initialize the database
        institutionTypeRepository.save(institutionType).block();

        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();

        // Update the institutionType using partial update
        InstitutionType partialUpdatedInstitutionType = new InstitutionType();
        partialUpdatedInstitutionType.setId(institutionType.getId());

        partialUpdatedInstitutionType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstitutionType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitutionType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionType testInstitutionType = institutionTypeList.get(institutionTypeList.size() - 1);
        assertThat(testInstitutionType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInstitutionType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingInstitutionType() throws Exception {
        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();
        institutionType.setId(UUID.randomUUID().toString());

        // Create the InstitutionType
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, institutionTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInstitutionType() throws Exception {
        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();
        institutionType.setId(UUID.randomUUID().toString());

        // Create the InstitutionType
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInstitutionType() throws Exception {
        int databaseSizeBeforeUpdate = institutionTypeRepository.findAll().collectList().block().size();
        institutionType.setId(UUID.randomUUID().toString());

        // Create the InstitutionType
        InstitutionTypeDTO institutionTypeDTO = institutionTypeMapper.toDto(institutionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InstitutionType in the database
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInstitutionType() {
        // Initialize the database
        institutionTypeRepository.save(institutionType).block();

        int databaseSizeBeforeDelete = institutionTypeRepository.findAll().collectList().block().size();

        // Delete the institutionType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, institutionType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<InstitutionType> institutionTypeList = institutionTypeRepository.findAll().collectList().block();
        assertThat(institutionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
