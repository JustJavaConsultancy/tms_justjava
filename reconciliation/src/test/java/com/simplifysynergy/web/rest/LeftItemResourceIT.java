package com.simplifysynergy.web.rest;

import static com.simplifysynergy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.LeftItem;
import com.simplifysynergy.repository.LeftItemRepository;
import com.simplifysynergy.service.dto.LeftItemDTO;
import com.simplifysynergy.service.mapper.LeftItemMapper;
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
 * Integration tests for the {@link LeftItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LeftItemResourceIT {

    private static final String DEFAULT_NARRATION = "AAAAAAAAAA";
    private static final String UPDATED_NARRATION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_REFERENCE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/left-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private LeftItemRepository leftItemRepository;

    @Autowired
    private LeftItemMapper leftItemMapper;

    @Autowired
    private WebTestClient webTestClient;

    private LeftItem leftItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeftItem createEntity() {
        LeftItem leftItem = new LeftItem()
            .narration(DEFAULT_NARRATION)
            .reference(DEFAULT_REFERENCE)
            .externalReference(DEFAULT_EXTERNAL_REFERENCE)
            .amount(DEFAULT_AMOUNT);
        return leftItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeftItem createUpdatedEntity() {
        LeftItem leftItem = new LeftItem()
            .narration(UPDATED_NARRATION)
            .reference(UPDATED_REFERENCE)
            .externalReference(UPDATED_EXTERNAL_REFERENCE)
            .amount(UPDATED_AMOUNT);
        return leftItem;
    }

    @BeforeEach
    public void initTest() {
        leftItemRepository.deleteAll().block();
        leftItem = createEntity();
    }

    @Test
    void createLeftItem() throws Exception {
        int databaseSizeBeforeCreate = leftItemRepository.findAll().collectList().block().size();
        // Create the LeftItem
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(leftItem);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeCreate + 1);
        LeftItem testLeftItem = leftItemList.get(leftItemList.size() - 1);
        assertThat(testLeftItem.getNarration()).isEqualTo(DEFAULT_NARRATION);
        assertThat(testLeftItem.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testLeftItem.getExternalReference()).isEqualTo(DEFAULT_EXTERNAL_REFERENCE);
        assertThat(testLeftItem.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void createLeftItemWithExistingId() throws Exception {
        // Create the LeftItem with an existing ID
        leftItem.setId("existing_id");
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(leftItem);

        int databaseSizeBeforeCreate = leftItemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLeftItems() {
        // Initialize the database
        leftItemRepository.save(leftItem).block();

        // Get all the leftItemList
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
            .value(hasItem(leftItem.getId()))
            .jsonPath("$.[*].narration")
            .value(hasItem(DEFAULT_NARRATION))
            .jsonPath("$.[*].reference")
            .value(hasItem(DEFAULT_REFERENCE))
            .jsonPath("$.[*].externalReference")
            .value(hasItem(DEFAULT_EXTERNAL_REFERENCE))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getLeftItem() {
        // Initialize the database
        leftItemRepository.save(leftItem).block();

        // Get the leftItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, leftItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(leftItem.getId()))
            .jsonPath("$.narration")
            .value(is(DEFAULT_NARRATION))
            .jsonPath("$.reference")
            .value(is(DEFAULT_REFERENCE))
            .jsonPath("$.externalReference")
            .value(is(DEFAULT_EXTERNAL_REFERENCE))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getNonExistingLeftItem() {
        // Get the leftItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLeftItem() throws Exception {
        // Initialize the database
        leftItemRepository.save(leftItem).block();

        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();

        // Update the leftItem
        LeftItem updatedLeftItem = leftItemRepository.findById(leftItem.getId()).block();
        updatedLeftItem
            .narration(UPDATED_NARRATION)
            .reference(UPDATED_REFERENCE)
            .externalReference(UPDATED_EXTERNAL_REFERENCE)
            .amount(UPDATED_AMOUNT);
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(updatedLeftItem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, leftItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
        LeftItem testLeftItem = leftItemList.get(leftItemList.size() - 1);
        assertThat(testLeftItem.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testLeftItem.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testLeftItem.getExternalReference()).isEqualTo(UPDATED_EXTERNAL_REFERENCE);
        assertThat(testLeftItem.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void putNonExistingLeftItem() throws Exception {
        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();
        leftItem.setId(UUID.randomUUID().toString());

        // Create the LeftItem
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(leftItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, leftItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLeftItem() throws Exception {
        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();
        leftItem.setId(UUID.randomUUID().toString());

        // Create the LeftItem
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(leftItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLeftItem() throws Exception {
        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();
        leftItem.setId(UUID.randomUUID().toString());

        // Create the LeftItem
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(leftItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLeftItemWithPatch() throws Exception {
        // Initialize the database
        leftItemRepository.save(leftItem).block();

        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();

        // Update the leftItem using partial update
        LeftItem partialUpdatedLeftItem = new LeftItem();
        partialUpdatedLeftItem.setId(leftItem.getId());

        partialUpdatedLeftItem.narration(UPDATED_NARRATION).reference(UPDATED_REFERENCE).amount(UPDATED_AMOUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLeftItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLeftItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
        LeftItem testLeftItem = leftItemList.get(leftItemList.size() - 1);
        assertThat(testLeftItem.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testLeftItem.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testLeftItem.getExternalReference()).isEqualTo(DEFAULT_EXTERNAL_REFERENCE);
        assertThat(testLeftItem.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void fullUpdateLeftItemWithPatch() throws Exception {
        // Initialize the database
        leftItemRepository.save(leftItem).block();

        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();

        // Update the leftItem using partial update
        LeftItem partialUpdatedLeftItem = new LeftItem();
        partialUpdatedLeftItem.setId(leftItem.getId());

        partialUpdatedLeftItem
            .narration(UPDATED_NARRATION)
            .reference(UPDATED_REFERENCE)
            .externalReference(UPDATED_EXTERNAL_REFERENCE)
            .amount(UPDATED_AMOUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLeftItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLeftItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
        LeftItem testLeftItem = leftItemList.get(leftItemList.size() - 1);
        assertThat(testLeftItem.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testLeftItem.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testLeftItem.getExternalReference()).isEqualTo(UPDATED_EXTERNAL_REFERENCE);
        assertThat(testLeftItem.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void patchNonExistingLeftItem() throws Exception {
        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();
        leftItem.setId(UUID.randomUUID().toString());

        // Create the LeftItem
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(leftItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, leftItemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLeftItem() throws Exception {
        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();
        leftItem.setId(UUID.randomUUID().toString());

        // Create the LeftItem
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(leftItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLeftItem() throws Exception {
        int databaseSizeBeforeUpdate = leftItemRepository.findAll().collectList().block().size();
        leftItem.setId(UUID.randomUUID().toString());

        // Create the LeftItem
        LeftItemDTO leftItemDTO = leftItemMapper.toDto(leftItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(leftItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LeftItem in the database
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLeftItem() {
        // Initialize the database
        leftItemRepository.save(leftItem).block();

        int databaseSizeBeforeDelete = leftItemRepository.findAll().collectList().block().size();

        // Delete the leftItem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, leftItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<LeftItem> leftItemList = leftItemRepository.findAll().collectList().block();
        assertThat(leftItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
