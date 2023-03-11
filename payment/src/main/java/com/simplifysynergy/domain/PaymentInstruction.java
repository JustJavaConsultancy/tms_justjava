package com.simplifysynergy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplifysynergy.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PaymentInstruction.
 */
@Document(collection = "payment_instruction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentInstruction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("source_account")
    private String sourceAccount;

    @NotNull(message = "must not be null")
    @Field("destination_account")
    private String destinationAccount;

    @Field("narration")
    private String narration;

    @NotNull(message = "must not be null")
    @Field("amount")
    private BigDecimal amount;

    @Field("source_name")
    private String sourceName;

    @Field("destination_name")
    private String destinationName;

    @NotNull(message = "must not be null")
    @Field("source_bank_account_code")
    private String sourceBankAccountCode;

    @NotNull(message = "must not be null")
    @Field("destination_bank_account_code")
    private String destinationBankAccountCode;

    @Field("payment_status")
    private PaymentStatus paymentStatus;

    @Field("paymentBatch")
    @JsonIgnoreProperties(value = { "paymentInstructions" }, allowSetters = true)
    private PaymentBatch paymentBatch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PaymentInstruction id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceAccount() {
        return this.sourceAccount;
    }

    public PaymentInstruction sourceAccount(String sourceAccount) {
        this.setSourceAccount(sourceAccount);
        return this;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestinationAccount() {
        return this.destinationAccount;
    }

    public PaymentInstruction destinationAccount(String destinationAccount) {
        this.setDestinationAccount(destinationAccount);
        return this;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getNarration() {
        return this.narration;
    }

    public PaymentInstruction narration(String narration) {
        this.setNarration(narration);
        return this;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public PaymentInstruction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public PaymentInstruction sourceName(String sourceName) {
        this.setSourceName(sourceName);
        return this;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestinationName() {
        return this.destinationName;
    }

    public PaymentInstruction destinationName(String destinationName) {
        this.setDestinationName(destinationName);
        return this;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getSourceBankAccountCode() {
        return this.sourceBankAccountCode;
    }

    public PaymentInstruction sourceBankAccountCode(String sourceBankAccountCode) {
        this.setSourceBankAccountCode(sourceBankAccountCode);
        return this;
    }

    public void setSourceBankAccountCode(String sourceBankAccountCode) {
        this.sourceBankAccountCode = sourceBankAccountCode;
    }

    public String getDestinationBankAccountCode() {
        return this.destinationBankAccountCode;
    }

    public PaymentInstruction destinationBankAccountCode(String destinationBankAccountCode) {
        this.setDestinationBankAccountCode(destinationBankAccountCode);
        return this;
    }

    public void setDestinationBankAccountCode(String destinationBankAccountCode) {
        this.destinationBankAccountCode = destinationBankAccountCode;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public PaymentInstruction paymentStatus(PaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentBatch getPaymentBatch() {
        return this.paymentBatch;
    }

    public void setPaymentBatch(PaymentBatch paymentBatch) {
        this.paymentBatch = paymentBatch;
    }

    public PaymentInstruction paymentBatch(PaymentBatch paymentBatch) {
        this.setPaymentBatch(paymentBatch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentInstruction)) {
            return false;
        }
        return id != null && id.equals(((PaymentInstruction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentInstruction{" +
            "id=" + getId() +
            ", sourceAccount='" + getSourceAccount() + "'" +
            ", destinationAccount='" + getDestinationAccount() + "'" +
            ", narration='" + getNarration() + "'" +
            ", amount=" + getAmount() +
            ", sourceName='" + getSourceName() + "'" +
            ", destinationName='" + getDestinationName() + "'" +
            ", sourceBankAccountCode='" + getSourceBankAccountCode() + "'" +
            ", destinationBankAccountCode='" + getDestinationBankAccountCode() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            "}";
    }
}
