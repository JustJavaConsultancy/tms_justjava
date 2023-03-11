package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.ApprovalLine;
import com.simplifysynergy.repository.ApprovalLineRepository;
import com.simplifysynergy.service.dto.ApprovalLineDTO;
import com.simplifysynergy.service.mapper.ApprovalLineMapper;
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
 * Integration tests for the {@link ApprovalLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ApprovalLineResourceIT {

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String ENTITY_API_URL = "/api/approval-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ApprovalLineRepository approvalLineRepository;

    @Autowired
    private ApprovalLineMapper approvalLineMapper;

    @Autowired
    private WebTestClient webTestClient;

    private ApprovalLine approvalLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalLine createEntity() {
        ApprovalLine approvalLine = new ApprovalLine().level(DEFAULT_LEVEL);
        return approvalLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalLine createUpdatedEntity() {
        ApprovalLine approvalLine = new ApprovalLine().level(UPDATED_LEVEL);
        return approvalLine;
    }

    @BeforeEach
    public void initTest() {
        approvalLineRepository.deleteAll().block();
        approvalLine = createEntity();
    }

    @Test
    void createApprovalLine() throws Exception {
        int databaseSizeBeforeCreate = approvalLineRepository.findAll().collectList().block().size();
        // Create the ApprovalLine
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(approvalLine);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalLine testApprovalLine = approvalLineList.get(approvalLineList.size() - 1);
        assertThat(testApprovalLine.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    void createApprovalLineWithExistingId() throws Exception {
        // Create the ApprovalLine with an existing ID
        approvalLine.setId("existing_id");
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(approvalLine);

        int databaseSizeBeforeCreate = approvalLineRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllApprovalLines() {
        // Initialize the database
        approvalLineRepository.save(approvalLine).block();

        // Get all the approvalLineList
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
            .value(hasItem(approvalLine.getId()))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL));
    }

    @Test
    void getApprovalLine() {
        // Initialize the database
        approvalLineRepository.save(approvalLine).block();

        // Get the approvalLine
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, approvalLine.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(approvalLine.getId()))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL));
    }

    @Test
    void getNonExistingApprovalLine() {
        // Get the approvalLine
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingApprovalLine() throws Exception {
        // Initialize the database
        approvalLineRepository.save(approvalLine).block();

        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();

        // Update the approvalLine
        ApprovalLine updatedApprovalLine = approvalLineRepository.findById(approvalLine.getId()).block();
        updatedApprovalLine.level(UPDATED_LEVEL);
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(updatedApprovalLine);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, approvalLineDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
        ApprovalLine testApprovalLine = approvalLineList.get(approvalLineList.size() - 1);
        assertThat(testApprovalLine.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    void putNonExistingApprovalLine() throws Exception {
        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();
        approvalLine.setId(UUID.randomUUID().toString());

        // Create the ApprovalLine
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(approvalLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, approvalLineDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchApprovalLine() throws Exception {
        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();
        approvalLine.setId(UUID.randomUUID().toString());

        // Create the ApprovalLine
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(approvalLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamApprovalLine() throws Exception {
        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();
        approvalLine.setId(UUID.randomUUID().toString());

        // Create the ApprovalLine
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(approvalLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateApprovalLineWithPatch() throws Exception {
        // Initialize the database
        approvalLineRepository.save(approvalLine).block();

        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();

        // Update the approvalLine using partial update
        ApprovalLine partialUpdatedApprovalLine = new ApprovalLine();
        partialUpdatedApprovalLine.setId(approvalLine.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedApprovalLine.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalLine))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
        ApprovalLine testApprovalLine = approvalLineList.get(approvalLineList.size() - 1);
        assertThat(testApprovalLine.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    void fullUpdateApprovalLineWithPatch() throws Exception {
        // Initialize the database
        approvalLineRepository.save(approvalLine).block();

        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();

        // Update the approvalLine using partial update
        ApprovalLine partialUpdatedApprovalLine = new ApprovalLine();
        partialUpdatedApprovalLine.setId(approvalLine.getId());

        partialUpdatedApprovalLine.level(UPDATED_LEVEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedApprovalLine.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalLine))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
        ApprovalLine testApprovalLine = approvalLineList.get(approvalLineList.size() - 1);
        assertThat(testApprovalLine.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    void patchNonExistingApprovalLine() throws Exception {
        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();
        approvalLine.setId(UUID.randomUUID().toString());

        // Create the ApprovalLine
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(approvalLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, approvalLineDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchApprovalLine() throws Exception {
        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();
        approvalLine.setId(UUID.randomUUID().toString());

        // Create the ApprovalLine
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(approvalLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamApprovalLine() throws Exception {
        int databaseSizeBeforeUpdate = approvalLineRepository.findAll().collectList().block().size();
        approvalLine.setId(UUID.randomUUID().toString());

        // Create the ApprovalLine
        ApprovalLineDTO approvalLineDTO = approvalLineMapper.toDto(approvalLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalLineDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ApprovalLine in the database
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteApprovalLine() {
        // Initialize the database
        approvalLineRepository.save(approvalLine).block();

        int databaseSizeBeforeDelete = approvalLineRepository.findAll().collectList().block().size();

        // Delete the approvalLine
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, approvalLine.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ApprovalLine> approvalLineList = approvalLineRepository.findAll().collectList().block();
        assertThat(approvalLineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
