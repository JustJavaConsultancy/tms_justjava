package com.simplifysynergy.web.rest;

import static com.simplifysynergy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.InvoicePayment;
import com.simplifysynergy.repository.InvoicePaymentRepository;
import com.simplifysynergy.service.dto.InvoicePaymentDTO;
import com.simplifysynergy.service.mapper.InvoicePaymentMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link InvoicePaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InvoicePaymentResourceIT {

    private static final String DEFAULT_NARRATION = "AAAAAAAAAA";
    private static final String UPDATED_NARRATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/invoice-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InvoicePaymentRepository invoicePaymentRepository;

    @Autowired
    private InvoicePaymentMapper invoicePaymentMapper;

    @Autowired
    private WebTestClient webTestClient;

    private InvoicePayment invoicePayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoicePayment createEntity() {
        InvoicePayment invoicePayment = new InvoicePayment()
            .narration(DEFAULT_NARRATION)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .amount(DEFAULT_AMOUNT);
        return invoicePayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoicePayment createUpdatedEntity() {
        InvoicePayment invoicePayment = new InvoicePayment()
            .narration(UPDATED_NARRATION)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT);
        return invoicePayment;
    }

    @BeforeEach
    public void initTest() {
        invoicePaymentRepository.deleteAll().block();
        invoicePayment = createEntity();
    }

    @Test
    void createInvoicePayment() throws Exception {
        int databaseSizeBeforeCreate = invoicePaymentRepository.findAll().collectList().block().size();
        // Create the InvoicePayment
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(invoicePayment);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeCreate + 1);
        InvoicePayment testInvoicePayment = invoicePaymentList.get(invoicePaymentList.size() - 1);
        assertThat(testInvoicePayment.getNarration()).isEqualTo(DEFAULT_NARRATION);
        assertThat(testInvoicePayment.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testInvoicePayment.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void createInvoicePaymentWithExistingId() throws Exception {
        // Create the InvoicePayment with an existing ID
        invoicePayment.setId("existing_id");
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(invoicePayment);

        int databaseSizeBeforeCreate = invoicePaymentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInvoicePayments() {
        // Initialize the database
        invoicePaymentRepository.save(invoicePayment).block();

        // Get all the invoicePaymentList
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
            .value(hasItem(invoicePayment.getId()))
            .jsonPath("$.[*].narration")
            .value(hasItem(DEFAULT_NARRATION))
            .jsonPath("$.[*].paymentDate")
            .value(hasItem(DEFAULT_PAYMENT_DATE.toString()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getInvoicePayment() {
        // Initialize the database
        invoicePaymentRepository.save(invoicePayment).block();

        // Get the invoicePayment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, invoicePayment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(invoicePayment.getId()))
            .jsonPath("$.narration")
            .value(is(DEFAULT_NARRATION))
            .jsonPath("$.paymentDate")
            .value(is(DEFAULT_PAYMENT_DATE.toString()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getNonExistingInvoicePayment() {
        // Get the invoicePayment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInvoicePayment() throws Exception {
        // Initialize the database
        invoicePaymentRepository.save(invoicePayment).block();

        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();

        // Update the invoicePayment
        InvoicePayment updatedInvoicePayment = invoicePaymentRepository.findById(invoicePayment.getId()).block();
        updatedInvoicePayment.narration(UPDATED_NARRATION).paymentDate(UPDATED_PAYMENT_DATE).amount(UPDATED_AMOUNT);
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(updatedInvoicePayment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, invoicePaymentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
        InvoicePayment testInvoicePayment = invoicePaymentList.get(invoicePaymentList.size() - 1);
        assertThat(testInvoicePayment.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testInvoicePayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testInvoicePayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void putNonExistingInvoicePayment() throws Exception {
        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();
        invoicePayment.setId(UUID.randomUUID().toString());

        // Create the InvoicePayment
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(invoicePayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, invoicePaymentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInvoicePayment() throws Exception {
        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();
        invoicePayment.setId(UUID.randomUUID().toString());

        // Create the InvoicePayment
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(invoicePayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInvoicePayment() throws Exception {
        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();
        invoicePayment.setId(UUID.randomUUID().toString());

        // Create the InvoicePayment
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(invoicePayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInvoicePaymentWithPatch() throws Exception {
        // Initialize the database
        invoicePaymentRepository.save(invoicePayment).block();

        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();

        // Update the invoicePayment using partial update
        InvoicePayment partialUpdatedInvoicePayment = new InvoicePayment();
        partialUpdatedInvoicePayment.setId(invoicePayment.getId());

        partialUpdatedInvoicePayment.narration(UPDATED_NARRATION).amount(UPDATED_AMOUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInvoicePayment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoicePayment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
        InvoicePayment testInvoicePayment = invoicePaymentList.get(invoicePaymentList.size() - 1);
        assertThat(testInvoicePayment.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testInvoicePayment.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testInvoicePayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void fullUpdateInvoicePaymentWithPatch() throws Exception {
        // Initialize the database
        invoicePaymentRepository.save(invoicePayment).block();

        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();

        // Update the invoicePayment using partial update
        InvoicePayment partialUpdatedInvoicePayment = new InvoicePayment();
        partialUpdatedInvoicePayment.setId(invoicePayment.getId());

        partialUpdatedInvoicePayment.narration(UPDATED_NARRATION).paymentDate(UPDATED_PAYMENT_DATE).amount(UPDATED_AMOUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInvoicePayment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoicePayment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
        InvoicePayment testInvoicePayment = invoicePaymentList.get(invoicePaymentList.size() - 1);
        assertThat(testInvoicePayment.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testInvoicePayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testInvoicePayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void patchNonExistingInvoicePayment() throws Exception {
        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();
        invoicePayment.setId(UUID.randomUUID().toString());

        // Create the InvoicePayment
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(invoicePayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, invoicePaymentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInvoicePayment() throws Exception {
        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();
        invoicePayment.setId(UUID.randomUUID().toString());

        // Create the InvoicePayment
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(invoicePayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInvoicePayment() throws Exception {
        int databaseSizeBeforeUpdate = invoicePaymentRepository.findAll().collectList().block().size();
        invoicePayment.setId(UUID.randomUUID().toString());

        // Create the InvoicePayment
        InvoicePaymentDTO invoicePaymentDTO = invoicePaymentMapper.toDto(invoicePayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoicePaymentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InvoicePayment in the database
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInvoicePayment() {
        // Initialize the database
        invoicePaymentRepository.save(invoicePayment).block();

        int databaseSizeBeforeDelete = invoicePaymentRepository.findAll().collectList().block().size();

        // Delete the invoicePayment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, invoicePayment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<InvoicePayment> invoicePaymentList = invoicePaymentRepository.findAll().collectList().block();
        assertThat(invoicePaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
