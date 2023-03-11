package com.simplifysynergy.web.rest;

import static com.simplifysynergy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.CollectionService;
import com.simplifysynergy.domain.enumeration.Frequency;
import com.simplifysynergy.repository.CollectionServiceRepository;
import com.simplifysynergy.service.dto.CollectionServiceDTO;
import com.simplifysynergy.service.mapper.CollectionServiceMapper;
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
 * Integration tests for the {@link CollectionServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CollectionServiceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Frequency DEFAULT_FREQUENCY = Frequency.WEEKLY;
    private static final Frequency UPDATED_FREQUENCY = Frequency.MONTHLY;

    private static final String ENTITY_API_URL = "/api/collection-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CollectionServiceRepository collectionServiceRepository;

    @Autowired
    private CollectionServiceMapper collectionServiceMapper;

    @Autowired
    private WebTestClient webTestClient;

    private CollectionService collectionService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionService createEntity() {
        CollectionService collectionService = new CollectionService()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .frequency(DEFAULT_FREQUENCY);
        return collectionService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionService createUpdatedEntity() {
        CollectionService collectionService = new CollectionService()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .frequency(UPDATED_FREQUENCY);
        return collectionService;
    }

    @BeforeEach
    public void initTest() {
        collectionServiceRepository.deleteAll().block();
        collectionService = createEntity();
    }

    @Test
    void createCollectionService() throws Exception {
        int databaseSizeBeforeCreate = collectionServiceRepository.findAll().collectList().block().size();
        // Create the CollectionService
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(collectionService);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeCreate + 1);
        CollectionService testCollectionService = collectionServiceList.get(collectionServiceList.size() - 1);
        assertThat(testCollectionService.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCollectionService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCollectionService.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testCollectionService.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
    }

    @Test
    void createCollectionServiceWithExistingId() throws Exception {
        // Create the CollectionService with an existing ID
        collectionService.setId("existing_id");
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(collectionService);

        int databaseSizeBeforeCreate = collectionServiceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCollectionServices() {
        // Initialize the database
        collectionServiceRepository.save(collectionService).block();

        // Get all the collectionServiceList
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
            .value(hasItem(collectionService.getId()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].frequency")
            .value(hasItem(DEFAULT_FREQUENCY.toString()));
    }

    @Test
    void getCollectionService() {
        // Initialize the database
        collectionServiceRepository.save(collectionService).block();

        // Get the collectionService
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, collectionService.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(collectionService.getId()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.frequency")
            .value(is(DEFAULT_FREQUENCY.toString()));
    }

    @Test
    void getNonExistingCollectionService() {
        // Get the collectionService
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCollectionService() throws Exception {
        // Initialize the database
        collectionServiceRepository.save(collectionService).block();

        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();

        // Update the collectionService
        CollectionService updatedCollectionService = collectionServiceRepository.findById(collectionService.getId()).block();
        updatedCollectionService.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).amount(UPDATED_AMOUNT).frequency(UPDATED_FREQUENCY);
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(updatedCollectionService);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, collectionServiceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
        CollectionService testCollectionService = collectionServiceList.get(collectionServiceList.size() - 1);
        assertThat(testCollectionService.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCollectionService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCollectionService.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testCollectionService.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    void putNonExistingCollectionService() throws Exception {
        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();
        collectionService.setId(UUID.randomUUID().toString());

        // Create the CollectionService
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(collectionService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, collectionServiceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCollectionService() throws Exception {
        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();
        collectionService.setId(UUID.randomUUID().toString());

        // Create the CollectionService
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(collectionService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCollectionService() throws Exception {
        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();
        collectionService.setId(UUID.randomUUID().toString());

        // Create the CollectionService
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(collectionService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCollectionServiceWithPatch() throws Exception {
        // Initialize the database
        collectionServiceRepository.save(collectionService).block();

        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();

        // Update the collectionService using partial update
        CollectionService partialUpdatedCollectionService = new CollectionService();
        partialUpdatedCollectionService.setId(collectionService.getId());

        partialUpdatedCollectionService.description(UPDATED_DESCRIPTION).amount(UPDATED_AMOUNT).frequency(UPDATED_FREQUENCY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCollectionService.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCollectionService))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
        CollectionService testCollectionService = collectionServiceList.get(collectionServiceList.size() - 1);
        assertThat(testCollectionService.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCollectionService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCollectionService.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testCollectionService.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    void fullUpdateCollectionServiceWithPatch() throws Exception {
        // Initialize the database
        collectionServiceRepository.save(collectionService).block();

        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();

        // Update the collectionService using partial update
        CollectionService partialUpdatedCollectionService = new CollectionService();
        partialUpdatedCollectionService.setId(collectionService.getId());

        partialUpdatedCollectionService
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .frequency(UPDATED_FREQUENCY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCollectionService.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCollectionService))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
        CollectionService testCollectionService = collectionServiceList.get(collectionServiceList.size() - 1);
        assertThat(testCollectionService.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCollectionService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCollectionService.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testCollectionService.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    void patchNonExistingCollectionService() throws Exception {
        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();
        collectionService.setId(UUID.randomUUID().toString());

        // Create the CollectionService
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(collectionService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, collectionServiceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCollectionService() throws Exception {
        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();
        collectionService.setId(UUID.randomUUID().toString());

        // Create the CollectionService
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(collectionService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCollectionService() throws Exception {
        int databaseSizeBeforeUpdate = collectionServiceRepository.findAll().collectList().block().size();
        collectionService.setId(UUID.randomUUID().toString());

        // Create the CollectionService
        CollectionServiceDTO collectionServiceDTO = collectionServiceMapper.toDto(collectionService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(collectionServiceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CollectionService in the database
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCollectionService() {
        // Initialize the database
        collectionServiceRepository.save(collectionService).block();

        int databaseSizeBeforeDelete = collectionServiceRepository.findAll().collectList().block().size();

        // Delete the collectionService
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, collectionService.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CollectionService> collectionServiceList = collectionServiceRepository.findAll().collectList().block();
        assertThat(collectionServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
