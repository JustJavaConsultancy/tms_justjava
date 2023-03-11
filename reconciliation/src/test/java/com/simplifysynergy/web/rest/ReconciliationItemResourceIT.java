package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.ReconciliationItem;
import com.simplifysynergy.domain.enumeration.ReconciliationStatus;
import com.simplifysynergy.repository.ReconciliationItemRepository;
import com.simplifysynergy.service.dto.ReconciliationItemDTO;
import com.simplifysynergy.service.mapper.ReconciliationItemMapper;
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
 * Integration tests for the {@link ReconciliationItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReconciliationItemResourceIT {

    private static final ReconciliationStatus DEFAULT_STATUS = ReconciliationStatus.RECONCILED;
    private static final ReconciliationStatus UPDATED_STATUS = ReconciliationStatus.UNRECONCILED;

    private static final String ENTITY_API_URL = "/api/reconciliation-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReconciliationItemRepository reconciliationItemRepository;

    @Autowired
    private ReconciliationItemMapper reconciliationItemMapper;

    @Autowired
    private WebTestClient webTestClient;

    private ReconciliationItem reconciliationItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReconciliationItem createEntity() {
        ReconciliationItem reconciliationItem = new ReconciliationItem().status(DEFAULT_STATUS);
        return reconciliationItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReconciliationItem createUpdatedEntity() {
        ReconciliationItem reconciliationItem = new ReconciliationItem().status(UPDATED_STATUS);
        return reconciliationItem;
    }

    @BeforeEach
    public void initTest() {
        reconciliationItemRepository.deleteAll().block();
        reconciliationItem = createEntity();
    }

    @Test
    void createReconciliationItem() throws Exception {
        int databaseSizeBeforeCreate = reconciliationItemRepository.findAll().collectList().block().size();
        // Create the ReconciliationItem
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(reconciliationItem);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeCreate + 1);
        ReconciliationItem testReconciliationItem = reconciliationItemList.get(reconciliationItemList.size() - 1);
        assertThat(testReconciliationItem.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createReconciliationItemWithExistingId() throws Exception {
        // Create the ReconciliationItem with an existing ID
        reconciliationItem.setId("existing_id");
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(reconciliationItem);

        int databaseSizeBeforeCreate = reconciliationItemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReconciliationItems() {
        // Initialize the database
        reconciliationItemRepository.save(reconciliationItem).block();

        // Get all the reconciliationItemList
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
            .value(hasItem(reconciliationItem.getId()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()));
    }

    @Test
    void getReconciliationItem() {
        // Initialize the database
        reconciliationItemRepository.save(reconciliationItem).block();

        // Get the reconciliationItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reconciliationItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(reconciliationItem.getId()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingReconciliationItem() {
        // Get the reconciliationItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReconciliationItem() throws Exception {
        // Initialize the database
        reconciliationItemRepository.save(reconciliationItem).block();

        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();

        // Update the reconciliationItem
        ReconciliationItem updatedReconciliationItem = reconciliationItemRepository.findById(reconciliationItem.getId()).block();
        updatedReconciliationItem.status(UPDATED_STATUS);
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(updatedReconciliationItem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reconciliationItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
        ReconciliationItem testReconciliationItem = reconciliationItemList.get(reconciliationItemList.size() - 1);
        assertThat(testReconciliationItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingReconciliationItem() throws Exception {
        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();
        reconciliationItem.setId(UUID.randomUUID().toString());

        // Create the ReconciliationItem
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(reconciliationItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reconciliationItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReconciliationItem() throws Exception {
        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();
        reconciliationItem.setId(UUID.randomUUID().toString());

        // Create the ReconciliationItem
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(reconciliationItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReconciliationItem() throws Exception {
        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();
        reconciliationItem.setId(UUID.randomUUID().toString());

        // Create the ReconciliationItem
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(reconciliationItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReconciliationItemWithPatch() throws Exception {
        // Initialize the database
        reconciliationItemRepository.save(reconciliationItem).block();

        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();

        // Update the reconciliationItem using partial update
        ReconciliationItem partialUpdatedReconciliationItem = new ReconciliationItem();
        partialUpdatedReconciliationItem.setId(reconciliationItem.getId());

        partialUpdatedReconciliationItem.status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReconciliationItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReconciliationItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
        ReconciliationItem testReconciliationItem = reconciliationItemList.get(reconciliationItemList.size() - 1);
        assertThat(testReconciliationItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdateReconciliationItemWithPatch() throws Exception {
        // Initialize the database
        reconciliationItemRepository.save(reconciliationItem).block();

        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();

        // Update the reconciliationItem using partial update
        ReconciliationItem partialUpdatedReconciliationItem = new ReconciliationItem();
        partialUpdatedReconciliationItem.setId(reconciliationItem.getId());

        partialUpdatedReconciliationItem.status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReconciliationItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReconciliationItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
        ReconciliationItem testReconciliationItem = reconciliationItemList.get(reconciliationItemList.size() - 1);
        assertThat(testReconciliationItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingReconciliationItem() throws Exception {
        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();
        reconciliationItem.setId(UUID.randomUUID().toString());

        // Create the ReconciliationItem
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(reconciliationItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reconciliationItemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReconciliationItem() throws Exception {
        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();
        reconciliationItem.setId(UUID.randomUUID().toString());

        // Create the ReconciliationItem
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(reconciliationItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReconciliationItem() throws Exception {
        int databaseSizeBeforeUpdate = reconciliationItemRepository.findAll().collectList().block().size();
        reconciliationItem.setId(UUID.randomUUID().toString());

        // Create the ReconciliationItem
        ReconciliationItemDTO reconciliationItemDTO = reconciliationItemMapper.toDto(reconciliationItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reconciliationItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReconciliationItem in the database
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReconciliationItem() {
        // Initialize the database
        reconciliationItemRepository.save(reconciliationItem).block();

        int databaseSizeBeforeDelete = reconciliationItemRepository.findAll().collectList().block().size();

        // Delete the reconciliationItem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reconciliationItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ReconciliationItem> reconciliationItemList = reconciliationItemRepository.findAll().collectList().block();
        assertThat(reconciliationItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
