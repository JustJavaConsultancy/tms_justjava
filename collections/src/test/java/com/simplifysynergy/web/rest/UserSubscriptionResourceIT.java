package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.UserSubscription;
import com.simplifysynergy.domain.enumeration.SubscriptionStatus;
import com.simplifysynergy.repository.UserSubscriptionRepository;
import com.simplifysynergy.service.dto.UserSubscriptionDTO;
import com.simplifysynergy.service.mapper.UserSubscriptionMapper;
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
 * Integration tests for the {@link UserSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserSubscriptionResourceIT {

    private static final SubscriptionStatus DEFAULT_STATUS = SubscriptionStatus.ACTIVE;
    private static final SubscriptionStatus UPDATED_STATUS = SubscriptionStatus.INACTIVE;

    private static final String ENTITY_API_URL = "/api/user-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;

    @Autowired
    private WebTestClient webTestClient;

    private UserSubscription userSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSubscription createEntity() {
        UserSubscription userSubscription = new UserSubscription().status(DEFAULT_STATUS);
        return userSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSubscription createUpdatedEntity() {
        UserSubscription userSubscription = new UserSubscription().status(UPDATED_STATUS);
        return userSubscription;
    }

    @BeforeEach
    public void initTest() {
        userSubscriptionRepository.deleteAll().block();
        userSubscription = createEntity();
    }

    @Test
    void createUserSubscription() throws Exception {
        int databaseSizeBeforeCreate = userSubscriptionRepository.findAll().collectList().block().size();
        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createUserSubscriptionWithExistingId() throws Exception {
        // Create the UserSubscription with an existing ID
        userSubscription.setId("existing_id");
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        int databaseSizeBeforeCreate = userSubscriptionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllUserSubscriptions() {
        // Initialize the database
        userSubscriptionRepository.save(userSubscription).block();

        // Get all the userSubscriptionList
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
            .value(hasItem(userSubscription.getId()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()));
    }

    @Test
    void getUserSubscription() {
        // Initialize the database
        userSubscriptionRepository.save(userSubscription).block();

        // Get the userSubscription
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userSubscription.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userSubscription.getId()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingUserSubscription() {
        // Get the userSubscription
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.save(userSubscription).block();

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();

        // Update the userSubscription
        UserSubscription updatedUserSubscription = userSubscriptionRepository.findById(userSubscription.getId()).block();
        updatedUserSubscription.status(UPDATED_STATUS);
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(updatedUserSubscription);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();
        userSubscription.setId(UUID.randomUUID().toString());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();
        userSubscription.setId(UUID.randomUUID().toString());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();
        userSubscription.setId(UUID.randomUUID().toString());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserSubscriptionWithPatch() throws Exception {
        // Initialize the database
        userSubscriptionRepository.save(userSubscription).block();

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();

        // Update the userSubscription using partial update
        UserSubscription partialUpdatedUserSubscription = new UserSubscription();
        partialUpdatedUserSubscription.setId(userSubscription.getId());

        partialUpdatedUserSubscription.status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserSubscription.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSubscription))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdateUserSubscriptionWithPatch() throws Exception {
        // Initialize the database
        userSubscriptionRepository.save(userSubscription).block();

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();

        // Update the userSubscription using partial update
        UserSubscription partialUpdatedUserSubscription = new UserSubscription();
        partialUpdatedUserSubscription.setId(userSubscription.getId());

        partialUpdatedUserSubscription.status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserSubscription.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSubscription))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();
        userSubscription.setId(UUID.randomUUID().toString());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();
        userSubscription.setId(UUID.randomUUID().toString());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().collectList().block().size();
        userSubscription.setId(UUID.randomUUID().toString());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserSubscription() {
        // Initialize the database
        userSubscriptionRepository.save(userSubscription).block();

        int databaseSizeBeforeDelete = userSubscriptionRepository.findAll().collectList().block().size();

        // Delete the userSubscription
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userSubscription.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll().collectList().block();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
