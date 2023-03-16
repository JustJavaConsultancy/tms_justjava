package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.Notification;
import com.simplifysynergy.domain.enumeration.NotificationType;
import com.simplifysynergy.domain.enumeration.ReadStatus;
import com.simplifysynergy.domain.enumeration.Status;
import com.simplifysynergy.repository.NotificationRepository;
import com.simplifysynergy.service.dto.NotificationDTO;
import com.simplifysynergy.service.mapper.NotificationMapper;
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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NotificationResourceIT {

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.SENT;

    private static final String DEFAULT_SENDER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ReadStatus DEFAULT_READ = ReadStatus.READ;
    private static final ReadStatus UPDATED_READ = ReadStatus.UNREAD;

    private static final NotificationType DEFAULT_NOTIFICATION_TYPE = NotificationType.EMAIL;
    private static final NotificationType UPDATED_NOTIFICATION_TYPE = NotificationType.SMS;

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity() {
        Notification notification = new Notification()
            .status(DEFAULT_STATUS)
            .sender(DEFAULT_SENDER)
            .receiver(DEFAULT_RECEIVER)
            .subject(DEFAULT_SUBJECT)
            .content(DEFAULT_CONTENT)
            .read(DEFAULT_READ)
            .notificationType(DEFAULT_NOTIFICATION_TYPE);
        return notification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity() {
        Notification notification = new Notification()
            .status(UPDATED_STATUS)
            .sender(UPDATED_SENDER)
            .receiver(UPDATED_RECEIVER)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .read(UPDATED_READ)
            .notificationType(UPDATED_NOTIFICATION_TYPE);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notificationRepository.deleteAll().block();
        notification = createEntity();
    }

    @Test
    void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().collectList().block().size();
        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNotification.getSender()).isEqualTo(DEFAULT_SENDER);
        assertThat(testNotification.getReceiver()).isEqualTo(DEFAULT_RECEIVER);
        assertThat(testNotification.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testNotification.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testNotification.getRead()).isEqualTo(DEFAULT_READ);
        assertThat(testNotification.getNotificationType()).isEqualTo(DEFAULT_NOTIFICATION_TYPE);
    }

    @Test
    void createNotificationWithExistingId() throws Exception {
        // Create the Notification with an existing ID
        notification.setId("existing_id");
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        int databaseSizeBeforeCreate = notificationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().collectList().block().size();
        // set the field null
        notification.setStatus(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().collectList().block().size();
        // set the field null
        notification.setSender(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllNotifications() {
        // Initialize the database
        notificationRepository.save(notification).block();

        // Get all the notificationList
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
            .value(hasItem(notification.getId()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].sender")
            .value(hasItem(DEFAULT_SENDER))
            .jsonPath("$.[*].receiver")
            .value(hasItem(DEFAULT_RECEIVER))
            .jsonPath("$.[*].subject")
            .value(hasItem(DEFAULT_SUBJECT))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].read")
            .value(hasItem(DEFAULT_READ.toString()))
            .jsonPath("$.[*].notificationType")
            .value(hasItem(DEFAULT_NOTIFICATION_TYPE.toString()));
    }

    @Test
    void getNotification() {
        // Initialize the database
        notificationRepository.save(notification).block();

        // Get the notification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, notification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(notification.getId()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.sender")
            .value(is(DEFAULT_SENDER))
            .jsonPath("$.receiver")
            .value(is(DEFAULT_RECEIVER))
            .jsonPath("$.subject")
            .value(is(DEFAULT_SUBJECT))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.read")
            .value(is(DEFAULT_READ.toString()))
            .jsonPath("$.notificationType")
            .value(is(DEFAULT_NOTIFICATION_TYPE.toString()));
    }

    @Test
    void getNonExistingNotification() {
        // Get the notification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNotification() throws Exception {
        // Initialize the database
        notificationRepository.save(notification).block();

        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).block();
        updatedNotification
            .status(UPDATED_STATUS)
            .sender(UPDATED_SENDER)
            .receiver(UPDATED_RECEIVER)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .read(UPDATED_READ)
            .notificationType(UPDATED_NOTIFICATION_TYPE);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNotification.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testNotification.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testNotification.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNotification.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotification.getRead()).isEqualTo(UPDATED_READ);
        assertThat(testNotification.getNotificationType()).isEqualTo(UPDATED_NOTIFICATION_TYPE);
    }

    @Test
    void putNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        notification.setId(UUID.randomUUID().toString());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        notification.setId(UUID.randomUUID().toString());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        notification.setId(UUID.randomUUID().toString());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.save(notification).block();

        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .receiver(UPDATED_RECEIVER)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .read(UPDATED_READ)
            .notificationType(UPDATED_NOTIFICATION_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNotification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNotification.getSender()).isEqualTo(DEFAULT_SENDER);
        assertThat(testNotification.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testNotification.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNotification.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotification.getRead()).isEqualTo(UPDATED_READ);
        assertThat(testNotification.getNotificationType()).isEqualTo(UPDATED_NOTIFICATION_TYPE);
    }

    @Test
    void fullUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.save(notification).block();

        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .status(UPDATED_STATUS)
            .sender(UPDATED_SENDER)
            .receiver(UPDATED_RECEIVER)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .read(UPDATED_READ)
            .notificationType(UPDATED_NOTIFICATION_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNotification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNotification.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testNotification.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testNotification.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNotification.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotification.getRead()).isEqualTo(UPDATED_READ);
        assertThat(testNotification.getNotificationType()).isEqualTo(UPDATED_NOTIFICATION_TYPE);
    }

    @Test
    void patchNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        notification.setId(UUID.randomUUID().toString());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, notificationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        notification.setId(UUID.randomUUID().toString());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        notification.setId(UUID.randomUUID().toString());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNotification() {
        // Initialize the database
        notificationRepository.save(notification).block();

        int databaseSizeBeforeDelete = notificationRepository.findAll().collectList().block().size();

        // Delete the notification
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, notification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
