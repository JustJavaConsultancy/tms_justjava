package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.PaymentBatch;
import com.simplifysynergy.repository.PaymentBatchRepository;
import com.simplifysynergy.service.dto.PaymentBatchDTO;
import com.simplifysynergy.service.mapper.PaymentBatchMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PaymentBatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaymentBatchResourceIT {

    private static final String DEFAULT_BATCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NARRATION = "AAAAAAAAAA";
    private static final String UPDATED_NARRATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/payment-batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PaymentBatchRepository paymentBatchRepository;

    @Autowired
    private PaymentBatchMapper paymentBatchMapper;

    @Autowired
    private WebTestClient webTestClient;

    private PaymentBatch paymentBatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentBatch createEntity() {
        PaymentBatch paymentBatch = new PaymentBatch()
            .batchId(DEFAULT_BATCH_ID)
            .narration(DEFAULT_NARRATION)
            .creationDate(DEFAULT_CREATION_DATE);
        return paymentBatch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentBatch createUpdatedEntity() {
        PaymentBatch paymentBatch = new PaymentBatch()
            .batchId(UPDATED_BATCH_ID)
            .narration(UPDATED_NARRATION)
            .creationDate(UPDATED_CREATION_DATE);
        return paymentBatch;
    }

    @BeforeEach
    public void initTest() {
        paymentBatchRepository.deleteAll().block();
        paymentBatch = createEntity();
    }

    @Test
    void createPaymentBatch() throws Exception {
        int databaseSizeBeforeCreate = paymentBatchRepository.findAll().collectList().block().size();
        // Create the PaymentBatch
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentBatch testPaymentBatch = paymentBatchList.get(paymentBatchList.size() - 1);
        assertThat(testPaymentBatch.getBatchId()).isEqualTo(DEFAULT_BATCH_ID);
        assertThat(testPaymentBatch.getNarration()).isEqualTo(DEFAULT_NARRATION);
        assertThat(testPaymentBatch.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    void createPaymentBatchWithExistingId() throws Exception {
        // Create the PaymentBatch with an existing ID
        paymentBatch.setId("existing_id");
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);

        int databaseSizeBeforeCreate = paymentBatchRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBatchIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentBatchRepository.findAll().collectList().block().size();
        // set the field null
        paymentBatch.setBatchId(null);

        // Create the PaymentBatch, which fails.
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPaymentBatches() {
        // Initialize the database
        paymentBatchRepository.save(paymentBatch).block();

        // Get all the paymentBatchList
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
            .value(hasItem(paymentBatch.getId()))
            .jsonPath("$.[*].batchId")
            .value(hasItem(DEFAULT_BATCH_ID))
            .jsonPath("$.[*].narration")
            .value(hasItem(DEFAULT_NARRATION))
            .jsonPath("$.[*].creationDate")
            .value(hasItem(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    void getPaymentBatch() {
        // Initialize the database
        paymentBatchRepository.save(paymentBatch).block();

        // Get the paymentBatch
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paymentBatch.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paymentBatch.getId()))
            .jsonPath("$.batchId")
            .value(is(DEFAULT_BATCH_ID))
            .jsonPath("$.narration")
            .value(is(DEFAULT_NARRATION))
            .jsonPath("$.creationDate")
            .value(is(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    void getNonExistingPaymentBatch() {
        // Get the paymentBatch
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaymentBatch() throws Exception {
        // Initialize the database
        paymentBatchRepository.save(paymentBatch).block();

        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();

        // Update the paymentBatch
        PaymentBatch updatedPaymentBatch = paymentBatchRepository.findById(paymentBatch.getId()).block();
        updatedPaymentBatch.batchId(UPDATED_BATCH_ID).narration(UPDATED_NARRATION).creationDate(UPDATED_CREATION_DATE);
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(updatedPaymentBatch);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentBatchDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
        PaymentBatch testPaymentBatch = paymentBatchList.get(paymentBatchList.size() - 1);
        assertThat(testPaymentBatch.getBatchId()).isEqualTo(UPDATED_BATCH_ID);
        assertThat(testPaymentBatch.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testPaymentBatch.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    void putNonExistingPaymentBatch() throws Exception {
        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();
        paymentBatch.setId(UUID.randomUUID().toString());

        // Create the PaymentBatch
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentBatchDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaymentBatch() throws Exception {
        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();
        paymentBatch.setId(UUID.randomUUID().toString());

        // Create the PaymentBatch
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaymentBatch() throws Exception {
        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();
        paymentBatch.setId(UUID.randomUUID().toString());

        // Create the PaymentBatch
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePaymentBatchWithPatch() throws Exception {
        // Initialize the database
        paymentBatchRepository.save(paymentBatch).block();

        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();

        // Update the paymentBatch using partial update
        PaymentBatch partialUpdatedPaymentBatch = new PaymentBatch();
        partialUpdatedPaymentBatch.setId(paymentBatch.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentBatch.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentBatch))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
        PaymentBatch testPaymentBatch = paymentBatchList.get(paymentBatchList.size() - 1);
        assertThat(testPaymentBatch.getBatchId()).isEqualTo(DEFAULT_BATCH_ID);
        assertThat(testPaymentBatch.getNarration()).isEqualTo(DEFAULT_NARRATION);
        assertThat(testPaymentBatch.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    void fullUpdatePaymentBatchWithPatch() throws Exception {
        // Initialize the database
        paymentBatchRepository.save(paymentBatch).block();

        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();

        // Update the paymentBatch using partial update
        PaymentBatch partialUpdatedPaymentBatch = new PaymentBatch();
        partialUpdatedPaymentBatch.setId(paymentBatch.getId());

        partialUpdatedPaymentBatch.batchId(UPDATED_BATCH_ID).narration(UPDATED_NARRATION).creationDate(UPDATED_CREATION_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentBatch.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentBatch))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
        PaymentBatch testPaymentBatch = paymentBatchList.get(paymentBatchList.size() - 1);
        assertThat(testPaymentBatch.getBatchId()).isEqualTo(UPDATED_BATCH_ID);
        assertThat(testPaymentBatch.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testPaymentBatch.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    void patchNonExistingPaymentBatch() throws Exception {
        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();
        paymentBatch.setId(UUID.randomUUID().toString());

        // Create the PaymentBatch
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, paymentBatchDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaymentBatch() throws Exception {
        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();
        paymentBatch.setId(UUID.randomUUID().toString());

        // Create the PaymentBatch
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaymentBatch() throws Exception {
        int databaseSizeBeforeUpdate = paymentBatchRepository.findAll().collectList().block().size();
        paymentBatch.setId(UUID.randomUUID().toString());

        // Create the PaymentBatch
        PaymentBatchDTO paymentBatchDTO = paymentBatchMapper.toDto(paymentBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentBatchDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentBatch in the database
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaymentBatch() {
        // Initialize the database
        paymentBatchRepository.save(paymentBatch).block();

        int databaseSizeBeforeDelete = paymentBatchRepository.findAll().collectList().block().size();

        // Delete the paymentBatch
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paymentBatch.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PaymentBatch> paymentBatchList = paymentBatchRepository.findAll().collectList().block();
        assertThat(paymentBatchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
