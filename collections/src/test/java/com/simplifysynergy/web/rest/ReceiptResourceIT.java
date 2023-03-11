package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.Receipt;
import com.simplifysynergy.repository.ReceiptRepository;
import com.simplifysynergy.service.dto.ReceiptDTO;
import com.simplifysynergy.service.mapper.ReceiptMapper;
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
 * Integration tests for the {@link ReceiptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReceiptResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GENERATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GENERATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_RECEIPT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Receipt receipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createEntity() {
        Receipt receipt = new Receipt()
            .description(DEFAULT_DESCRIPTION)
            .generationDate(DEFAULT_GENERATION_DATE)
            .receiptNumber(DEFAULT_RECEIPT_NUMBER);
        return receipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createUpdatedEntity() {
        Receipt receipt = new Receipt()
            .description(UPDATED_DESCRIPTION)
            .generationDate(UPDATED_GENERATION_DATE)
            .receiptNumber(UPDATED_RECEIPT_NUMBER);
        return receipt;
    }

    @BeforeEach
    public void initTest() {
        receiptRepository.deleteAll().block();
        receipt = createEntity();
    }

    @Test
    void createReceipt() throws Exception {
        int databaseSizeBeforeCreate = receiptRepository.findAll().collectList().block().size();
        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeCreate + 1);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReceipt.getGenerationDate()).isEqualTo(DEFAULT_GENERATION_DATE);
        assertThat(testReceipt.getReceiptNumber()).isEqualTo(DEFAULT_RECEIPT_NUMBER);
    }

    @Test
    void createReceiptWithExistingId() throws Exception {
        // Create the Receipt with an existing ID
        receipt.setId("existing_id");
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        int databaseSizeBeforeCreate = receiptRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkReceiptNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = receiptRepository.findAll().collectList().block().size();
        // set the field null
        receipt.setReceiptNumber(null);

        // Create the Receipt, which fails.
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllReceipts() {
        // Initialize the database
        receiptRepository.save(receipt).block();

        // Get all the receiptList
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
            .value(hasItem(receipt.getId()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].generationDate")
            .value(hasItem(DEFAULT_GENERATION_DATE.toString()))
            .jsonPath("$.[*].receiptNumber")
            .value(hasItem(DEFAULT_RECEIPT_NUMBER));
    }

    @Test
    void getReceipt() {
        // Initialize the database
        receiptRepository.save(receipt).block();

        // Get the receipt
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, receipt.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(receipt.getId()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.generationDate")
            .value(is(DEFAULT_GENERATION_DATE.toString()))
            .jsonPath("$.receiptNumber")
            .value(is(DEFAULT_RECEIPT_NUMBER));
    }

    @Test
    void getNonExistingReceipt() {
        // Get the receipt
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReceipt() throws Exception {
        // Initialize the database
        receiptRepository.save(receipt).block();

        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();

        // Update the receipt
        Receipt updatedReceipt = receiptRepository.findById(receipt.getId()).block();
        updatedReceipt.description(UPDATED_DESCRIPTION).generationDate(UPDATED_GENERATION_DATE).receiptNumber(UPDATED_RECEIPT_NUMBER);
        ReceiptDTO receiptDTO = receiptMapper.toDto(updatedReceipt);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, receiptDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReceipt.getGenerationDate()).isEqualTo(UPDATED_GENERATION_DATE);
        assertThat(testReceipt.getReceiptNumber()).isEqualTo(UPDATED_RECEIPT_NUMBER);
    }

    @Test
    void putNonExistingReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();
        receipt.setId(UUID.randomUUID().toString());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, receiptDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();
        receipt.setId(UUID.randomUUID().toString());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();
        receipt.setId(UUID.randomUUID().toString());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.save(receipt).block();

        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt.description(UPDATED_DESCRIPTION).receiptNumber(UPDATED_RECEIPT_NUMBER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReceipt))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReceipt.getGenerationDate()).isEqualTo(DEFAULT_GENERATION_DATE);
        assertThat(testReceipt.getReceiptNumber()).isEqualTo(UPDATED_RECEIPT_NUMBER);
    }

    @Test
    void fullUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.save(receipt).block();

        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt
            .description(UPDATED_DESCRIPTION)
            .generationDate(UPDATED_GENERATION_DATE)
            .receiptNumber(UPDATED_RECEIPT_NUMBER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReceipt))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReceipt.getGenerationDate()).isEqualTo(UPDATED_GENERATION_DATE);
        assertThat(testReceipt.getReceiptNumber()).isEqualTo(UPDATED_RECEIPT_NUMBER);
    }

    @Test
    void patchNonExistingReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();
        receipt.setId(UUID.randomUUID().toString());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, receiptDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();
        receipt.setId(UUID.randomUUID().toString());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().collectList().block().size();
        receipt.setId(UUID.randomUUID().toString());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReceipt() {
        // Initialize the database
        receiptRepository.save(receipt).block();

        int databaseSizeBeforeDelete = receiptRepository.findAll().collectList().block().size();

        // Delete the receipt
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, receipt.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Receipt> receiptList = receiptRepository.findAll().collectList().block();
        assertThat(receiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
