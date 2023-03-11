package com.simplifysynergy.web.rest;

import static com.simplifysynergy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.Chart;
import com.simplifysynergy.repository.ChartRepository;
import com.simplifysynergy.service.dto.ChartDTO;
import com.simplifysynergy.service.mapper.ChartMapper;
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
 * Integration tests for the {@link ChartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ChartResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/charts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Chart chart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chart createEntity() {
        Chart chart = new Chart().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION).balance(DEFAULT_BALANCE);
        return chart;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chart createUpdatedEntity() {
        Chart chart = new Chart().code(UPDATED_CODE).description(UPDATED_DESCRIPTION).balance(UPDATED_BALANCE);
        return chart;
    }

    @BeforeEach
    public void initTest() {
        chartRepository.deleteAll().block();
        chart = createEntity();
    }

    @Test
    void createChart() throws Exception {
        int databaseSizeBeforeCreate = chartRepository.findAll().collectList().block().size();
        // Create the Chart
        ChartDTO chartDTO = chartMapper.toDto(chart);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeCreate + 1);
        Chart testChart = chartList.get(chartList.size() - 1);
        assertThat(testChart.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChart.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
    }

    @Test
    void createChartWithExistingId() throws Exception {
        // Create the Chart with an existing ID
        chart.setId("existing_id");
        ChartDTO chartDTO = chartMapper.toDto(chart);

        int databaseSizeBeforeCreate = chartRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chartRepository.findAll().collectList().block().size();
        // set the field null
        chart.setCode(null);

        // Create the Chart, which fails.
        ChartDTO chartDTO = chartMapper.toDto(chart);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCharts() {
        // Initialize the database
        chartRepository.save(chart).block();

        // Get all the chartList
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
            .value(hasItem(chart.getId()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].balance")
            .value(hasItem(sameNumber(DEFAULT_BALANCE)));
    }

    @Test
    void getChart() {
        // Initialize the database
        chartRepository.save(chart).block();

        // Get the chart
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, chart.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(chart.getId()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.balance")
            .value(is(sameNumber(DEFAULT_BALANCE)));
    }

    @Test
    void getNonExistingChart() {
        // Get the chart
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingChart() throws Exception {
        // Initialize the database
        chartRepository.save(chart).block();

        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();

        // Update the chart
        Chart updatedChart = chartRepository.findById(chart.getId()).block();
        updatedChart.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).balance(UPDATED_BALANCE);
        ChartDTO chartDTO = chartMapper.toDto(updatedChart);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chartDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
        Chart testChart = chartList.get(chartList.size() - 1);
        assertThat(testChart.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChart.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChart.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    void putNonExistingChart() throws Exception {
        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();
        chart.setId(UUID.randomUUID().toString());

        // Create the Chart
        ChartDTO chartDTO = chartMapper.toDto(chart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chartDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChart() throws Exception {
        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();
        chart.setId(UUID.randomUUID().toString());

        // Create the Chart
        ChartDTO chartDTO = chartMapper.toDto(chart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChart() throws Exception {
        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();
        chart.setId(UUID.randomUUID().toString());

        // Create the Chart
        ChartDTO chartDTO = chartMapper.toDto(chart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChartWithPatch() throws Exception {
        // Initialize the database
        chartRepository.save(chart).block();

        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();

        // Update the chart using partial update
        Chart partialUpdatedChart = new Chart();
        partialUpdatedChart.setId(chart.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChart.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChart))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
        Chart testChart = chartList.get(chartList.size() - 1);
        assertThat(testChart.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChart.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
    }

    @Test
    void fullUpdateChartWithPatch() throws Exception {
        // Initialize the database
        chartRepository.save(chart).block();

        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();

        // Update the chart using partial update
        Chart partialUpdatedChart = new Chart();
        partialUpdatedChart.setId(chart.getId());

        partialUpdatedChart.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).balance(UPDATED_BALANCE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChart.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChart))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
        Chart testChart = chartList.get(chartList.size() - 1);
        assertThat(testChart.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChart.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChart.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    void patchNonExistingChart() throws Exception {
        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();
        chart.setId(UUID.randomUUID().toString());

        // Create the Chart
        ChartDTO chartDTO = chartMapper.toDto(chart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, chartDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChart() throws Exception {
        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();
        chart.setId(UUID.randomUUID().toString());

        // Create the Chart
        ChartDTO chartDTO = chartMapper.toDto(chart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChart() throws Exception {
        int databaseSizeBeforeUpdate = chartRepository.findAll().collectList().block().size();
        chart.setId(UUID.randomUUID().toString());

        // Create the Chart
        ChartDTO chartDTO = chartMapper.toDto(chart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chartDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Chart in the database
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChart() {
        // Initialize the database
        chartRepository.save(chart).block();

        int databaseSizeBeforeDelete = chartRepository.findAll().collectList().block().size();

        // Delete the chart
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, chart.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Chart> chartList = chartRepository.findAll().collectList().block();
        assertThat(chartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
