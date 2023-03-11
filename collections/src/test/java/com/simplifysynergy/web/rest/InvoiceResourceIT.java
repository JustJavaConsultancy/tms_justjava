package com.simplifysynergy.web.rest;

import static com.simplifysynergy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.Invoice;
import com.simplifysynergy.domain.enumeration.InvoiceStatus;
import com.simplifysynergy.repository.InvoiceRepository;
import com.simplifysynergy.service.dto.InvoiceDTO;
import com.simplifysynergy.service.mapper.InvoiceMapper;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InvoiceResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_NEXT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final InvoiceStatus DEFAULT_STATUS = InvoiceStatus.NEW;
    private static final InvoiceStatus UPDATED_STATUS = InvoiceStatus.PAID;

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity() {
        Invoice invoice = new Invoice()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .issueDate(DEFAULT_ISSUE_DATE)
            .nextDueDate(DEFAULT_NEXT_DUE_DATE)
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity() {
        Invoice invoice = new Invoice()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .nextDueDate(UPDATED_NEXT_DUE_DATE)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoiceRepository.deleteAll().block();
        invoice = createEntity();
    }

    @Test
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().collectList().block().size();
        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoice.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testInvoice.getNextDueDate()).isEqualTo(DEFAULT_NEXT_DUE_DATE);
        assertThat(testInvoice.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId("existing_id");
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInvoices() {
        // Initialize the database
        invoiceRepository.save(invoice).block();

        // Get all the invoiceList
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
            .value(hasItem(invoice.getId()))
            .jsonPath("$.[*].invoiceNumber")
            .value(hasItem(DEFAULT_INVOICE_NUMBER))
            .jsonPath("$.[*].issueDate")
            .value(hasItem(DEFAULT_ISSUE_DATE.toString()))
            .jsonPath("$.[*].nextDueDate")
            .value(hasItem(DEFAULT_NEXT_DUE_DATE.toString()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()));
    }

    @Test
    void getInvoice() {
        // Initialize the database
        invoiceRepository.save(invoice).block();

        // Get the invoice
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, invoice.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(invoice.getId()))
            .jsonPath("$.invoiceNumber")
            .value(is(DEFAULT_INVOICE_NUMBER))
            .jsonPath("$.issueDate")
            .value(is(DEFAULT_ISSUE_DATE.toString()))
            .jsonPath("$.nextDueDate")
            .value(is(DEFAULT_NEXT_DUE_DATE.toString()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingInvoice() {
        // Get the invoice
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.save(invoice).block();

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).block();
        updatedInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .nextDueDate(UPDATED_NEXT_DUE_DATE)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, invoiceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoice.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testInvoice.getNextDueDate()).isEqualTo(UPDATED_NEXT_DUE_DATE);
        assertThat(testInvoice.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, invoiceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.save(invoice).block();

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice.nextDueDate(UPDATED_NEXT_DUE_DATE).amount(UPDATED_AMOUNT).status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoice.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testInvoice.getNextDueDate()).isEqualTo(UPDATED_NEXT_DUE_DATE);
        assertThat(testInvoice.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.save(invoice).block();

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .nextDueDate(UPDATED_NEXT_DUE_DATE)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoice.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testInvoice.getNextDueDate()).isEqualTo(UPDATED_NEXT_DUE_DATE);
        assertThat(testInvoice.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, invoiceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().collectList().block().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInvoice() {
        // Initialize the database
        invoiceRepository.save(invoice).block();

        int databaseSizeBeforeDelete = invoiceRepository.findAll().collectList().block().size();

        // Delete the invoice
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, invoice.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll().collectList().block();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
