package com.simplifysynergy.web.rest;

import static com.simplifysynergy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.PaymentInstruction;
import com.simplifysynergy.domain.enumeration.PaymentStatus;
import com.simplifysynergy.repository.PaymentInstructionRepository;
import com.simplifysynergy.service.dto.PaymentInstructionDTO;
import com.simplifysynergy.service.mapper.PaymentInstructionMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentInstructionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaymentInstructionResourceIT {

    private static final String DEFAULT_SOURCE_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_NARRATION = "AAAAAAAAAA";
    private static final String UPDATED_NARRATION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_SOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_BANK_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_BANK_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_BANK_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_BANK_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.NEW;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.PAID;

    private static final String ENTITY_API_URL = "/api/payment-instructions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PaymentInstructionRepository paymentInstructionRepository;

    @Autowired
    private PaymentInstructionMapper paymentInstructionMapper;

    @Autowired
    private WebTestClient webTestClient;

    private PaymentInstruction paymentInstruction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentInstruction createEntity() {
        PaymentInstruction paymentInstruction = new PaymentInstruction()
            .sourceAccount(DEFAULT_SOURCE_ACCOUNT)
            .destinationAccount(DEFAULT_DESTINATION_ACCOUNT)
            .narration(DEFAULT_NARRATION)
            .amount(DEFAULT_AMOUNT)
            .sourceName(DEFAULT_SOURCE_NAME)
            .destinationName(DEFAULT_DESTINATION_NAME)
            .sourceBankAccountCode(DEFAULT_SOURCE_BANK_ACCOUNT_CODE)
            .destinationBankAccountCode(DEFAULT_DESTINATION_BANK_ACCOUNT_CODE)
            .paymentStatus(DEFAULT_PAYMENT_STATUS);
        return paymentInstruction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentInstruction createUpdatedEntity() {
        PaymentInstruction paymentInstruction = new PaymentInstruction()
            .sourceAccount(UPDATED_SOURCE_ACCOUNT)
            .destinationAccount(UPDATED_DESTINATION_ACCOUNT)
            .narration(UPDATED_NARRATION)
            .amount(UPDATED_AMOUNT)
            .sourceName(UPDATED_SOURCE_NAME)
            .destinationName(UPDATED_DESTINATION_NAME)
            .sourceBankAccountCode(UPDATED_SOURCE_BANK_ACCOUNT_CODE)
            .destinationBankAccountCode(UPDATED_DESTINATION_BANK_ACCOUNT_CODE)
            .paymentStatus(UPDATED_PAYMENT_STATUS);
        return paymentInstruction;
    }

    @BeforeEach
    public void initTest() {
        paymentInstructionRepository.deleteAll().block();
        paymentInstruction = createEntity();
    }

    @Test
    void createPaymentInstruction() throws Exception {
        int databaseSizeBeforeCreate = paymentInstructionRepository.findAll().collectList().block().size();
        // Create the PaymentInstruction
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentInstruction testPaymentInstruction = paymentInstructionList.get(paymentInstructionList.size() - 1);
        assertThat(testPaymentInstruction.getSourceAccount()).isEqualTo(DEFAULT_SOURCE_ACCOUNT);
        assertThat(testPaymentInstruction.getDestinationAccount()).isEqualTo(DEFAULT_DESTINATION_ACCOUNT);
        assertThat(testPaymentInstruction.getNarration()).isEqualTo(DEFAULT_NARRATION);
        assertThat(testPaymentInstruction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPaymentInstruction.getSourceName()).isEqualTo(DEFAULT_SOURCE_NAME);
        assertThat(testPaymentInstruction.getDestinationName()).isEqualTo(DEFAULT_DESTINATION_NAME);
        assertThat(testPaymentInstruction.getSourceBankAccountCode()).isEqualTo(DEFAULT_SOURCE_BANK_ACCOUNT_CODE);
        assertThat(testPaymentInstruction.getDestinationBankAccountCode()).isEqualTo(DEFAULT_DESTINATION_BANK_ACCOUNT_CODE);
        assertThat(testPaymentInstruction.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
    }

    @Test
    void createPaymentInstructionWithExistingId() throws Exception {
        // Create the PaymentInstruction with an existing ID
        paymentInstruction.setId("existing_id");
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        int databaseSizeBeforeCreate = paymentInstructionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkSourceAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentInstructionRepository.findAll().collectList().block().size();
        // set the field null
        paymentInstruction.setSourceAccount(null);

        // Create the PaymentInstruction, which fails.
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDestinationAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentInstructionRepository.findAll().collectList().block().size();
        // set the field null
        paymentInstruction.setDestinationAccount(null);

        // Create the PaymentInstruction, which fails.
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentInstructionRepository.findAll().collectList().block().size();
        // set the field null
        paymentInstruction.setAmount(null);

        // Create the PaymentInstruction, which fails.
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSourceBankAccountCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentInstructionRepository.findAll().collectList().block().size();
        // set the field null
        paymentInstruction.setSourceBankAccountCode(null);

        // Create the PaymentInstruction, which fails.
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDestinationBankAccountCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentInstructionRepository.findAll().collectList().block().size();
        // set the field null
        paymentInstruction.setDestinationBankAccountCode(null);

        // Create the PaymentInstruction, which fails.
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPaymentInstructions() {
        // Initialize the database
        paymentInstructionRepository.save(paymentInstruction).block();

        // Get all the paymentInstructionList
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
            .value(hasItem(paymentInstruction.getId()))
            .jsonPath("$.[*].sourceAccount")
            .value(hasItem(DEFAULT_SOURCE_ACCOUNT))
            .jsonPath("$.[*].destinationAccount")
            .value(hasItem(DEFAULT_DESTINATION_ACCOUNT))
            .jsonPath("$.[*].narration")
            .value(hasItem(DEFAULT_NARRATION))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].sourceName")
            .value(hasItem(DEFAULT_SOURCE_NAME))
            .jsonPath("$.[*].destinationName")
            .value(hasItem(DEFAULT_DESTINATION_NAME))
            .jsonPath("$.[*].sourceBankAccountCode")
            .value(hasItem(DEFAULT_SOURCE_BANK_ACCOUNT_CODE))
            .jsonPath("$.[*].destinationBankAccountCode")
            .value(hasItem(DEFAULT_DESTINATION_BANK_ACCOUNT_CODE))
            .jsonPath("$.[*].paymentStatus")
            .value(hasItem(DEFAULT_PAYMENT_STATUS.toString()));
    }

    @Test
    void getPaymentInstruction() {
        // Initialize the database
        paymentInstructionRepository.save(paymentInstruction).block();

        // Get the paymentInstruction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paymentInstruction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paymentInstruction.getId()))
            .jsonPath("$.sourceAccount")
            .value(is(DEFAULT_SOURCE_ACCOUNT))
            .jsonPath("$.destinationAccount")
            .value(is(DEFAULT_DESTINATION_ACCOUNT))
            .jsonPath("$.narration")
            .value(is(DEFAULT_NARRATION))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.sourceName")
            .value(is(DEFAULT_SOURCE_NAME))
            .jsonPath("$.destinationName")
            .value(is(DEFAULT_DESTINATION_NAME))
            .jsonPath("$.sourceBankAccountCode")
            .value(is(DEFAULT_SOURCE_BANK_ACCOUNT_CODE))
            .jsonPath("$.destinationBankAccountCode")
            .value(is(DEFAULT_DESTINATION_BANK_ACCOUNT_CODE))
            .jsonPath("$.paymentStatus")
            .value(is(DEFAULT_PAYMENT_STATUS.toString()));
    }

    @Test
    void getNonExistingPaymentInstruction() {
        // Get the paymentInstruction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaymentInstruction() throws Exception {
        // Initialize the database
        paymentInstructionRepository.save(paymentInstruction).block();

        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();

        // Update the paymentInstruction
        PaymentInstruction updatedPaymentInstruction = paymentInstructionRepository.findById(paymentInstruction.getId()).block();
        updatedPaymentInstruction
            .sourceAccount(UPDATED_SOURCE_ACCOUNT)
            .destinationAccount(UPDATED_DESTINATION_ACCOUNT)
            .narration(UPDATED_NARRATION)
            .amount(UPDATED_AMOUNT)
            .sourceName(UPDATED_SOURCE_NAME)
            .destinationName(UPDATED_DESTINATION_NAME)
            .sourceBankAccountCode(UPDATED_SOURCE_BANK_ACCOUNT_CODE)
            .destinationBankAccountCode(UPDATED_DESTINATION_BANK_ACCOUNT_CODE)
            .paymentStatus(UPDATED_PAYMENT_STATUS);
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(updatedPaymentInstruction);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentInstructionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
        PaymentInstruction testPaymentInstruction = paymentInstructionList.get(paymentInstructionList.size() - 1);
        assertThat(testPaymentInstruction.getSourceAccount()).isEqualTo(UPDATED_SOURCE_ACCOUNT);
        assertThat(testPaymentInstruction.getDestinationAccount()).isEqualTo(UPDATED_DESTINATION_ACCOUNT);
        assertThat(testPaymentInstruction.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testPaymentInstruction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentInstruction.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testPaymentInstruction.getDestinationName()).isEqualTo(UPDATED_DESTINATION_NAME);
        assertThat(testPaymentInstruction.getSourceBankAccountCode()).isEqualTo(UPDATED_SOURCE_BANK_ACCOUNT_CODE);
        assertThat(testPaymentInstruction.getDestinationBankAccountCode()).isEqualTo(UPDATED_DESTINATION_BANK_ACCOUNT_CODE);
        assertThat(testPaymentInstruction.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
    }

    @Test
    void putNonExistingPaymentInstruction() throws Exception {
        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();
        paymentInstruction.setId(UUID.randomUUID().toString());

        // Create the PaymentInstruction
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentInstructionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaymentInstruction() throws Exception {
        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();
        paymentInstruction.setId(UUID.randomUUID().toString());

        // Create the PaymentInstruction
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaymentInstruction() throws Exception {
        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();
        paymentInstruction.setId(UUID.randomUUID().toString());

        // Create the PaymentInstruction
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePaymentInstructionWithPatch() throws Exception {
        // Initialize the database
        paymentInstructionRepository.save(paymentInstruction).block();

        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();

        // Update the paymentInstruction using partial update
        PaymentInstruction partialUpdatedPaymentInstruction = new PaymentInstruction();
        partialUpdatedPaymentInstruction.setId(paymentInstruction.getId());

        partialUpdatedPaymentInstruction
            .destinationAccount(UPDATED_DESTINATION_ACCOUNT)
            .amount(UPDATED_AMOUNT)
            .sourceName(UPDATED_SOURCE_NAME)
            .destinationName(UPDATED_DESTINATION_NAME)
            .sourceBankAccountCode(UPDATED_SOURCE_BANK_ACCOUNT_CODE)
            .destinationBankAccountCode(UPDATED_DESTINATION_BANK_ACCOUNT_CODE)
            .paymentStatus(UPDATED_PAYMENT_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentInstruction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentInstruction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
        PaymentInstruction testPaymentInstruction = paymentInstructionList.get(paymentInstructionList.size() - 1);
        assertThat(testPaymentInstruction.getSourceAccount()).isEqualTo(DEFAULT_SOURCE_ACCOUNT);
        assertThat(testPaymentInstruction.getDestinationAccount()).isEqualTo(UPDATED_DESTINATION_ACCOUNT);
        assertThat(testPaymentInstruction.getNarration()).isEqualTo(DEFAULT_NARRATION);
        assertThat(testPaymentInstruction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentInstruction.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testPaymentInstruction.getDestinationName()).isEqualTo(UPDATED_DESTINATION_NAME);
        assertThat(testPaymentInstruction.getSourceBankAccountCode()).isEqualTo(UPDATED_SOURCE_BANK_ACCOUNT_CODE);
        assertThat(testPaymentInstruction.getDestinationBankAccountCode()).isEqualTo(UPDATED_DESTINATION_BANK_ACCOUNT_CODE);
        assertThat(testPaymentInstruction.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
    }

    @Test
    void fullUpdatePaymentInstructionWithPatch() throws Exception {
        // Initialize the database
        paymentInstructionRepository.save(paymentInstruction).block();

        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();

        // Update the paymentInstruction using partial update
        PaymentInstruction partialUpdatedPaymentInstruction = new PaymentInstruction();
        partialUpdatedPaymentInstruction.setId(paymentInstruction.getId());

        partialUpdatedPaymentInstruction
            .sourceAccount(UPDATED_SOURCE_ACCOUNT)
            .destinationAccount(UPDATED_DESTINATION_ACCOUNT)
            .narration(UPDATED_NARRATION)
            .amount(UPDATED_AMOUNT)
            .sourceName(UPDATED_SOURCE_NAME)
            .destinationName(UPDATED_DESTINATION_NAME)
            .sourceBankAccountCode(UPDATED_SOURCE_BANK_ACCOUNT_CODE)
            .destinationBankAccountCode(UPDATED_DESTINATION_BANK_ACCOUNT_CODE)
            .paymentStatus(UPDATED_PAYMENT_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentInstruction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentInstruction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
        PaymentInstruction testPaymentInstruction = paymentInstructionList.get(paymentInstructionList.size() - 1);
        assertThat(testPaymentInstruction.getSourceAccount()).isEqualTo(UPDATED_SOURCE_ACCOUNT);
        assertThat(testPaymentInstruction.getDestinationAccount()).isEqualTo(UPDATED_DESTINATION_ACCOUNT);
        assertThat(testPaymentInstruction.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testPaymentInstruction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentInstruction.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testPaymentInstruction.getDestinationName()).isEqualTo(UPDATED_DESTINATION_NAME);
        assertThat(testPaymentInstruction.getSourceBankAccountCode()).isEqualTo(UPDATED_SOURCE_BANK_ACCOUNT_CODE);
        assertThat(testPaymentInstruction.getDestinationBankAccountCode()).isEqualTo(UPDATED_DESTINATION_BANK_ACCOUNT_CODE);
        assertThat(testPaymentInstruction.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
    }

    @Test
    void patchNonExistingPaymentInstruction() throws Exception {
        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();
        paymentInstruction.setId(UUID.randomUUID().toString());

        // Create the PaymentInstruction
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, paymentInstructionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaymentInstruction() throws Exception {
        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();
        paymentInstruction.setId(UUID.randomUUID().toString());

        // Create the PaymentInstruction
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaymentInstruction() throws Exception {
        int databaseSizeBeforeUpdate = paymentInstructionRepository.findAll().collectList().block().size();
        paymentInstruction.setId(UUID.randomUUID().toString());

        // Create the PaymentInstruction
        PaymentInstructionDTO paymentInstructionDTO = paymentInstructionMapper.toDto(paymentInstruction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentInstructionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentInstruction in the database
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaymentInstruction() {
        // Initialize the database
        paymentInstructionRepository.save(paymentInstruction).block();

        int databaseSizeBeforeDelete = paymentInstructionRepository.findAll().collectList().block().size();

        // Delete the paymentInstruction
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paymentInstruction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PaymentInstruction> paymentInstructionList = paymentInstructionRepository.findAll().collectList().block();
        assertThat(paymentInstructionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
