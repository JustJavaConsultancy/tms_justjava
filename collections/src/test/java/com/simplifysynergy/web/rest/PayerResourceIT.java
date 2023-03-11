package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.Payer;
import com.simplifysynergy.repository.PayerRepository;
import com.simplifysynergy.service.dto.PayerDTO;
import com.simplifysynergy.service.mapper.PayerMapper;
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
 * Integration tests for the {@link PayerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PayerResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PayerRepository payerRepository;

    @Autowired
    private PayerMapper payerMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Payer payer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payer createEntity() {
        Payer payer = new Payer().email(DEFAULT_EMAIL).phone(DEFAULT_PHONE).fullName(DEFAULT_FULL_NAME);
        return payer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payer createUpdatedEntity() {
        Payer payer = new Payer().email(UPDATED_EMAIL).phone(UPDATED_PHONE).fullName(UPDATED_FULL_NAME);
        return payer;
    }

    @BeforeEach
    public void initTest() {
        payerRepository.deleteAll().block();
        payer = createEntity();
    }

    @Test
    void createPayer() throws Exception {
        int databaseSizeBeforeCreate = payerRepository.findAll().collectList().block().size();
        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeCreate + 1);
        Payer testPayer = payerList.get(payerList.size() - 1);
        assertThat(testPayer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPayer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPayer.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
    }

    @Test
    void createPayerWithExistingId() throws Exception {
        // Create the Payer with an existing ID
        payer.setId("existing_id");
        PayerDTO payerDTO = payerMapper.toDto(payer);

        int databaseSizeBeforeCreate = payerRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPayers() {
        // Initialize the database
        payerRepository.save(payer).block();

        // Get all the payerList
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
            .value(hasItem(payer.getId()))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME));
    }

    @Test
    void getPayer() {
        // Initialize the database
        payerRepository.save(payer).block();

        // Get the payer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, payer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(payer.getId()))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE))
            .jsonPath("$.fullName")
            .value(is(DEFAULT_FULL_NAME));
    }

    @Test
    void getNonExistingPayer() {
        // Get the payer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPayer() throws Exception {
        // Initialize the database
        payerRepository.save(payer).block();

        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();

        // Update the payer
        Payer updatedPayer = payerRepository.findById(payer.getId()).block();
        updatedPayer.email(UPDATED_EMAIL).phone(UPDATED_PHONE).fullName(UPDATED_FULL_NAME);
        PayerDTO payerDTO = payerMapper.toDto(updatedPayer);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, payerDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
        Payer testPayer = payerList.get(payerList.size() - 1);
        assertThat(testPayer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPayer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPayer.getFullName()).isEqualTo(UPDATED_FULL_NAME);
    }

    @Test
    void putNonExistingPayer() throws Exception {
        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();
        payer.setId(UUID.randomUUID().toString());

        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, payerDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPayer() throws Exception {
        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();
        payer.setId(UUID.randomUUID().toString());

        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPayer() throws Exception {
        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();
        payer.setId(UUID.randomUUID().toString());

        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePayerWithPatch() throws Exception {
        // Initialize the database
        payerRepository.save(payer).block();

        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();

        // Update the payer using partial update
        Payer partialUpdatedPayer = new Payer();
        partialUpdatedPayer.setId(payer.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPayer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
        Payer testPayer = payerList.get(payerList.size() - 1);
        assertThat(testPayer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPayer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPayer.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
    }

    @Test
    void fullUpdatePayerWithPatch() throws Exception {
        // Initialize the database
        payerRepository.save(payer).block();

        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();

        // Update the payer using partial update
        Payer partialUpdatedPayer = new Payer();
        partialUpdatedPayer.setId(payer.getId());

        partialUpdatedPayer.email(UPDATED_EMAIL).phone(UPDATED_PHONE).fullName(UPDATED_FULL_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPayer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
        Payer testPayer = payerList.get(payerList.size() - 1);
        assertThat(testPayer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPayer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPayer.getFullName()).isEqualTo(UPDATED_FULL_NAME);
    }

    @Test
    void patchNonExistingPayer() throws Exception {
        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();
        payer.setId(UUID.randomUUID().toString());

        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, payerDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPayer() throws Exception {
        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();
        payer.setId(UUID.randomUUID().toString());

        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPayer() throws Exception {
        int databaseSizeBeforeUpdate = payerRepository.findAll().collectList().block().size();
        payer.setId(UUID.randomUUID().toString());

        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payerDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePayer() {
        // Initialize the database
        payerRepository.save(payer).block();

        int databaseSizeBeforeDelete = payerRepository.findAll().collectList().block().size();

        // Delete the payer
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, payer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Payer> payerList = payerRepository.findAll().collectList().block();
        assertThat(payerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
