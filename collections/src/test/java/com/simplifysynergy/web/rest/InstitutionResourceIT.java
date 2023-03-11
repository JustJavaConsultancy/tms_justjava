package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.Institution;
import com.simplifysynergy.repository.InstitutionRepository;
import com.simplifysynergy.service.dto.InstitutionDTO;
import com.simplifysynergy.service.mapper.InstitutionMapper;
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
 * Integration tests for the {@link InstitutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InstitutionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/institutions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private InstitutionMapper institutionMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Institution institution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institution createEntity() {
        Institution institution = new Institution().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION);
        return institution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institution createUpdatedEntity() {
        Institution institution = new Institution().code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        return institution;
    }

    @BeforeEach
    public void initTest() {
        institutionRepository.deleteAll().block();
        institution = createEntity();
    }

    @Test
    void createInstitution() throws Exception {
        int databaseSizeBeforeCreate = institutionRepository.findAll().collectList().block().size();
        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeCreate + 1);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInstitution.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createInstitutionWithExistingId() throws Exception {
        // Create the Institution with an existing ID
        institution.setId("existing_id");
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        int databaseSizeBeforeCreate = institutionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().collectList().block().size();
        // set the field null
        institution.setCode(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllInstitutions() {
        // Initialize the database
        institutionRepository.save(institution).block();

        // Get all the institutionList
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
            .value(hasItem(institution.getId()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getInstitution() {
        // Initialize the database
        institutionRepository.save(institution).block();

        // Get the institution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, institution.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(institution.getId()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingInstitution() {
        // Get the institution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInstitution() throws Exception {
        // Initialize the database
        institutionRepository.save(institution).block();

        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();

        // Update the institution
        Institution updatedInstitution = institutionRepository.findById(institution.getId()).block();
        updatedInstitution.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        InstitutionDTO institutionDTO = institutionMapper.toDto(updatedInstitution);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, institutionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInstitution.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();
        institution.setId(UUID.randomUUID().toString());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, institutionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();
        institution.setId(UUID.randomUUID().toString());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();
        institution.setId(UUID.randomUUID().toString());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInstitutionWithPatch() throws Exception {
        // Initialize the database
        institutionRepository.save(institution).block();

        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();

        // Update the institution using partial update
        Institution partialUpdatedInstitution = new Institution();
        partialUpdatedInstitution.setId(institution.getId());

        partialUpdatedInstitution.code(UPDATED_CODE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstitution.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInstitution.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateInstitutionWithPatch() throws Exception {
        // Initialize the database
        institutionRepository.save(institution).block();

        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();

        // Update the institution using partial update
        Institution partialUpdatedInstitution = new Institution();
        partialUpdatedInstitution.setId(institution.getId());

        partialUpdatedInstitution.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstitution.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInstitution.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();
        institution.setId(UUID.randomUUID().toString());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, institutionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();
        institution.setId(UUID.randomUUID().toString());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().collectList().block().size();
        institution.setId(UUID.randomUUID().toString());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institutionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInstitution() {
        // Initialize the database
        institutionRepository.save(institution).block();

        int databaseSizeBeforeDelete = institutionRepository.findAll().collectList().block().size();

        // Delete the institution
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, institution.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Institution> institutionList = institutionRepository.findAll().collectList().block();
        assertThat(institutionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
