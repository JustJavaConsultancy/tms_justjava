package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.Subscriber;
import com.simplifysynergy.repository.SubscriberRepository;
import com.simplifysynergy.service.dto.SubscriberDTO;
import com.simplifysynergy.service.mapper.SubscriberMapper;
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
 * Integration tests for the {@link SubscriberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SubscriberResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subscribers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberMapper subscriberMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Subscriber subscriber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriber createEntity() {
        Subscriber subscriber = new Subscriber().fullName(DEFAULT_FULL_NAME);
        return subscriber;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriber createUpdatedEntity() {
        Subscriber subscriber = new Subscriber().fullName(UPDATED_FULL_NAME);
        return subscriber;
    }

    @BeforeEach
    public void initTest() {
        subscriberRepository.deleteAll().block();
        subscriber = createEntity();
    }

    @Test
    void createSubscriber() throws Exception {
        int databaseSizeBeforeCreate = subscriberRepository.findAll().collectList().block().size();
        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeCreate + 1);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
    }

    @Test
    void createSubscriberWithExistingId() throws Exception {
        // Create the Subscriber with an existing ID
        subscriber.setId("existing_id");
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        int databaseSizeBeforeCreate = subscriberRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSubscribers() {
        // Initialize the database
        subscriberRepository.save(subscriber).block();

        // Get all the subscriberList
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
            .value(hasItem(subscriber.getId()))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME));
    }

    @Test
    void getSubscriber() {
        // Initialize the database
        subscriberRepository.save(subscriber).block();

        // Get the subscriber
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, subscriber.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(subscriber.getId()))
            .jsonPath("$.fullName")
            .value(is(DEFAULT_FULL_NAME));
    }

    @Test
    void getNonExistingSubscriber() {
        // Get the subscriber
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.save(subscriber).block();

        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();

        // Update the subscriber
        Subscriber updatedSubscriber = subscriberRepository.findById(subscriber.getId()).block();
        updatedSubscriber.fullName(UPDATED_FULL_NAME);
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(updatedSubscriber);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, subscriberDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getFullName()).isEqualTo(UPDATED_FULL_NAME);
    }

    @Test
    void putNonExistingSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();
        subscriber.setId(UUID.randomUUID().toString());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, subscriberDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();
        subscriber.setId(UUID.randomUUID().toString());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();
        subscriber.setId(UUID.randomUUID().toString());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSubscriberWithPatch() throws Exception {
        // Initialize the database
        subscriberRepository.save(subscriber).block();

        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();

        // Update the subscriber using partial update
        Subscriber partialUpdatedSubscriber = new Subscriber();
        partialUpdatedSubscriber.setId(subscriber.getId());

        partialUpdatedSubscriber.fullName(UPDATED_FULL_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSubscriber.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriber))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getFullName()).isEqualTo(UPDATED_FULL_NAME);
    }

    @Test
    void fullUpdateSubscriberWithPatch() throws Exception {
        // Initialize the database
        subscriberRepository.save(subscriber).block();

        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();

        // Update the subscriber using partial update
        Subscriber partialUpdatedSubscriber = new Subscriber();
        partialUpdatedSubscriber.setId(subscriber.getId());

        partialUpdatedSubscriber.fullName(UPDATED_FULL_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSubscriber.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriber))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getFullName()).isEqualTo(UPDATED_FULL_NAME);
    }

    @Test
    void patchNonExistingSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();
        subscriber.setId(UUID.randomUUID().toString());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, subscriberDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();
        subscriber.setId(UUID.randomUUID().toString());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().collectList().block().size();
        subscriber.setId(UUID.randomUUID().toString());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSubscriber() {
        // Initialize the database
        subscriberRepository.save(subscriber).block();

        int databaseSizeBeforeDelete = subscriberRepository.findAll().collectList().block().size();

        // Delete the subscriber
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, subscriber.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Subscriber> subscriberList = subscriberRepository.findAll().collectList().block();
        assertThat(subscriberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
