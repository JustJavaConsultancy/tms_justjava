package com.simplifysynergy.web.rest;

import static com.simplifysynergy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.RightItem;
import com.simplifysynergy.repository.RightItemRepository;
import com.simplifysynergy.service.dto.RightItemDTO;
import com.simplifysynergy.service.mapper.RightItemMapper;
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
 * Integration tests for the {@link RightItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RightItemResourceIT {

    private static final String DEFAULT_NARRATION = "AAAAAAAAAA";
    private static final String UPDATED_NARRATION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_REFERENCE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/right-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RightItemRepository rightItemRepository;

    @Autowired
    private RightItemMapper rightItemMapper;

    @Autowired
    private WebTestClient webTestClient;

    private RightItem rightItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RightItem createEntity() {
        RightItem rightItem = new RightItem()
            .narration(DEFAULT_NARRATION)
            .reference(DEFAULT_REFERENCE)
            .externalReference(DEFAULT_EXTERNAL_REFERENCE)
            .amount(DEFAULT_AMOUNT);
        return rightItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RightItem createUpdatedEntity() {
        RightItem rightItem = new RightItem()
            .narration(UPDATED_NARRATION)
            .reference(UPDATED_REFERENCE)
            .externalReference(UPDATED_EXTERNAL_REFERENCE)
            .amount(UPDATED_AMOUNT);
        return rightItem;
    }

    @BeforeEach
    public void initTest() {
        rightItemRepository.deleteAll().block();
        rightItem = createEntity();
    }

    @Test
    void createRightItem() throws Exception {
        int databaseSizeBeforeCreate = rightItemRepository.findAll().collectList().block().size();
        // Create the RightItem
        RightItemDTO rightItemDTO = rightItemMapper.toDto(rightItem);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeCreate + 1);
        RightItem testRightItem = rightItemList.get(rightItemList.size() - 1);
        assertThat(testRightItem.getNarration()).isEqualTo(DEFAULT_NARRATION);
        assertThat(testRightItem.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testRightItem.getExternalReference()).isEqualTo(DEFAULT_EXTERNAL_REFERENCE);
        assertThat(testRightItem.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void createRightItemWithExistingId() throws Exception {
        // Create the RightItem with an existing ID
        rightItem.setId("existing_id");
        RightItemDTO rightItemDTO = rightItemMapper.toDto(rightItem);

        int databaseSizeBeforeCreate = rightItemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRightItems() {
        // Initialize the database
        rightItemRepository.save(rightItem).block();

        // Get all the rightItemList
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
            .value(hasItem(rightItem.getId()))
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
    void getRightItem() {
        // Initialize the database
        rightItemRepository.save(rightItem).block();

        // Get the rightItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, rightItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(rightItem.getId()))
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
    void getNonExistingRightItem() {
        // Get the rightItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRightItem() throws Exception {
        // Initialize the database
        rightItemRepository.save(rightItem).block();

        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();

        // Update the rightItem
        RightItem updatedRightItem = rightItemRepository.findById(rightItem.getId()).block();
        updatedRightItem
            .narration(UPDATED_NARRATION)
            .reference(UPDATED_REFERENCE)
            .externalReference(UPDATED_EXTERNAL_REFERENCE)
            .amount(UPDATED_AMOUNT);
        RightItemDTO rightItemDTO = rightItemMapper.toDto(updatedRightItem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rightItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
        RightItem testRightItem = rightItemList.get(rightItemList.size() - 1);
        assertThat(testRightItem.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testRightItem.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testRightItem.getExternalReference()).isEqualTo(UPDATED_EXTERNAL_REFERENCE);
        assertThat(testRightItem.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void putNonExistingRightItem() throws Exception {
        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();
        rightItem.setId(UUID.randomUUID().toString());

        // Create the RightItem
        RightItemDTO rightItemDTO = rightItemMapper.toDto(rightItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rightItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRightItem() throws Exception {
        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();
        rightItem.setId(UUID.randomUUID().toString());

        // Create the RightItem
        RightItemDTO rightItemDTO = rightItemMapper.toDto(rightItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRightItem() throws Exception {
        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();
        rightItem.setId(UUID.randomUUID().toString());

        // Create the RightItem
        RightItemDTO rightItemDTO = rightItemMapper.toDto(rightItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRightItemWithPatch() throws Exception {
        // Initialize the database
        rightItemRepository.save(rightItem).block();

        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();

        // Update the rightItem using partial update
        RightItem partialUpdatedRightItem = new RightItem();
        partialUpdatedRightItem.setId(rightItem.getId());

        partialUpdatedRightItem.narration(UPDATED_NARRATION).reference(UPDATED_REFERENCE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRightItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRightItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
        RightItem testRightItem = rightItemList.get(rightItemList.size() - 1);
        assertThat(testRightItem.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testRightItem.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testRightItem.getExternalReference()).isEqualTo(DEFAULT_EXTERNAL_REFERENCE);
        assertThat(testRightItem.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void fullUpdateRightItemWithPatch() throws Exception {
        // Initialize the database
        rightItemRepository.save(rightItem).block();

        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();

        // Update the rightItem using partial update
        RightItem partialUpdatedRightItem = new RightItem();
        partialUpdatedRightItem.setId(rightItem.getId());

        partialUpdatedRightItem
            .narration(UPDATED_NARRATION)
            .reference(UPDATED_REFERENCE)
            .externalReference(UPDATED_EXTERNAL_REFERENCE)
            .amount(UPDATED_AMOUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRightItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRightItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
        RightItem testRightItem = rightItemList.get(rightItemList.size() - 1);
        assertThat(testRightItem.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testRightItem.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testRightItem.getExternalReference()).isEqualTo(UPDATED_EXTERNAL_REFERENCE);
        assertThat(testRightItem.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void patchNonExistingRightItem() throws Exception {
        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();
        rightItem.setId(UUID.randomUUID().toString());

        // Create the RightItem
        RightItemDTO rightItemDTO = rightItemMapper.toDto(rightItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, rightItemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRightItem() throws Exception {
        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();
        rightItem.setId(UUID.randomUUID().toString());

        // Create the RightItem
        RightItemDTO rightItemDTO = rightItemMapper.toDto(rightItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRightItem() throws Exception {
        int databaseSizeBeforeUpdate = rightItemRepository.findAll().collectList().block().size();
        rightItem.setId(UUID.randomUUID().toString());

        // Create the RightItem
        RightItemDTO rightItemDTO = rightItemMapper.toDto(rightItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rightItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RightItem in the database
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRightItem() {
        // Initialize the database
        rightItemRepository.save(rightItem).block();

        int databaseSizeBeforeDelete = rightItemRepository.findAll().collectList().block().size();

        // Delete the rightItem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, rightItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RightItem> rightItemList = rightItemRepository.findAll().collectList().block();
        assertThat(rightItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
