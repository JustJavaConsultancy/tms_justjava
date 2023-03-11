package com.simplifysynergy.service.dto;

import com.simplifysynergy.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.PaymentInstruction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentInstructionDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String sourceAccount;

    @NotNull(message = "must not be null")
    private String destinationAccount;

    private String narration;

    @NotNull(message = "must not be null")
    private BigDecimal amount;

    private String sourceName;

    private String destinationName;

    @NotNull(message = "must not be null")
    private String sourceBankAccountCode;

    @NotNull(message = "must not be null")
    private String destinationBankAccountCode;

    private PaymentStatus paymentStatus;

    private PaymentBatchDTO paymentBatch;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getSourceBankAccountCode() {
        return sourceBankAccountCode;
    }

    public void setSourceBankAccountCode(String sourceBankAccountCode) {
        this.sourceBankAccountCode = sourceBankAccountCode;
    }

    public String getDestinationBankAccountCode() {
        return destinationBankAccountCode;
    }

    public void setDestinationBankAccountCode(String destinationBankAccountCode) {
        this.destinationBankAccountCode = destinationBankAccountCode;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentBatchDTO getPaymentBatch() {
        return paymentBatch;
    }

    public void setPaymentBatch(PaymentBatchDTO paymentBatch) {
        this.paymentBatch = paymentBatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentInstructionDTO)) {
            return false;
        }

        PaymentInstructionDTO paymentInstructionDTO = (PaymentInstructionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentInstructionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentInstructionDTO{" +
            "id='" + getId() + "'" +
            ", sourceAccount='" + getSourceAccount() + "'" +
            ", destinationAccount='" + getDestinationAccount() + "'" +
            ", narration='" + getNarration() + "'" +
            ", amount=" + getAmount() +
            ", sourceName='" + getSourceName() + "'" +
            ", destinationName='" + getDestinationName() + "'" +
            ", sourceBankAccountCode='" + getSourceBankAccountCode() + "'" +
            ", destinationBankAccountCode='" + getDestinationBankAccountCode() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentBatch=" + getPaymentBatch() +
            "}";
    }
}
