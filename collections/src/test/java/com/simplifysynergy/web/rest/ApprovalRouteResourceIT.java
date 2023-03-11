package com.simplifysynergy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.ApprovalRoute;
import com.simplifysynergy.repository.ApprovalRouteRepository;
import com.simplifysynergy.service.ApprovalRouteService;
import com.simplifysynergy.service.dto.ApprovalRouteDTO;
import com.simplifysynergy.service.mapper.ApprovalRouteMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link ApprovalRouteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ApprovalRouteResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/approval-routes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ApprovalRouteRepository approvalRouteRepository;

    @Mock
    private ApprovalRouteRepository approvalRouteRepositoryMock;

    @Autowired
    private ApprovalRouteMapper approvalRouteMapper;

    @Mock
    private ApprovalRouteService approvalRouteServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private ApprovalRoute approvalRoute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRoute createEntity() {
        ApprovalRoute approvalRoute = new ApprovalRoute().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION);
        return approvalRoute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRoute createUpdatedEntity() {
        ApprovalRoute approvalRoute = new ApprovalRoute().code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        return approvalRoute;
    }

    @BeforeEach
    public void initTest() {
        approvalRouteRepository.deleteAll().block();
        approvalRoute = createEntity();
    }

    @Test
    void createApprovalRoute() throws Exception {
        int databaseSizeBeforeCreate = approvalRouteRepository.findAll().collectList().block().size();
        // Create the ApprovalRoute
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalRoute testApprovalRoute = approvalRouteList.get(approvalRouteList.size() - 1);
        assertThat(testApprovalRoute.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testApprovalRoute.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createApprovalRouteWithExistingId() throws Exception {
        // Create the ApprovalRoute with an existing ID
        approvalRoute.setId("existing_id");
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);

        int databaseSizeBeforeCreate = approvalRouteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRouteRepository.findAll().collectList().block().size();
        // set the field null
        approvalRoute.setCode(null);

        // Create the ApprovalRoute, which fails.
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllApprovalRoutes() {
        // Initialize the database
        approvalRouteRepository.save(approvalRoute).block();

        // Get all the approvalRouteList
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
            .value(hasItem(approvalRoute.getId()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApprovalRoutesWithEagerRelationshipsIsEnabled() {
        when(approvalRouteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(approvalRouteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApprovalRoutesWithEagerRelationshipsIsNotEnabled() {
        when(approvalRouteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(approvalRouteRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getApprovalRoute() {
        // Initialize the database
        approvalRouteRepository.save(approvalRoute).block();

        // Get the approvalRoute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, approvalRoute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(approvalRoute.getId()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingApprovalRoute() {
        // Get the approvalRoute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingApprovalRoute() throws Exception {
        // Initialize the database
        approvalRouteRepository.save(approvalRoute).block();

        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();

        // Update the approvalRoute
        ApprovalRoute updatedApprovalRoute = approvalRouteRepository.findById(approvalRoute.getId()).block();
        updatedApprovalRoute.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(updatedApprovalRoute);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, approvalRouteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRoute testApprovalRoute = approvalRouteList.get(approvalRouteList.size() - 1);
        assertThat(testApprovalRoute.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApprovalRoute.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingApprovalRoute() throws Exception {
        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();
        approvalRoute.setId(UUID.randomUUID().toString());

        // Create the ApprovalRoute
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, approvalRouteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchApprovalRoute() throws Exception {
        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();
        approvalRoute.setId(UUID.randomUUID().toString());

        // Create the ApprovalRoute
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamApprovalRoute() throws Exception {
        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();
        approvalRoute.setId(UUID.randomUUID().toString());

        // Create the ApprovalRoute
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateApprovalRouteWithPatch() throws Exception {
        // Initialize the database
        approvalRouteRepository.save(approvalRoute).block();

        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();

        // Update the approvalRoute using partial update
        ApprovalRoute partialUpdatedApprovalRoute = new ApprovalRoute();
        partialUpdatedApprovalRoute.setId(approvalRoute.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedApprovalRoute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalRoute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRoute testApprovalRoute = approvalRouteList.get(approvalRouteList.size() - 1);
        assertThat(testApprovalRoute.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testApprovalRoute.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateApprovalRouteWithPatch() throws Exception {
        // Initialize the database
        approvalRouteRepository.save(approvalRoute).block();

        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();

        // Update the approvalRoute using partial update
        ApprovalRoute partialUpdatedApprovalRoute = new ApprovalRoute();
        partialUpdatedApprovalRoute.setId(approvalRoute.getId());

        partialUpdatedApprovalRoute.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedApprovalRoute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalRoute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRoute testApprovalRoute = approvalRouteList.get(approvalRouteList.size() - 1);
        assertThat(testApprovalRoute.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApprovalRoute.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingApprovalRoute() throws Exception {
        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();
        approvalRoute.setId(UUID.randomUUID().toString());

        // Create the ApprovalRoute
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, approvalRouteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchApprovalRoute() throws Exception {
        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();
        approvalRoute.setId(UUID.randomUUID().toString());

        // Create the ApprovalRoute
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamApprovalRoute() throws Exception {
        int databaseSizeBeforeUpdate = approvalRouteRepository.findAll().collectList().block().size();
        approvalRoute.setId(UUID.randomUUID().toString());

        // Create the ApprovalRoute
        ApprovalRouteDTO approvalRouteDTO = approvalRouteMapper.toDto(approvalRoute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(approvalRouteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ApprovalRoute in the database
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteApprovalRoute() {
        // Initialize the database
        approvalRouteRepository.save(approvalRoute).block();

        int databaseSizeBeforeDelete = approvalRouteRepository.findAll().collectList().block().size();

        // Delete the approvalRoute
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, approvalRoute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ApprovalRoute> approvalRouteList = approvalRouteRepository.findAll().collectList().block();
        assertThat(approvalRouteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
