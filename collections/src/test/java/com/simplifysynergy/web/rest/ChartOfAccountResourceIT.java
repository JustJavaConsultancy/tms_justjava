package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.ChartOfAccount;
import com.simplifysynergy.repository.ChartOfAccountRepository;
import com.simplifysynergy.service.dto.ChartOfAccountDTO;
import com.simplifysynergy.service.mapper.ChartOfAccountMapper;
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
 * Integration tests for the {@link ChartOfAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ChartOfAccountResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chart-of-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private ChartOfAccountMapper chartOfAccountMapper;

    @Autowired
    private WebTestClient webTestClient;

    private ChartOfAccount chartOfAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChartOfAccount createEntity() {
        ChartOfAccount chartOfAccount = new ChartOfAccount().description(DEFAULT_DESCRIPTION);
        return chartOfAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChartOfAccount createUpdatedEntity() {
        ChartOfAccount chartOfAccount = new ChartOfAccount().description(UPDATED_DESCRIPTION);
        return chartOfAccount;
    }

    @BeforeEach
    public void initTest() {
        chartOfAccountRepository.deleteAll().block();
        chartOfAccount = createEntity();
    }

    @Test
    void createChartOfAccount() throws Exception {
        int databaseSizeBeforeCreate = chartOfAccountRepository.findAll().collectList().block().size();
        // Create the ChartOfAccount
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(chartOfAccount);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeCreate + 1);
        ChartOfAccount testChartOfAccount = chartOfAccountList.get(chartOfAccountList.size() - 1);
        assertThat(testChartOfAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createChartOfAccountWithExistingId() throws Exception {
        // Create the ChartOfAccount with an existing ID
        chartOfAccount.setId("existing_id");
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(chartOfAccount);

        int databaseSizeBeforeCreate = chartOfAccountRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllChartOfAccounts() {
        // Initialize the database
        chartOfAccountRepository.save(chartOfAccount).block();

        // Get all the chartOfAccountList
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
            .value(hasItem(chartOfAccount.getId()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getChartOfAccount() {
        // Initialize the database
        chartOfAccountRepository.save(chartOfAccount).block();

        // Get the chartOfAccount
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, chartOfAccount.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(chartOfAccount.getId()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingChartOfAccount() {
        // Get the chartOfAccount
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingChartOfAccount() throws Exception {
        // Initialize the database
        chartOfAccountRepository.save(chartOfAccount).block();

        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();

        // Update the chartOfAccount
        ChartOfAccount updatedChartOfAccount = chartOfAccountRepository.findById(chartOfAccount.getId()).block();
        updatedChartOfAccount.description(UPDATED_DESCRIPTION);
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(updatedChartOfAccount);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chartOfAccountDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
        ChartOfAccount testChartOfAccount = chartOfAccountList.get(chartOfAccountList.size() - 1);
        assertThat(testChartOfAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingChartOfAccount() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();
        chartOfAccount.setId(UUID.randomUUID().toString());

        // Create the ChartOfAccount
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(chartOfAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chartOfAccountDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChartOfAccount() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();
        chartOfAccount.setId(UUID.randomUUID().toString());

        // Create the ChartOfAccount
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(chartOfAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChartOfAccount() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();
        chartOfAccount.setId(UUID.randomUUID().toString());

        // Create the ChartOfAccount
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(chartOfAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChartOfAccountWithPatch() throws Exception {
        // Initialize the database
        chartOfAccountRepository.save(chartOfAccount).block();

        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();

        // Update the chartOfAccount using partial update
        ChartOfAccount partialUpdatedChartOfAccount = new ChartOfAccount();
        partialUpdatedChartOfAccount.setId(chartOfAccount.getId());

        partialUpdatedChartOfAccount.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChartOfAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChartOfAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
        ChartOfAccount testChartOfAccount = chartOfAccountList.get(chartOfAccountList.size() - 1);
        assertThat(testChartOfAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateChartOfAccountWithPatch() throws Exception {
        // Initialize the database
        chartOfAccountRepository.save(chartOfAccount).block();

        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();

        // Update the chartOfAccount using partial update
        ChartOfAccount partialUpdatedChartOfAccount = new ChartOfAccount();
        partialUpdatedChartOfAccount.setId(chartOfAccount.getId());

        partialUpdatedChartOfAccount.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChartOfAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChartOfAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
        ChartOfAccount testChartOfAccount = chartOfAccountList.get(chartOfAccountList.size() - 1);
        assertThat(testChartOfAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingChartOfAccount() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();
        chartOfAccount.setId(UUID.randomUUID().toString());

        // Create the ChartOfAccount
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(chartOfAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, chartOfAccountDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChartOfAccount() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();
        chartOfAccount.setId(UUID.randomUUID().toString());

        // Create the ChartOfAccount
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(chartOfAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChartOfAccount() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().collectList().block().size();
        chartOfAccount.setId(UUID.randomUUID().toString());

        // Create the ChartOfAccount
        ChartOfAccountDTO chartOfAccountDTO = chartOfAccountMapper.toDto(chartOfAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartOfAccountDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChartOfAccount() {
        // Initialize the database
        chartOfAccountRepository.save(chartOfAccount).block();

        int databaseSizeBeforeDelete = chartOfAccountRepository.findAll().collectList().block().size();

        // Delete the chartOfAccount
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, chartOfAccount.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll().collectList().block();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
