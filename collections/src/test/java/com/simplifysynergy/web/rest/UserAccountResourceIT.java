package com.simplifysynergy.web.rest;

import static com.simplifysynergy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.simplifysynergy.IntegrationTest;
import com.simplifysynergy.domain.UserAccount;
import com.simplifysynergy.repository.UserAccountRepository;
import com.simplifysynergy.service.dto.UserAccountDTO;
import com.simplifysynergy.service.mapper.UserAccountMapper;
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
 * Integration tests for the {@link UserAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserAccountResourceIT {

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AVAILABLE_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVAILABLE_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_BALANCE = new BigDecimal(2);

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private WebTestClient webTestClient;

    private UserAccount userAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createEntity() {
        UserAccount userAccount = new UserAccount()
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .availableBalance(DEFAULT_AVAILABLE_BALANCE)
            .currentBalance(DEFAULT_CURRENT_BALANCE)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .bankCode(DEFAULT_BANK_CODE)
            .bankName(DEFAULT_BANK_NAME);
        return userAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createUpdatedEntity() {
        UserAccount userAccount = new UserAccount()
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .currentBalance(UPDATED_CURRENT_BALANCE)
            .accountName(UPDATED_ACCOUNT_NAME)
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME);
        return userAccount;
    }

    @BeforeEach
    public void initTest() {
        userAccountRepository.deleteAll().block();
        userAccount = createEntity();
    }

    @Test
    void createUserAccount() throws Exception {
        int databaseSizeBeforeCreate = userAccountRepository.findAll().collectList().block().size();
        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeCreate + 1);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testUserAccount.getAvailableBalance()).isEqualByComparingTo(DEFAULT_AVAILABLE_BALANCE);
        assertThat(testUserAccount.getCurrentBalance()).isEqualByComparingTo(DEFAULT_CURRENT_BALANCE);
        assertThat(testUserAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testUserAccount.getBankCode()).isEqualTo(DEFAULT_BANK_CODE);
        assertThat(testUserAccount.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
    }

    @Test
    void createUserAccountWithExistingId() throws Exception {
        // Create the UserAccount with an existing ID
        userAccount.setId("existing_id");
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        int databaseSizeBeforeCreate = userAccountRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().collectList().block().size();
        // set the field null
        userAccount.setAccountNumber(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAvailableBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().collectList().block().size();
        // set the field null
        userAccount.setAvailableBalance(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCurrentBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().collectList().block().size();
        // set the field null
        userAccount.setCurrentBalance(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().collectList().block().size();
        // set the field null
        userAccount.setAccountName(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBankCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().collectList().block().size();
        // set the field null
        userAccount.setBankCode(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBankNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().collectList().block().size();
        // set the field null
        userAccount.setBankName(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserAccounts() {
        // Initialize the database
        userAccountRepository.save(userAccount).block();

        // Get all the userAccountList
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
            .value(hasItem(userAccount.getId()))
            .jsonPath("$.[*].accountNumber")
            .value(hasItem(DEFAULT_ACCOUNT_NUMBER))
            .jsonPath("$.[*].availableBalance")
            .value(hasItem(sameNumber(DEFAULT_AVAILABLE_BALANCE)))
            .jsonPath("$.[*].currentBalance")
            .value(hasItem(sameNumber(DEFAULT_CURRENT_BALANCE)))
            .jsonPath("$.[*].accountName")
            .value(hasItem(DEFAULT_ACCOUNT_NAME))
            .jsonPath("$.[*].bankCode")
            .value(hasItem(DEFAULT_BANK_CODE))
            .jsonPath("$.[*].bankName")
            .value(hasItem(DEFAULT_BANK_NAME));
    }

    @Test
    void getUserAccount() {
        // Initialize the database
        userAccountRepository.save(userAccount).block();

        // Get the userAccount
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userAccount.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userAccount.getId()))
            .jsonPath("$.accountNumber")
            .value(is(DEFAULT_ACCOUNT_NUMBER))
            .jsonPath("$.availableBalance")
            .value(is(sameNumber(DEFAULT_AVAILABLE_BALANCE)))
            .jsonPath("$.currentBalance")
            .value(is(sameNumber(DEFAULT_CURRENT_BALANCE)))
            .jsonPath("$.accountName")
            .value(is(DEFAULT_ACCOUNT_NAME))
            .jsonPath("$.bankCode")
            .value(is(DEFAULT_BANK_CODE))
            .jsonPath("$.bankName")
            .value(is(DEFAULT_BANK_NAME));
    }

    @Test
    void getNonExistingUserAccount() {
        // Get the userAccount
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount).block();

        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();

        // Update the userAccount
        UserAccount updatedUserAccount = userAccountRepository.findById(userAccount.getId()).block();
        updatedUserAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .currentBalance(UPDATED_CURRENT_BALANCE)
            .accountName(UPDATED_ACCOUNT_NAME)
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(updatedUserAccount);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAccountDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testUserAccount.getAvailableBalance()).isEqualByComparingTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testUserAccount.getCurrentBalance()).isEqualByComparingTo(UPDATED_CURRENT_BALANCE);
        assertThat(testUserAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testUserAccount.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testUserAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);
    }

    @Test
    void putNonExistingUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAccountDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount).block();

        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .currentBalance(UPDATED_CURRENT_BALANCE)
            .accountName(UPDATED_ACCOUNT_NAME)
            .bankName(UPDATED_BANK_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testUserAccount.getAvailableBalance()).isEqualByComparingTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testUserAccount.getCurrentBalance()).isEqualByComparingTo(UPDATED_CURRENT_BALANCE);
        assertThat(testUserAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testUserAccount.getBankCode()).isEqualTo(DEFAULT_BANK_CODE);
        assertThat(testUserAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);
    }

    @Test
    void fullUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount).block();

        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .currentBalance(UPDATED_CURRENT_BALANCE)
            .accountName(UPDATED_ACCOUNT_NAME)
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testUserAccount.getAvailableBalance()).isEqualByComparingTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testUserAccount.getCurrentBalance()).isEqualByComparingTo(UPDATED_CURRENT_BALANCE);
        assertThat(testUserAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testUserAccount.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testUserAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);
    }

    @Test
    void patchNonExistingUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userAccountDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().collectList().block().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserAccount() {
        // Initialize the database
        userAccountRepository.save(userAccount).block();

        int databaseSizeBeforeDelete = userAccountRepository.findAll().collectList().block().size();

        // Delete the userAccount
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userAccount.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserAccount> userAccountList = userAccountRepository.findAll().collectList().block();
        assertThat(userAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
